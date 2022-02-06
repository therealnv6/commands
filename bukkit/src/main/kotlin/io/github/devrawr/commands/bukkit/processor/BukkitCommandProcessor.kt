package io.github.devrawr.commands.bukkit.processor

import io.github.devrawr.commands.bukkit.BukkitCommandPlatform
import io.github.devrawr.commands.processor.CommandProcessor

object BukkitCommandProcessor : CommandProcessor()
{
    override val platform = BukkitCommandPlatform
}