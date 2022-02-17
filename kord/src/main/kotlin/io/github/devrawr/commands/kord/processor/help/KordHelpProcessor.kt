package io.github.devrawr.commands.kord.processor.help

import dev.kord.rest.builder.message.EmbedBuilder
import io.github.devrawr.commands.Locale
import io.github.devrawr.commands.LocaleKeys
import io.github.devrawr.commands.kord.KordCommandPlatform
import io.github.devrawr.commands.kord.processor.executor.KordExecutor
import io.github.devrawr.commands.processor.executor.Executor
import io.github.devrawr.commands.processor.help.HelpBody
import io.github.devrawr.commands.processor.help.HelpProcessor
import io.github.devrawr.commands.processor.help.HelpTopicData
import kotlinx.coroutines.runBlocking

object KordHelpProcessor : HelpProcessor
{
    private val entryPerPage = Locale.retrieveLocaleField<Int>(LocaleKeys.HELP_ENTRY_PER_PAGE)

    override fun createBody(
        executor: Executor<*>,
        data: HelpTopicData
    ): HelpBody
    {
        return KordHelpBody {
            val min = data.page * entryPerPage
            val max = min + entryPerPage

            var entries = data.children

            if (entries.size > max)
            {
                entries = entries.subList(min, max)
            }

            field {
                name = "Help Page ${data.page}/${data.pageMax}"
                value = entries.joinToString("\n") {
                    var label = data.parent.label

                    if (it.method != data.parent.method)
                    {
                        label += " ${it.label}"
                    }

                    "${KordCommandPlatform.prefix}`$label` - ${it.description}"
                }
            }
        }
    }
}

class KordHelpBody(private val message: EmbedBuilder.() -> Unit) : HelpBody()
{
    override fun sendBodyToExecutor(executor: Executor<*>)
    {
        if (executor is KordExecutor)
        {
            runBlocking {
                executor.sendEmbed(message)
            }
        }
    }

    override fun getAsString(): String
    {
        return ""
    }
}