package io.github.devrawr.commands.processor

import io.github.devrawr.commands.CommandPlatform
import io.github.devrawr.commands.Locale
import io.github.devrawr.commands.command.WrappedCommand
import io.github.devrawr.commands.exception.ArgumentException
import io.github.devrawr.commands.exception.ConditionFailedException
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

            if (wrappedCommand.help)
            {
                val topic = wrappedCommand.helpTopic

                println("still not sending... what")

                if (arguments.isNotEmpty())
                {
                    val page = arguments[0]

                    if (page is Int)
                    {
                        topic.pageMap[executor.id] = arguments[0] as Int
                    }
                }

                println("past page argument check")

                topic
                    .createHelpBody(executor)
                    .sendBodyToExecutor(executor)
            } else
            {
                wrappedCommand.method.invoke(arguments)
            }
        } catch (exception: Exception)
        {
            when (exception)
            {
                is ArgumentException,
                is ConditionFailedException ->
                {
                    executor.sendMessage("${Locale.retrieveLocale(executor)["error-prefix"]!!}${exception.message!!}")
                }
            }
        }
    }
}