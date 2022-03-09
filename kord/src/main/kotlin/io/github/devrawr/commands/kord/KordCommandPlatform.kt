package io.github.devrawr.commands.kord

import dev.kord.core.Kord
import dev.kord.core.entity.User
import dev.kord.core.entity.channel.Channel
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.on
import io.github.devrawr.commands.CommandPlatform
import io.github.devrawr.commands.command.argument.context.Contexts
import io.github.devrawr.commands.kord.argument.context.ChannelArgumentContext
import io.github.devrawr.commands.kord.argument.context.UserArgumentContext
import io.github.devrawr.commands.kord.processor.executor.KordExecutorProcessor
import io.github.devrawr.commands.kord.processor.help.KordHelpProcessor

object KordCommandPlatform : CommandPlatform()
{
    override val executorProcessor = KordExecutorProcessor
    override val helpProcessor = KordHelpProcessor

    lateinit var kord: Kord

    var prefix: String = "!"

    init
    {
        Contexts.useContext<Channel, ChannelArgumentContext>()
        Contexts.useContext<User, UserArgumentContext>()
    }

    fun use(kord: Kord)
    {
        this.kord = kord

        kord.on<MessageCreateEvent> {
            KordCommandListener.listenToMessage(this.message)
        }

        kord.on<> {  }
    }
}