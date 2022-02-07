package io.github.devrawr.commands.command

import io.github.devrawr.commands.Locale
import io.github.devrawr.commands.exception.ArgumentCountException
import io.github.devrawr.commands.processor.executor.Executor
import java.lang.reflect.Method

class WrappedCommand(
    val name: Array<String>,
    var parent: WrappedCommand? = null,
    val method: (Array<Any?>) -> Unit
)
{
    val children = mutableListOf<WrappedCommand>()
    var arguments = mutableListOf<WrappedArgument<*>>()

    val label = name.first()

    var permission: String = ""

    constructor(
        name: Array<String>,
        instance: Any,
        method: Method,
        parent: WrappedCommand? = null
    ) : this(
        name = name,
        parent = parent,
        method = {
            method.invoke(instance, *it)
        }
    )

    fun formatArguments() = arguments
        .map {
            val optional = it.value != null
            val localeType = if (optional)
            {
                "optional-argument"
            } else
            {
                "required-argument"
            }

            return@map Locale.retrieveLocale()[localeType]!!.replace("{name}", it.name)
        }.joinToString(" ")

    fun handleArguments(
        executor: Executor<*>,
        args: Array<String>
    ): Array<Any?>
    {
        val arguments = mutableListOf<Any?>()
        val skipFirst = executor.appliesToUser(this.arguments[0].type)

        if (skipFirst)
        {
            executor.toUser()?.let { arguments.add(it) }
        }

        val start = if (skipFirst)
        {
            1
        } else
        {
            0
        }

        for (i in start..this.arguments.size)
        {
            val argument = this.arguments[i]

            if (arguments.size - 1 < i && argument.value == null)
            {
                val message = Locale.retrieveLocale(executor)["does-not-meet-arguments"]!!
                    .replace("{label}", this.label)
                    .replace("{arguments}", this.formatArguments())

                executor.sendMessage(message)
                throw ArgumentCountException(message)
            }

            arguments.add(
                if (arguments.size - 1 < i)
                {
                    argument.value
                } else if (i == this.arguments.size - 1 && arguments.size > i && argument.type == Array<String>::class.java)
                {
                    arguments.subList(i, args.size - 1).toTypedArray()
                } else
                {
                    argument.convertToValue(args[i])
                }
            )
        }

        return arguments
            .toTypedArray()
    }
}