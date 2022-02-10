package io.github.devrawr.commands.kord

import dev.kord.core.Kord
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.on
import io.github.devrawr.commands.CommandPlatform
import io.github.devrawr.commands.kord.processor.executor.KordExecutorProcessor
import io.github.devrawr.commands.kord.processor.help.KordHelpProcessor

object KordCommandPlatform : CommandPlatform()
{
    override val executorProcessor = KordExecutorProcessor
    override val helpProcessor = KordHelpProcessor

    var prefix: String = "!"

    fun use(kord: Kord)
    {
        kord.on<MessageCreateEvent> {
            KordCommandListener.listenToMessage(this.message)
        }
    }
}