package io.github.devrawr.commands.processor.help

import io.github.devrawr.commands.processor.executor.Executor

abstract class HelpBody
{
    abstract fun sendBodyToExecutor(executor: Executor<*>)
}