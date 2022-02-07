package io.github.devrawr.commands.command.argument.context

interface ArgumentContext<T>
{
    fun fromString(value: String): T?
}