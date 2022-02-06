package io.github.devrawr.commands.context.defaults

import io.github.devrawr.commands.context.CommandContext

object FloatCommandContext : CommandContext<Float>
{
    override fun fromString(value: String): Float?
    {
        return value.toFloatOrNull()
    }
}