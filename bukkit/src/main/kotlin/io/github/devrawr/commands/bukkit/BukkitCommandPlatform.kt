package io.github.devrawr.commands.bukkit

import io.github.devrawr.commands.CommandPlatform
import io.github.devrawr.commands.Locale
import io.github.devrawr.commands.bukkit.processor.BukkitCommand
import io.github.devrawr.commands.bukkit.processor.executor.BukkitExecutorProcessor
import io.github.devrawr.commands.bukkit.processor.help.BukkitHelpProcessor
import io.github.devrawr.commands.command.WrappedCommand
import org.bukkit.Bukkit
import org.bukkit.command.CommandMap
import org.bukkit.command.SimpleCommandMap
import org.bukkit.plugin.PluginManager
import org.bukkit.plugin.SimplePluginManager
import java.lang.reflect.Field
import java.util.*

object BukkitCommandPlatform : CommandPlatform()
{
    override val executorProcessor = BukkitExecutorProcessor
    override val helpProcessor = BukkitHelpProcessor

    var fallback: String = "commands"

    // TODO: 2/11/2022 clean up code
    private val commandMap by lazy {
        val pluginManager = Bukkit.getPluginManager()

        val mapField = this.getFieldWrapped<SimplePluginManager>("commandMap")
        val knownCommandsField = this.getFieldWrapped<SimpleCommandMap>("knownCommands")

        val map = BukkitCommandMap
        val oldMap = mapField.get(pluginManager) as CommandMap

        knownCommandsField.set(map, knownCommandsField[oldMap]!!)
        mapField.set(pluginManager, map)

        return@lazy map
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

    private inline fun <reified T : Any> getFieldWrapped(field: String): Field
    {
        return T::class.java.getDeclaredField(field).apply {
            this.isAccessible = true
        }
    }
}