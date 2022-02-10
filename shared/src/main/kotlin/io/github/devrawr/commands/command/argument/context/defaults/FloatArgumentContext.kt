package io.github.devrawr.commands.command.argument.context.defaults

import io.github.devrawr.commands.command.argument.context.ArgumentContext

object FloatArgumentContext : ArgumentContext<Float>
{
    override fun fromString(value: String) = value.toFloatOrNull()
}