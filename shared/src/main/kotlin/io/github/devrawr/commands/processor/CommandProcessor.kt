package io.github.devrawr.commands.processor

import io.github.devrawr.commands.CommandPlatform
import io.github.devrawr.commands.Locale
import io.github.devrawr.commands.command.WrappedCommand
import io.github.devrawr.commands.exception.ArgumentCountException
import io.github.devrawr.commands.exception.ArgumentException
import io.github.devrawr.commands.processor.executor.Executor

abstract class CommandProcessor
{
    abstract val platform: CommandPlatform

    open fun process(
        executor: Executor<*>,
        command: WrappedCommand,
        args: List<String>
    )
    {
        val user = executor.toUser()

        if (user == null)
        {
            executor.sendMessage(Locale.retrieveLocale(executor)["user-not-found"]!!)
            return
        }

        try
        {
            val arguments = command.handleArguments(
                executor = executor,
                args = args.toTypedArray()
            )

            command.method.invoke(arguments)
        } catch (ignored: ArgumentException)
        {
            executor.sendMessage("${Locale.retrieveLocale(executor)["error-prefix"]!!}${ignored.message!!}")
        }
    }
}