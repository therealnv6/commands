package io.github.devrawr.commands.context.defaults

import io.github.devrawr.commands.context.CommandContext

object StringCommandContext : CommandContext<String>
{
    override fun fromString(value: String): String
    {
        return value
    }
}