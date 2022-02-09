package io.github.devrawr.commands.bukkit.processor.help

import io.github.devrawr.commands.Locale
import io.github.devrawr.commands.Platforms
import io.github.devrawr.commands.bukkit.BukkitCommandPlatform
import io.github.devrawr.commands.bukkit.processor.BukkitCommand
import org.bukkit.command.CommandSender
import org.bukkit.help.GenericCommandHelpTopic
import java.lang.IllegalStateException
import java.lang.RuntimeException

class CustomBukkitHelpTopic(private val bukkitCommand: BukkitCommand) : GenericCommandHelpTopic(bukkitCommand)
{
    override fun getFullText(forWho: CommandSender): String
    {
        val platform = Platforms.usedPlatform

        if (platform !is BukkitCommandPlatform)
        {
            throw IllegalStateException("May not use ${this.javaClass.name} from ${Platforms.usedPlatform!!.javaClass.name} context.")
        }

        val executor = platform.executorProcessor.fromUser(forWho)
            ?: throw RuntimeException(
                Locale.retrieveLocale()["unable-to-parse-executor"]!!
            )

        return bukkitCommand.command.helpTopic
            .createHelpBody(executor)
            .getAsString()
    }
}