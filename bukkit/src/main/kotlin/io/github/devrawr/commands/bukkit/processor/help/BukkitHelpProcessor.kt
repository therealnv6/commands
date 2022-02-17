package io.github.devrawr.commands.bukkit.processor.help

import io.github.devrawr.commands.Locale
import io.github.devrawr.commands.LocaleKeys
import io.github.devrawr.commands.processor.executor.Executor
import io.github.devrawr.commands.processor.help.HelpBody
import io.github.devrawr.commands.processor.help.HelpProcessor
import io.github.devrawr.commands.processor.help.HelpTopicData
import org.bukkit.ChatColor

object BukkitHelpProcessor : HelpProcessor
{
    private val entryPerPage = Locale.retrieveLocaleField<Int>(LocaleKeys.HELP_ENTRY_PER_PAGE)

    override fun createBody(executor: Executor<*>, data: HelpTopicData): HelpBody
    {
        val min = data.page * entryPerPage
        val max = min + this.entryPerPage

        var entries = data.children

        if (entries.size > max)
        {
            entries = entries.subList(min, max)
        }

        val message = mutableListOf<String>()

        message.add(
            Locale.retrieveLocaleField<String>(LocaleKeys.HELP_TITLE, executor)
                .replace("{parent}", data.parent.label)
        )

        for (entry in entries)
        {
            var label = data.parent.label

            if (entry.method != data.parent.method)
            {
                label += " ${entry.label}"
            }

            message.add(
                Locale.retrieveLocaleField<String>(LocaleKeys.HELP_ENTRY, executor)
                    .replace("{label}", label)
                    .replace("{args}", entry.formatArguments(executor))
                    .replace("{description}", entry.description)
            )
        }

        message.add(
            Locale.retrieveLocaleField<String>(LocaleKeys.HELP_FOOTER, executor)
                .replace("{page-current}", data.page.toString())
                .replace("{page-max}", data.pageMax.toString())
                .replace("{results}", entries.size.toString())
        )

        return BukkitHelpBody(
            message
        )
    }
}

class BukkitHelpBody(private val message: List<String>) : HelpBody()
{
    override fun sendBodyToExecutor(executor: Executor<*>)
    {
        executor.sendMessage(getAsString())
    }

    override fun getAsString(): String
    {
        return message.joinToString("\n") {
            ChatColor.translateAlternateColorCodes('&', it)
        }
    }
}