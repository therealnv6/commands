package io.github.devrawr.commands.command.wrapper

import io.github.devrawr.commands.command.CommandWrapper
import io.github.devrawr.commands.command.WrappedCommand
import io.github.devrawr.commands.command.annotation.Command
import io.github.devrawr.commands.command.annotation.CommandPermission
import io.github.devrawr.commands.command.annotation.Default
import io.github.devrawr.commands.util.ParameterUtil.getAnnotation
import java.lang.reflect.Method

object AnnotationCommandWrapper : CommandWrapper()
{
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
                    name = annotation.value,
                    instance = instance,
                    parent = parent
                ).apply {
                    this.permission = method.getAnnotation<CommandPermission>()?.value ?: ""
                    this.arguments = wrapArguments(method).toMutableList()
                    this.method = command
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
                    WrappedCommand(annotation.value, command).apply {
                        this.permission = clazz.getAnnotation<CommandPermission>()?.value ?: ""
                        this.arguments = wrapArguments(method).toMutableList()
                        this.method = method

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