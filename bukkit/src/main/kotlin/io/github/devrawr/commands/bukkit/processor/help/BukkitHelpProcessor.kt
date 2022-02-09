package io.github.devrawr.commands.bukkit.processor.help

import io.github.devrawr.commands.command.WrappedCommand
import io.github.devrawr.commands.processor.executor.Executor
import io.github.devrawr.commands.processor.help.HelpBody
import io.github.devrawr.commands.processor.help.HelpProcessor

object BukkitHelpProcessor : HelpProcessor<String>
{
    override fun generateHelp(
        page: Int,
        issuer: Executor<*>,
        command: WrappedCommand
    ): String
    {
        TODO("Not yet implemented")
    }

    override fun createBody(message: List<String>): HelpBody
    {
        TODO("Not yet implemented")
    }
}