package io.github.devrawr.commands.kord.argument.context

import dev.kord.core.entity.channel.Channel
import io.github.devrawr.commands.command.argument.context.ArgumentContext
import io.github.devrawr.commands.kord.processor.executor.KordExecutor
import io.github.devrawr.commands.processor.executor.Executor
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking

object ChannelArgumentContext : ArgumentContext<Channel>
{
    override fun fromString(executor: Executor<*>?, value: String): Channel?
    {
        if (executor == null)
        {
            return null
        }

        val message = (executor as KordExecutor).message

        return runBlocking {
            message.mentionedChannels.firstOrNull {
                it.mention == value
            }
        }
    }
}