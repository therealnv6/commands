package io.github.devrawr.commands.bukkit.processor

import io.github.devrawr.commands.Locale
import io.github.devrawr.commands.bukkit.BukkitCommandPlatform
import io.github.devrawr.commands.command.WrappedCommand
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class BukkitCommand(val command: WrappedCommand) : Command(command.label)
{
    init
    {
        this.aliases = command.name.toMutableList()
        this.permission = command.permission
    }

    override fun execute(
        sender: CommandSender,
        label: String,
        args: Array<out String>
    ): Boolean
    {
        val executor = BukkitCommandPlatform.executorProcessor.fromUser(sender)

        if (executor == null)
        {
            sender.sendMessage(
                ChatColor.translateAlternateColorCodes(
                    '&',
                    Locale.retrieveLocale(executor)["unable-to-parse-executor"]!!
                )
            )
            return true
        }

        BukkitCommandPlatform.commandProcessor.process(
            executor,
            command,
            args.toList()
        )

        return true
    }
}