package io.github.devrawr.commands.command

import java.lang.reflect.Method

class WrappedCommand(
    val name: Array<String>,
    var parent: WrappedCommand? = null
)
{
    lateinit var method: Method

    val children = mutableListOf<WrappedCommand>()
    var arguments = mutableListOf<WrappedArgument<*>>()

    val label = name.first()

    var permission: String = ""
}