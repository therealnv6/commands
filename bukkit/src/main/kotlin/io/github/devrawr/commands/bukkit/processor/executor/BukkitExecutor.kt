package io.github.devrawr.commands.bukkit.processor.executor

import io.github.devrawr.commands.Locale
import io.github.devrawr.commands.processor.executor.Executor
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

class BukkitExecutor(val sender: CommandSender) : Executor<CommandSender>()
{
    override val localeType = Locale.defaultLocale
    override val id: UUID by lazy {
        if (sender is Player)
        {
            sender.uniqueId
        } else
        {
            Locale.retrieveLocaleField("cli-id")
        }
    }

    override fun sendMessage(message: String)
    {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message))
    }

    override fun hasPermission(permission: String): Boolean
    {
        return sender.hasPermission(permission)
    }

    override fun appliesToUser(type: Class<*>): Boolean
    {
        return CommandSender::class.java.isAssignableFrom(type)
    }

    override fun toUser(): CommandSender
    {
        return this.sender
    }
}