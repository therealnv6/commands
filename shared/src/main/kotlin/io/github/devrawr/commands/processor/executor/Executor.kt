package io.github.devrawr.commands.processor.executor

import java.util.*

abstract class Executor<T>
{
    abstract val id: UUID
    abstract val localeType: String

    abstract fun sendMessage(message: String)
    abstract fun hasPermission(permission: String): Boolean
    abstract fun appliesToUser(type: Class<*>): Boolean
    abstract fun toUser(): T?
}