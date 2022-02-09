package io.github.devrawr.commands.processor.help

import io.github.devrawr.commands.command.WrappedCommand
import io.github.devrawr.commands.processor.executor.Executor

interface HelpProcessor<T>
{
    fun generateHelp(
        page: Int,
        issuer: Executor<*>,
        command: WrappedCommand
    ): T

    fun createBody(
        message: List<String>
    ): HelpBody
}