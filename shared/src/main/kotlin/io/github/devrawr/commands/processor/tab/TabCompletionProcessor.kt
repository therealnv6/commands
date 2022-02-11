package io.github.devrawr.commands.processor.tab

import io.github.devrawr.commands.command.WrappedCommand
import io.github.devrawr.commands.processor.executor.Executor

abstract class TabCompletionProcessor
{
    abstract fun modifyTabCompletion(
        executor: Executor<*>,
        initial: List<String>,
        query: String
    ): List<String>

    abstract fun modifyTabCompletion(
        executor: Executor<*>,
        initial: List<String>,
        command: List<WrappedCommand>,
        query: String
    ): List<String>

    abstract fun modifyTabCompletion(
        executor: Executor<*>,
        initial: List<String>,
        command: WrappedCommand,
        query: String
    ): List<String>
}