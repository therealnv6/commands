package io.github.devrawr.commands.executor

abstract class CommandExecutorProcessor
{
    abstract fun toUser(executor: CommandExecutor): Any?
}