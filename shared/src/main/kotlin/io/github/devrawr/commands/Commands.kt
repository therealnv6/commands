package io.github.devrawr.commands

import io.github.devrawr.commands.command.CommandWrapper
import io.github.devrawr.commands.command.WrappedCommand
import io.github.devrawr.commands.context.CommandContext
import io.github.devrawr.commands.context.defaults.StringCommandContext
import io.github.devrawr.commands.util.ObjectInstanceUtil.getOrCreateInstance

object Commands
{
    val platforms = mutableListOf<CommandPlatform>()
    val wrappers = mutableListOf<CommandWrapper>()
    val contexts = mutableMapOf<Class<*>, CommandContext<*>>()

    val DEFAULT_CONTEXT: CommandContext<*> = StringCommandContext

    inline fun <reified T : CommandPlatform> usePlatform(
        noinline body: T.() -> Unit = {}
    ): Commands
    {
        return this.usePlatform(T::class.java, body)
    }

    fun <T : CommandPlatform> usePlatform(
        type: Class<T>,
        body: T.() -> Unit = {}
    ): Commands
    {
        return this.apply {
            val instance = type.kotlin
                .getOrCreateInstance<T>()
                .apply(body)

            this.platforms.add(instance)
        }
    }

    inline fun <reified T : CommandWrapper> useWrapper(): Commands
    {
        return this.useWrapper(T::class.java)
    }

    fun <T : CommandWrapper> useWrapper(type: Class<T>): Commands
    {
        return this.apply {
            this.wrappers.add(type.kotlin.getOrCreateInstance())
        }
    }

    inline fun <reified K, reified V : CommandContext<K>> useContext(): Commands
    {
        return this.useContext(K::class.java, V::class.java)
    }

    fun <K, V : CommandContext<K>> useContext(
        keyType: Class<K>,
        valueType: Class<V>
    ): Commands
    {
        return this.apply {
            this.contexts[keyType] = valueType.kotlin.getOrCreateInstance()
        }
    }

    inline fun <reified T : CommandPlatform> registerCommand(
        command: Any
    ): Commands
    {
        return this.apply {
            platforms.firstOrNull {
                it.javaClass == T::class.java
            }?.registerCommand(command)
        }
    }

    @JvmName("registerCommandGlobal")
    fun registerCommand(command: Any): Commands
    {
        return this.apply {
            this.platforms.forEach {
                it.registerCommand(it)
            }
        }
    }

    fun wrapCommand(command: Any): List<WrappedCommand>
    {
        return this.wrappers
            .map { it.wrapCommand(command) }
            .flatten()
    }
}