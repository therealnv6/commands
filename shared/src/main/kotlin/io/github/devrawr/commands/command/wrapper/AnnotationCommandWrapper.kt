package io.github.devrawr.commands.command.wrapper

import io.github.devrawr.commands.Platforms
import io.github.devrawr.commands.command.CommandWrapper
import io.github.devrawr.commands.command.WrappedCommand
import io.github.devrawr.commands.command.annotation.Command
import io.github.devrawr.commands.command.annotation.CommandPermission
import io.github.devrawr.commands.command.annotation.Default
import io.github.devrawr.commands.command.annotation.HelpDescription
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
                    parent = parent,
                    method = command
                ).apply {
                    this.permission = command.getAnnotation<CommandPermission>()?.value ?: ""
                    this.description = command.getAnnotation<HelpDescription>()?.value ?: ""
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
}