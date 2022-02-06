package io.github.devrawr.commands.context.defaults

import io.github.devrawr.commands.context.CommandContext

object IntegerCommandContext : CommandContext<Int>
{
    override fun fromString(value: String): Int?
    {
        return value.toIntOrNull()
    }
}