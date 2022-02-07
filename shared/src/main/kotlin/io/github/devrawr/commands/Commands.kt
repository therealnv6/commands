package io.github.devrawr.commands

import io.github.devrawr.commands.command.CommandWrapper
import io.github.devrawr.commands.command.WrappedArgument
import io.github.devrawr.commands.command.WrappedCommand
import io.github.devrawr.commands.context.CommandContext
import io.github.devrawr.commands.context.defaults.*
import io.github.devrawr.commands.util.ObjectInstanceUtil.getOrCreateInstance

object Commands
{
    val platforms = mutableListOf<CommandPlatform>()
    val wrappers = mutableListOf<CommandWrapper>()

    val contexts = mutableMapOf<Class<*>, CommandContext<*>>(
        Int::class.java to IntegerCommandContext,
        Long::class.java to LongCommandContext,
        Double::class.java to DoubleCommandContext,
        Float::class.java to FloatCommandContext,
        String::class.java to StringCommandContext
    )

    val DEFAULT_CONTEXT: CommandContext<*> = StringCommandContext

    inline fun <reified T : CommandPlatform> usePlatform(noinline body: T.() -> Unit = {}) =
        usePlatform(T::class.java, body)

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

    inline fun <reified T : CommandWrapper> useWrapper() = useWrapper(T::class.java)

    fun <T : CommandWrapper> useWrapper(type: Class<T>): Commands
    {
        return this.apply {
            this.wrappers.add(type.kotlin.getOrCreateInstance())
        }
    }

    inline fun <reified K, reified V : CommandContext<K>> useContext() = useContext(K::class.java, V::class.java)

    fun <K, V : CommandContext<K>> useContext(
        keyType: Class<K>,
        valueType: Class<V>
    ): Commands
    {
        return this.apply {
            this.contexts[keyType] = valueType.kotlin.getOrCreateInstance()
        }
    }

    inline fun <reified T> createContext(noinline body: (String) -> T) = createContext(T::class.java, body)

    fun <T> createContext(
        type: Class<T>,
        body: (String) -> T
    ): Commands
    {
        return this.apply {
            this.contexts[type] = object : CommandContext<T>
            {
                override fun fromString(value: String): T?
                {
                    return body.invoke(value)
                }
            }
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

    inline fun <reified T : CommandPlatform> createCommand(
        name: String,
        permission: String = "",
        arguments: Array<T>,
        noinline body: (Array<Any?>) -> Unit
    ): Commands
    {
        return this.apply {
            this.platforms.firstOrNull {
                it.javaClass == T::class.java
            }?.registerCommand(
                WrappedCommand(
                    name = name.split("|").toTypedArray(),
                    method = body
                ).apply {
                    this.permission = permission
                    this.arguments = arguments.map {
                        WrappedArgument(
                            name = "arg",
                            type = it.javaClass,
                            context = contexts[it.javaClass] as CommandContext<Any>
                        )
                    }.toMutableList()
                }
            )
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
            .map { it.wrapCommand(command, command) }
            .flatten()
    }
}