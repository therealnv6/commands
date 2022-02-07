package io.github.devrawr.commands.bukkit

import io.github.devrawr.commands.CommandPlatform
import io.github.devrawr.commands.Locale
import io.github.devrawr.commands.bukkit.processor.BukkitCommandProcessor
import io.github.devrawr.commands.bukkit.processor.executor.BukkitExecutorProcessor
import io.github.devrawr.commands.command.WrappedCommand
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.CommandMap

object BukkitCommandPlatform : CommandPlatform()
{
    override val executorProcessor = BukkitExecutorProcessor
    override val commandProcessor = BukkitCommandProcessor

    var fallback: String = "commands"

    private val commandMap by lazy {
        val pluginManager = Bukkit.getPluginManager()
        val field = pluginManager.javaClass.getDeclaredField("commandMap")

        return@lazy field.apply {
            this.isAccessible = true
        }.get(pluginManager) as CommandMap
    }

    init
    {
        Locale.locales["en_US"]!!["error-prefix"] = "${ChatColor.RED}Error: "
    }

    override fun registerCommand(command: WrappedCommand)
    {
        commandMap.register(fallback, BukkitCommand(command))
    }
}