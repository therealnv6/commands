package io.github.devrawr.commands.command.argument.context.defaults

import io.github.devrawr.commands.command.argument.context.ArgumentContext
import io.github.devrawr.commands.processor.executor.Executor
import java.util.*

object UUIDArgumentContext : ArgumentContext<UUID>
{
    override fun fromString(executor: Executor<*>?, value: String): UUID? = UUID.fromString(value)
}