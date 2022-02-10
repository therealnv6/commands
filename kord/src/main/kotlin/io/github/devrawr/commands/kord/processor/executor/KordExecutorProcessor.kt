package io.github.devrawr.commands.kord.processor.executor

import dev.kord.core.entity.User
import io.github.devrawr.commands.processor.executor.ExecutorProcessor

object KordExecutorProcessor : ExecutorProcessor<KordExecutor>()
{
    val executorMap = hashMapOf<User, KordExecutor>()

    override fun toUser(executor: KordExecutor): Any?
    {
        return executor.message.author
    }

    override fun fromUser(user: Any): KordExecutor?
    {
        return this.executorMap[user as User]
    }

    override fun isUser(type: Class<*>): Boolean
    {
        return type == User::class.java
    }
}