package io.github.devrawr.commands.processor.executor

abstract class Executor<T>
{
    abstract fun sendMessage(message: String)
    abstract fun hasPermission(permission: String): Boolean
    abstract fun appliesToUser(type: Class<*>): Boolean
    abstract fun toUser(): T?
}