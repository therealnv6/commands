package io.github.devrawr.commands.processor

import io.github.devrawr.commands.CommandPlatform
import io.github.devrawr.commands.Locale
import io.github.devrawr.commands.command.WrappedCommand
import io.github.devrawr.commands.processor.executor.Executor

abstract class CommandProcessor
{
    abstract val platform: CommandPlatform

    open fun process(
        executor: Executor<*>,
        command: WrappedCommand,
        arguments: List<String>
    )
    {
        val user = executor.toUser()

        if (user == null)
        {
            executor.sendMessage(Locale.retrieveLocale(executor)["user-not-found"]!!)
            return
        }

        command.method.invoke(
            command.handleArguments(
                executor = executor,
                args = arguments.toTypedArray()
            )
        )
    }
}