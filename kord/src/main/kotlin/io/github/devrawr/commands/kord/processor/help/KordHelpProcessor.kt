package io.github.devrawr.commands.kord.processor.help

import dev.kord.rest.builder.message.EmbedBuilder
import io.github.devrawr.commands.kord.processor.executor.KordExecutor
import io.github.devrawr.commands.processor.executor.Executor
import io.github.devrawr.commands.processor.help.HelpBody
import io.github.devrawr.commands.processor.help.HelpProcessor
import kotlinx.coroutines.runBlocking

object KordHelpProcessor : HelpProcessor
{
    override fun createBody(message: List<String>): HelpBody
    {
        return KordHelpBody {
            this.title = "Help Menu"

            this.field {
                this.name = ":orange_book:"
                this.value = message.joinToString("\n")
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