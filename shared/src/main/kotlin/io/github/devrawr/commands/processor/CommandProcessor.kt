package io.github.devrawr.commands.processor

import io.github.devrawr.commands.command.WrappedCommand
import io.github.devrawr.commands.executor.CommandExecutor

abstract class CommandProcessor
{
    open fun process(
        executor: CommandExecutor,
        command: WrappedCommand,
        arguments: List<String>
    )
    {

    }
}