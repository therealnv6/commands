package io.github.devrawr.commands.processor

import io.github.devrawr.commands.CommandPlatform
import io.github.devrawr.commands.Locale
import io.github.devrawr.commands.command.WrappedCommand
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

        val wrappedCommand = command.children.firstOrNull {
            args.isNotEmpty() && it.name.contains(args[0])
        } ?: command

        if (wrappedCommand != command)
        {
            process(
                executor, wrappedCommand, args.subList(1, args.size)
            )
            return
        }

        try
        {
            val arguments = wrappedCommand.handleArguments(
                executor = executor,
                args = args.toTypedArray()
            )

            wrappedCommand.method.invoke(arguments)
        } catch (ignored: ArgumentException)
        {
            executor.sendMessage("${Locale.retrieveLocale(executor)["error-prefix"]!!}${ignored.message!!}")
        }
    }
}