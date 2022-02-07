package io.github.devrawr.commands.command.argument.context.defaults

import io.github.devrawr.commands.command.argument.context.ArgumentContext

object StringArgumentContext : ArgumentContext<String>
{
    override fun fromString(value: String): String
    {
        return value
    }
}