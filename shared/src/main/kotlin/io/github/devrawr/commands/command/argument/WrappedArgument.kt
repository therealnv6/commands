package io.github.devrawr.commands.command.argument

import io.github.devrawr.commands.command.argument.context.ArgumentContext
import io.github.devrawr.commands.processor.executor.Executor

class WrappedArgument<T>(
    val name: String,
    val type: Class<T>,
    val context: ArgumentContext<T>,
    val value: T? = null
)
{
    constructor(name: String, value: Any?, context: ArgumentContext<*>, type: Class<T>) : this(
        name,
        type,
        context as ArgumentContext<T>,
        value as T
    )

    fun convertToValue(executor: Executor<*>, value: String): T?
    {
        return context.fromString(executor, value)
    }
}