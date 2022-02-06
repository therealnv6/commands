package io.github.devrawr.commands.context

interface CommandContext<T>
{
    fun fromString(value: String): T?
}