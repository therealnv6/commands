package io.github.devrawr.commands.processor.executor

abstract class Executor
{
    abstract fun sendMessage(message: String)
    abstract fun hasPermission(permission: String): Boolean
}