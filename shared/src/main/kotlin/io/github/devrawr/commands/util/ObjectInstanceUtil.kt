package io.github.devrawr.commands.util

import kotlin.reflect.KClass

object ObjectInstanceUtil
{
    fun <T : Any> KClass<*>.getOrCreateInstance(): T
    {
        return (this.objectInstance ?: this.java.newInstance()) as T
    }
}