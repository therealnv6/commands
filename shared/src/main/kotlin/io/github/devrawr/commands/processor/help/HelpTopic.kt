package io.github.devrawr.commands.processor.help

import io.github.devrawr.commands.Locale
import io.github.devrawr.commands.LocaleKeys
import io.github.devrawr.commands.Platforms
import io.github.devrawr.commands.command.WrappedCommand
import io.github.devrawr.commands.processor.executor.Executor
import java.util.*

class HelpTopic(val command: WrappedCommand)
{
    val processor: HelpProcessor = Platforms.usedPlatform!!.helpProcessor
    val pageMap = hashMapOf<UUID, Int>()

    private val entryPerPage = Locale.retrieveLocaleField<Int>(LocaleKeys.HELP_ENTRY_PER_PAGE)

    fun createHelpBody(executor: Executor<*>): HelpBody
    {
        val page = pageMap.putIfAbsent(executor.id, 0) ?: pageMap[executor.id]!!
        val children = this.getHelpEntries(executor)

        return processor.createBody(
            executor = executor,
            data = HelpTopicData(
                parent = this.command,
                children = children,
                results = children.size,
                page = page + 1,
                pageMax = ((this.command.children.size + 1) / entryPerPage) + 1
            )
        )
    }

    /**
     * Get all entries to display within
     * the help message.
     *
     * This method filters out every
     * command/subcommand which the provided
     * executor does not have sufficient
     * permissions for to execute.
     */
    private fun getHelpEntries(executor: Executor<*>): List<WrappedCommand>
    {
        val entries = mutableListOf<WrappedCommand>()

        if (executor.hasPermission(command.permission))
        {
            entries.add(command)
        }

        for (child in command.children)
        {
            if (executor.hasPermission(child.permission))
            {
                entries.add(child)
            }
        }

        return entries.toList()
    }
}

data class HelpTopicData(
    val parent: WrappedCommand,
    val children: List<WrappedCommand>,
    val results: Int,
    val page: Int,
    val pageMax: Int
)