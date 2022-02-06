package io.github.devrawr.commands.command

import io.github.devrawr.commands.context.CommandContext

class WrappedArgument<T>(
    val name: String,
    val type: Class<T>,
    val context: CommandContext<T>,
    val value: T? = null
)
{
    constructor(name: String, value: Any?, context: CommandContext<*>, type: Class<T>) : this(
        name,
        type,
        context as CommandContext<T>,
        value as T
    )

    fun convertToValue(value: String): T?
    {
        return context.fromString(value)
    }
}