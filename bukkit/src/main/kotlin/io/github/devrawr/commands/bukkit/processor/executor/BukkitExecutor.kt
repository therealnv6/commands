package io.github.devrawr.commands.bukkit.processor.executor

import io.github.devrawr.commands.processor.executor.Executor
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender

class BukkitExecutor(val sender: CommandSender) : Executor<CommandSender>()
{
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
        return type.isAssignableFrom(CommandSender::class.java)
    }

    override fun toUser(): CommandSender
    {
        return this.sender
    }
}