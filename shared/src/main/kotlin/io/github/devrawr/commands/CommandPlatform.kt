package io.github.devrawr.commands

import io.github.devrawr.commands.command.WrappedCommand
import io.github.devrawr.commands.executor.CommandExecutorProcessor
import io.github.devrawr.commands.locale.Locale
import io.github.devrawr.commands.util.ObjectInstanceUtil.getOrCreateInstance

abstract class CommandPlatform
{
    abstract val executorProcessor: CommandExecutorProcessor
    abstract fun registerCommand(command: WrappedCommand)

    open fun loadLocale()
    {
        Commands.locales["en_US"]!!.putAll(
            mapOf(
                "user-not-found" to "User could not be parsed from provided executor.",
                "does-not-meet-arguments" to "Usage: /{label} {arguments}",
                "required-argument" to "<{name}>",
                "optional-argument" to "[{name}]",
                "vararg-argument" to "..."
            )
        )
    }

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