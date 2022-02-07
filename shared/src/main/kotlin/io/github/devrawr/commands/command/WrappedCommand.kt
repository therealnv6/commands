package io.github.devrawr.commands.command

import io.github.devrawr.commands.Locale
import java.lang.reflect.Method

class WrappedCommand(
    val name: Array<String>,
    var parent: WrappedCommand? = null,
    val method: (Array<Any?>) -> Unit
)
{
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


    val children = mutableListOf<WrappedCommand>()
    var arguments = mutableListOf<WrappedArgument<*>>()

    val label = name.first()

    var permission: String = ""

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
}