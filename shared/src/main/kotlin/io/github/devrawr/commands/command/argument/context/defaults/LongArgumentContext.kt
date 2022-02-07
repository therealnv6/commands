package io.github.devrawr.commands.command.argument.context.defaults

import io.github.devrawr.commands.command.argument.context.ArgumentContext

object LongArgumentContext : ArgumentContext<Long>
{
    override fun fromString(value: String): Long?
    {
        return value.toLongOrNull()
    }
}