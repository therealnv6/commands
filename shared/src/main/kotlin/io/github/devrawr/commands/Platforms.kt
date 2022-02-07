package io.github.devrawr.commands

import io.github.devrawr.commands.command.CommandWrapper
import io.github.devrawr.commands.command.WrappedCommand
import io.github.devrawr.commands.command.argument.WrappedArgument
import io.github.devrawr.commands.command.argument.context.ArgumentContext
import io.github.devrawr.commands.command.argument.context.Contexts
import io.github.devrawr.commands.util.ObjectInstanceUtil.getOrCreateInstance

object Platforms
{
    var usedPlatform: CommandPlatform? = null
    val wrappers = mutableListOf<CommandWrapper>()

    inline fun <reified T : CommandPlatform> usePlatform(noinline body: T.() -> Unit = {}) =
        usePlatform(T::class.java, body)

    fun <T : CommandPlatform> usePlatform(
        type: Class<T>,
        body: T.() -> Unit = {}
    ): Platforms
    {
        return this.apply {
            this.usedPlatform = type.kotlin
                .getOrCreateInstance<T>()
                .apply(body)
        }
    }

    inline fun <reified T : CommandWrapper> useWrapper() = useWrapper(T::class.java)

    fun <T : CommandWrapper> useWrapper(type: Class<T>): Platforms
    {
        return this.apply {
            this.wrappers.add(type.kotlin.getOrCreateInstance())
        }
    }

    fun registerCommand(
        command: Any
    ): Platforms
    {
        return this.apply {
            this.usedPlatform?.registerCommand(command)
        }
    }

    fun createCommand(
        name: String,
        permission: String = "",
        arguments: Array<Any>,
        body: (Array<Any?>) -> Unit
    ): Platforms
    {
        return this.apply {
            this.usedPlatform?.registerCommand(
                WrappedCommand(
                    name = name.split("|").toTypedArray(),
                    method = body
                ).apply {
                    this.permission = permission
                    this.arguments = arguments.map {
                        WrappedArgument(
                            name = "arg",
                            type = it.javaClass,
                            context = Contexts.contexts[it.javaClass] as ArgumentContext<Any>
                        )
                    }.toMutableList()
                }
            )
        }
    }

    fun wrapCommand(command: Any): List<WrappedCommand>
    {
        return this.wrappers
            .map { it.wrapCommand(command, command) }
            .flatten()
    }
}