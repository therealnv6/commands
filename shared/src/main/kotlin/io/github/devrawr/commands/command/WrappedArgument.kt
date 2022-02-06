package io.github.devrawr.commands.command

class WrappedArgument<T>(
    val name: String,
    val type: Class<T>,
    val value: T? = null
)
{
    constructor(name: String, value: Any?, type: Class<T>) : this(
        name,
        type,
        value as T
    )
}