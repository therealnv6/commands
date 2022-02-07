package io.github.devrawr.commands.processor.executor

abstract class ExecutorProcessor<T : Executor<*>>
{
    fun toUserCasted(executor: Executor<*>): Any?
    {
        return this.toUser(executor as T)
    }

    abstract fun toUser(executor: T): Any?
    abstract fun fromUser(user: Any): T?
    abstract fun isUser(type: Class<*>): Boolean
}