package io.github.devrawr.commands.command.argument.context.defaults

import io.github.devrawr.commands.command.argument.context.ArgumentContext
import io.github.devrawr.commands.processor.executor.Executor

object IntegerArgumentContext : ArgumentContext<Int>
{
    override fun fromString(executor: Executor<*>?, value: String) = value.toIntOrNull()
}