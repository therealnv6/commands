package io.github.devrawr.commands.command

import io.github.devrawr.commands.Commands
import io.github.devrawr.commands.Locale
import java.lang.reflect.Method

class WrappedCommand(
    val name: Array<String>,
    val instance: Any,
    val method: Method,
    var parent: WrappedCommand? = null
)
{
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