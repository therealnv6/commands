package io.github.devrawr.commands.executor

abstract class CommandExecutor
{
    abstract fun sendMessage(message: String)
    abstract fun hasPermission(permission: String)
}