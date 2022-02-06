package io.github.devrawr.commands.processor

import io.github.devrawr.commands.CommandPlatform
import io.github.devrawr.commands.Commands
import io.github.devrawr.commands.command.WrappedCommand
import io.github.devrawr.commands.executor.CommandExecutor

abstract class CommandProcessor
{
    abstract val platform: CommandPlatform

    open fun process(
        executor: CommandExecutor,
        command: WrappedCommand,
        arguments: List<String>
    )
    {
        val user = platform.executorProcessor.toUser(executor)

        if (user == null)
        {
            executor.sendMessage(Commands.currentLocale["user-not-found"]!!)
            return
        }

        val data = hashMapOf<Int, Any?>()

        for ((index, argument) in command.arguments.withIndex())
        {
            if (data[index] != null)
            {
                return
            }

            if (arguments.size - 1 < index && argument.value == null)
            {
                executor.sendMessage(
                    Commands.currentLocale["does-not-meet-arguments"]!!
                        .replace("{label}", command.label)
                        .replace("{arguments}", command.formatArguments())
                )
                return
            }

            data[index] = if (arguments.size - 1 < index)
            {
                argument.value
            } else if (index == command.arguments.size - 1 && arguments.size > index && argument.type == Array<String>::class.java)
            {
                arguments.subList(index, arguments.size - 1).toTypedArray()
            } else
            {
                argument.convertToValue(arguments[index])
            }
        }

        command.method.invoke(command.instance, *data.values.toTypedArray())
    }
}