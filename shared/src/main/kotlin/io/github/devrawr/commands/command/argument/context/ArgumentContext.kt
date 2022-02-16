package io.github.devrawr.commands.command.argument.context

import io.github.devrawr.commands.processor.executor.Executor

interface ArgumentContext<T>
{
    fun fromString(executor: Executor<*>?, value: String): T?
}