package io.github.devrawr.commands.processor.help

import io.github.devrawr.commands.Locale
import io.github.devrawr.commands.LocaleKeys
import io.github.devrawr.commands.Platforms
import io.github.devrawr.commands.command.WrappedCommand
import io.github.devrawr.commands.processor.executor.Executor
import java.util.*

class HelpTopic(val command: WrappedCommand)
{
    val processor: HelpProcessor<*> = Platforms.usedPlatform!!.helpProcessor

    val pageMap = hashMapOf<UUID, Int>()
    val entryPerPage = Locale.retrieveLocaleField<Int>(LocaleKeys.HELP_ENTRY_PER_PAGE)

    fun createHelpBody(executor: Executor<*>): HelpBody
    {
        val page = pageMap.putIfAbsent(executor.id, 0) ?: pageMap[executor.id]!!

        val min = page * entryPerPage
        val max = min + this.entryPerPage

        val entries = this.getHelpEntries(executor).subList(min, max)
        val message = mutableListOf<String>()

        message.add(
            Locale.retrieveLocaleField<String>(LocaleKeys.HELP_TITLE, executor)
                .replace("{parent}", this.command.label)
        )

        return processor.createBody(message)
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
    fun getHelpEntries(executor: Executor<*>): List<WrappedCommand>
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