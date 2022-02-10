package io.github.devrawr.commands.command.argument.context.defaults

import io.github.devrawr.commands.command.argument.context.ArgumentContext

object IntegerArgumentContext : ArgumentContext<Int>
{
    override fun fromString(value: String) = value.toIntOrNull()
}