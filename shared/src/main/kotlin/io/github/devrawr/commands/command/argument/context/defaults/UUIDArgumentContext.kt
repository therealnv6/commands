package io.github.devrawr.commands.command.argument.context.defaults

import io.github.devrawr.commands.command.argument.context.ArgumentContext
import java.util.*

object UUIDArgumentContext : ArgumentContext<UUID>
{
    override fun fromString(value: String): UUID? = UUID.fromString(value)
}