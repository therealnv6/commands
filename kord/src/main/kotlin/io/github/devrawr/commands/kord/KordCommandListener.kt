package io.github.devrawr.commands.kord

import dev.kord.core.entity.Message
import io.github.devrawr.commands.kord.processor.executor.KordExecutor
import io.github.devrawr.commands.kord.processor.executor.KordExecutorProcessor

object KordCommandListener
{
    fun listenToMessage(message: Message)
    {
        val content = message.content
        val arguments = content.split(" ")

        if (content.startsWith(KordCommandPlatform.prefix))
        {
            val label = arguments[0].replaceFirst(KordCommandPlatform.prefix, "")
            val command = KordCommandPlatform.commands
                .firstOrNull {
                    it.name.contains(label)
                }

            if (command != null)
            {
                val user = message.author!!
                val executor = KordExecutor(message)

                KordExecutorProcessor.executorMap[user] = executor

                KordCommandPlatform.processor.process(
                    executor,
                    command,
                    arguments.subList(1, arguments.size - 1)
                )
            }
        }
    }
}