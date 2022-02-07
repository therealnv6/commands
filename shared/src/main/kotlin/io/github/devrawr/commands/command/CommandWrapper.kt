package io.github.devrawr.commands.command

import io.github.devrawr.commands.CommandPlatform
import io.github.devrawr.commands.Commands
import io.github.devrawr.commands.command.annotation.Value
import io.github.devrawr.commands.util.ParameterUtil.getAnnotation
import java.lang.reflect.Method

abstract class CommandWrapper
{
    abstract val platform: CommandPlatform

    abstract fun wrapCommand(
        command: Any,
        instance: Any,
        parent: WrappedCommand? = null
    ): List<WrappedCommand>

    open fun wrapArguments(
        method: Method,
    ): List<WrappedArgument<*>>
    {
        return method.parameters
            .map {
                val value = it.getAnnotation<Value>()?.value
                val context = Commands.contexts[it.type] ?: Commands.DEFAULT_CONTEXT


                val wrappedValue = if (value != null)
                {
                    context.fromString(value)
                } else
                {
                    null
                }

                WrappedArgument(
                    name = it.name,
                    type = it.type,
                    context = context,
                    value = wrappedValue
                )
            }
    }
}