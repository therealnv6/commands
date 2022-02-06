package io.github.devrawr.commands.command

import io.github.devrawr.commands.Commands
import java.lang.reflect.Method

class WrappedCommand(
    val name: Array<String>,
    val instance: Any,
    var parent: WrappedCommand? = null
)
{
    lateinit var method: Method

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

            return@map Commands.currentLocale[localeType]!!.replace("{name}", it.name)
        }.joinToString(" ")
}