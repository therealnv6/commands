package io.github.devrawr.commands.kord.processor.executor

import dev.kord.common.entity.Permission
import dev.kord.core.behavior.channel.createEmbed
import dev.kord.core.behavior.channel.createMessage
import dev.kord.core.behavior.reply
import dev.kord.core.entity.Message
import dev.kord.core.entity.User
import dev.kord.rest.builder.message.EmbedBuilder
import dev.kord.rest.builder.message.create.UserMessageCreateBuilder
import io.github.devrawr.commands.Locale
import io.github.devrawr.commands.processor.executor.Executor
import kotlinx.coroutines.runBlocking
import java.util.*

class KordExecutor(
    val message: Message
) : Executor<User>()
{
    override val id: UUID = UUID.randomUUID()
    override val localeType = Locale.defaultLocale

    override fun sendMessage(message: String)
    {
        runBlocking {
            replyToMessage(message)
        }
    }

    suspend fun replyToMessage(message: String)
    {
        this.message.reply {
            this.content = message
        }
    }

    suspend fun sendKordMessage(message: UserMessageCreateBuilder.() -> Unit)
    {
        this.message.channel.createMessage(message)
    }

    suspend fun sendEmbed(message: EmbedBuilder.() -> Unit)
    {
        this.message.channel.createEmbed(message)
    }

    override fun hasPermission(permission: String): Boolean
    {
        return runBlocking {
            val guild = message.getGuild()
            val member = toUser().asMember(guild.id)

            val kordPermission = Permission.values.firstOrNull {
                it.code.value == permission
            } ?: return@runBlocking true

            return@runBlocking member.getPermissions().contains(kordPermission)
        }
    }

    override fun appliesToUser(type: Class<*>): Boolean
    {
        return type == User::class.java
    }

    override fun toUser(): User
    {
        return this.message.author!!
    }
}