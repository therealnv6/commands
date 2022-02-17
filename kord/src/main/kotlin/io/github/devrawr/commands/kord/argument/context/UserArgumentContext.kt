package io.github.devrawr.commands.kord.argument.context

import dev.kord.core.entity.User
import io.github.devrawr.commands.command.argument.context.ArgumentContext
import io.github.devrawr.commands.kord.processor.executor.KordExecutor
import io.github.devrawr.commands.processor.executor.Executor
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking

object UserArgumentContext : ArgumentContext<User>
{
    override fun fromString(executor: Executor<*>?, value: String): User?
    {
        if (executor == null)
        {
            return null
        }

        if (value == "SELF")
        {
            return executor.toUser() as User
        }

        val message = (executor as KordExecutor).message

        return runBlocking {
            message.mentionedUsers.firstOrNull {
                it.mention == value.replaceFirst("!", "")
            }
        }
    }
}