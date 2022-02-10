package io.github.devrawr.commands.command.wrapper

import io.github.devrawr.commands.Platforms
import io.github.devrawr.commands.command.CommandWrapper
import io.github.devrawr.commands.command.WrappedCommand
import io.github.devrawr.commands.command.annotation.*
import io.github.devrawr.commands.command.argument.WrappedArgument
import io.github.devrawr.commands.command.argument.context.Contexts
import io.github.devrawr.commands.util.ParameterUtil.getAnnotation
import java.lang.reflect.Method

object AnnotationCommandWrapper : CommandWrapper()
{
    override val platform = Platforms.usedPlatform!!

    override fun wrapCommand(
        command: Any,
        instance: Any,
        parent: WrappedCommand?
    ): List<WrappedCommand>
    {
        if (command is Method)
        {
            val annotation = command.getAnnotation<Command>()
                ?: return emptyList()

            return listOf(
                WrappedCommand(
                    name = annotation.value
                        .split("|")
                        .toTypedArray(),
                    instance = instance,
                    method = command
                ).apply {
                    this.permission = command.getAnnotation<CommandPermission>()?.value ?: ""
                    this.description = command.getAnnotation<HelpDescription>()?.value ?: ""
                    this.help = command.getAnnotation<Help>() != null
                    this.arguments = wrapArguments(command).toMutableList()
                }
            )
        } else
        {
            val clazz = command.javaClass
            val annotation = clazz.getAnnotation<Command>()

            if (annotation == null)
            {
                return clazz.declaredMethods
                    .map {
                        try
                        {
                            wrapCommand(it, command, null)
                        } catch (ignored: IllegalArgumentException)
                        {
                            emptyList()
                        }
                    }
                    .flatten()
            } else
            {
                val method = clazz.declaredMethods
                    .first {
                        it.getAnnotation<Default>() != null
                    }

                return listOf(
                    WrappedCommand(
                        name = annotation.value
                            .split("|")
                            .toTypedArray(),
                        instance = command,
                        method = method
                    ).apply {
                        this.permission = clazz.getAnnotation<CommandPermission>()?.value ?: ""
                        this.description = clazz.getAnnotation<HelpDescription>()?.value ?: ""
                        this.help = clazz.getAnnotation<Help>() != null
                        this.arguments = wrapArguments(method).toMutableList()

                        for (declaredMethod in clazz.declaredMethods)
                        {
                            this.children.addAll(
                                wrapCommand(
                                    declaredMethod, command, this
                                )
                            )
                        }
                    }
                )
            }
        }
    }

    override fun wrapArguments(method: Method): List<WrappedArgument<*>>
    {
        return if (method.getAnnotation<Help>() == null)
        {
            return super.wrapArguments(method)
        } else
        {
            mutableListOf(
                WrappedArgument(
                    name = "page",
                    type = Int::class.java,
                    value = 0,
                    context = Contexts.retrieveContext()
                )
            )
        }
    }
}