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
        println("creating help body")

        val page = pageMap.putIfAbsent(executor.id, 0) ?: pageMap[executor.id]!!

        println("page is $page")

        val min = page * entryPerPage
        val max = min + this.entryPerPage

        println("min/max is $min, $max respectively")

        var entries = this.getHelpEntries(executor)

        if (entries.size > max)
        {
            entries = entries.subList(min, max)
        }

        println("amount of entries is ${entries.size}")

        val message = mutableListOf<String>()

        println("created new message list")

        message.add(
            Locale.retrieveLocaleField<String>(LocaleKeys.HELP_TITLE, executor)
                .replace("{parent}", this.command.label)
        )

        println("added to message, $message")

        for (entry in entries)
        {
            println("found new entry!")

            message.add(
                Locale.retrieveLocaleField<String>(LocaleKeys.HELP_ENTRY, executor)
                    .replace("{label}", entry.label)
                    .replace("{args}", entry.formatArguments(executor))
                    .replace("{description}", command.description)
            )
        }

        message.add(
            Locale.retrieveLocaleField<String>(LocaleKeys.HELP_FOOTER, executor)
                .replace("{page-current}", page.toString())
                .replace("{page-max}", ((this.command.children.size + 1) / entryPerPage).toString())
                .replace("{results}", entries.size.toString())
        )

        println("returning now.")

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