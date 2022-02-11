package io.github.devrawr.commands.bukkit

import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.command.SimpleCommandMap

object BukkitCommandMap : SimpleCommandMap(Bukkit.getServer())
{
    override fun tabComplete(
        sender: CommandSender,
        cmdLine: String
    ): MutableList<String>
    {
        val tabCompleteProcessor = BukkitCommandPlatform.tabCompletionProcessor
        val executorProcessor = BukkitCommandPlatform.executorProcessor

        return tabCompleteProcessor.modifyTabCompletion(
            executor = executorProcessor.fromUser(sender)!!,
            initial = super.tabComplete(sender, cmdLine),
            query = cmdLine
        ).toMutableList()
    }
}