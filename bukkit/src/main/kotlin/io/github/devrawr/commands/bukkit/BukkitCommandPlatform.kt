package io.github.devrawr.commands.bukkit

import io.github.devrawr.commands.CommandPlatform
import io.github.devrawr.commands.Locale
import io.github.devrawr.commands.bukkit.processor.BukkitCommand
import io.github.devrawr.commands.bukkit.processor.BukkitCommandProcessor
import io.github.devrawr.commands.bukkit.processor.executor.BukkitExecutorProcessor
import io.github.devrawr.commands.bukkit.processor.help.BukkitHelpProcessor
import io.github.devrawr.commands.command.WrappedCommand
import io.github.devrawr.commands.processor.help.HelpProcessor
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.CommandMap
import java.util.*

object BukkitCommandPlatform : CommandPlatform()
{
    override val executorProcessor = BukkitExecutorProcessor
    override val commandProcessor = BukkitCommandProcessor
    override val helpProcessor = BukkitHelpProcessor

    var fallback: String = "commands"

    private val commandMap by lazy {
        val pluginManager = Bukkit.getPluginManager()
        val field = pluginManager.javaClass
            .getDeclaredField("commandMap")
            .apply {
                this.isAccessible = true
            }

        return@lazy field.get(pluginManager) as CommandMap
    }

    init
    {
        for (entry in Locale.locales.entries)
        {
            Locale.locales[entry.key]!!["cli-id"] = UUID.randomUUID().toString()
        }
    }

    override fun registerCommand(command: WrappedCommand)
    {
        commandMap.register(fallback, BukkitCommand(command))
    }
}