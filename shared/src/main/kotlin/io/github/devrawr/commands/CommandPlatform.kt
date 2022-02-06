package io.github.devrawr.commands

import io.github.devrawr.commands.command.WrappedCommand
import io.github.devrawr.commands.processor.CommandProcessor
import io.github.devrawr.commands.processor.executor.ExecutorProcessor
import io.github.devrawr.commands.util.ObjectInstanceUtil.getOrCreateInstance

abstract class CommandPlatform
{
    abstract val executorProcessor: ExecutorProcessor<*>
    abstract val commandProcessor: CommandProcessor

    abstract fun registerCommand(command: WrappedCommand)

    fun registerCommand(command: Any)
    {
        this.wrapCommand(command).forEach {
            this.registerCommand(it)
        }
    }

    inline fun <reified T> registerCommand()
    {
        this.registerCommand(
            T::class.getOrCreateInstance()
        )
    }

    fun wrapCommand(command: Any): List<WrappedCommand>
    {
        return Commands.wrapCommand(command)
    }
}