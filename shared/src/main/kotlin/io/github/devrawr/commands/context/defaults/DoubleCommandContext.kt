package io.github.devrawr.commands.context.defaults

import io.github.devrawr.commands.context.CommandContext

object DoubleCommandContext : CommandContext<Double>
{
    override fun fromString(value: String): Double?
    {
        return value.toDoubleOrNull()
    }
}