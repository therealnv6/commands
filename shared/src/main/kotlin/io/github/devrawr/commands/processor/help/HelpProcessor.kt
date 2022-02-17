package io.github.devrawr.commands.processor.help

import io.github.devrawr.commands.processor.executor.Executor

interface HelpProcessor
{
    fun createBody(
        executor: Executor<*>,
        data: HelpTopicData
    ): HelpBody
}