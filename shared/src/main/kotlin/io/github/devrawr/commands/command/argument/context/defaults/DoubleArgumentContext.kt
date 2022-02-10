package io.github.devrawr.commands.command.argument.context.defaults

import io.github.devrawr.commands.command.argument.context.ArgumentContext

object DoubleArgumentContext : ArgumentContext<Double>
{
    override fun fromString(value: String) = value.toDoubleOrNull()
}