package io.github.devrawr.commands.bukkit.processor.executor

import io.github.devrawr.commands.processor.executor.ExecutorProcessor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object BukkitExecutorProcessor : ExecutorProcessor<BukkitExecutor>()
{
    override fun toUser(
        executor: BukkitExecutor
    ): Any
    {
        val sender = executor.sender

        return if (sender is Player)
            sender else sender
    }

    override fun fromUser(
        user: Any
    ): BukkitExecutor?
    {
        if (user !is CommandSender)
        {
            return null
        }

        return BukkitExecutor(user)
    }

    override fun isUser(type: Class<*>): Boolean
    {
        return CommandSender::class.java.isAssignableFrom(type)
    }
}