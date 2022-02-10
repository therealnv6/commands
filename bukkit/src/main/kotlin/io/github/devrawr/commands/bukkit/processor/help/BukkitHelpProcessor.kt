package io.github.devrawr.commands.bukkit.processor.help

import io.github.devrawr.commands.processor.executor.Executor
import io.github.devrawr.commands.processor.help.HelpBody
import io.github.devrawr.commands.processor.help.HelpProcessor
import org.bukkit.ChatColor

object BukkitHelpProcessor : HelpProcessor
{
    override fun createBody(message: List<String>): HelpBody
    {
        return BukkitHelpBody(message)
    }
}

class BukkitHelpBody(private val message: List<String>) : HelpBody()
{
    override fun sendBodyToExecutor(executor: Executor<*>)
    {
        println("sending body to executor, ${getAsString()}")

        executor.sendMessage(getAsString())
    }

    override fun getAsString(): String
    {
        return message.joinToString("\n") {
            ChatColor.translateAlternateColorCodes('&', it)
        }
    }
}