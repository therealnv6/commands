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
        val field = pluginManager.javaClass
            .getDeclaredField("commandMap")
            .apply {
                this.isAccessible = true
            }

        return@lazy field.get(pluginManager) as CommandMap
    }

    init
    {
        Locale.locales["bukkit"] = hashMapOf(
            "user-not-found" to "User could not be parsed from provided executor.",
            "unable-to-parse-executor" to "Executor could not be parsed from provided user.",
            "does-not-meet-arguments" to "Incorrect usage, try: /{label} {arguments}",
            "unable-to-parse-argument" to "Unable to parse argument from \"{arg}\"",
            "required-argument" to "<{name}>",
            "optional-argument" to "[{name}]",
            "error-prefix" to "${ChatColor.RED}Error: ${ChatColor.WHITE}",
            "vararg-argument" to "..."
        )
    }

    override fun registerCommand(command: WrappedCommand)
    {
        commandMap.register(fallback, BukkitCommand(command))
    }
}