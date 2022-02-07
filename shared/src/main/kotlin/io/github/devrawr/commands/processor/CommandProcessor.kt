package io.github.devrawr.commands.processor

import io.github.devrawr.commands.CommandPlatform
import io.github.devrawr.commands.Locale
import io.github.devrawr.commands.command.WrappedCommand
import io.github.devrawr.commands.processor.executor.Executor

abstract class CommandProcessor
{
    abstract val platform: CommandPlatform

    open fun process(
        executor: Executor<*>,
        command: WrappedCommand,
        arguments: List<String>
    )
    {
        val user = executor.toUser()

        if (user == null)
        {
            executor.sendMessage(Locale.retrieveLocale(executor)["user-not-found"]!!)
            return
        }

        val data = hashMapOf<Int, Any?>()

        for ((index, argument) in command.arguments.withIndex())
        {
            val firstNull = data.size

            if (firstNull == command.arguments.size)
            {
                break
            }

            if (data[index] != null)
            {
                continue
            }

            if (executor.appliesToUser(argument.type))
            {
                data[index] = user
                continue
            }

            if (arguments.size - 1 < index && argument.value == null)
            {
                executor.sendMessage(
                    Locale.retrieveLocale(executor)["does-not-meet-arguments"]!!
                        .replace("{label}", command.label)
                        .replace("{arguments}", command.formatArguments())
                )
                return
            }

            data[index] = if (arguments.size - 1 < index)
            {
                argument.value
            } else if (index == command.arguments.size - 1 && arguments.size > index && argument.type == Array<String>::class.java)
            {
                arguments.subList(index, arguments.size - 1).toTypedArray()
            } else
            {
                argument.convertToValue(arguments[index])
            }
        }

        data.values.toTypedArray().let {
            command.method.invoke(it)
        }
    }
}