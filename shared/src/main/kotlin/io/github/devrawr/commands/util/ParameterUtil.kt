package io.github.devrawr.commands.util

import io.github.devrawr.commands.command.annotation.Value
import java.lang.reflect.AnnotatedElement
import java.lang.reflect.Method
import kotlin.reflect.KAnnotatedElement
import kotlin.reflect.full.findAnnotations
import kotlin.reflect.jvm.jvmErasure
import kotlin.reflect.jvm.kotlinFunction

object ParameterUtil
{
    inline fun <reified T : Annotation> AnnotatedElement.getAnnotation(): T?
    {
        return this.getAnnotation(T::class.java)
    }

    @OptIn(ExperimentalStdlibApi::class)
    inline fun <reified T : Annotation> KAnnotatedElement.getAnnotation(): T?
    {
        return this.findAnnotations(T::class).firstOrNull()
    }

    fun Method.getKotlinParameters(): List<WrappedParameter<out Any>>
    {
        val kotlin = this.kotlinFunction

        return kotlin?.parameters
            ?.subList(1, kotlin.parameters.size)
            ?.map {
                WrappedParameter(
                    type = it.type.jvmErasure.java,
                    name = it.name ?: "arg",
                    value = it.getAnnotation<Value>()?.value
                )
            }
            ?: this.parameters
                .map {
                    WrappedParameter(
                        type = it.type,
                        name = it.name,
                        value = it.getAnnotation<Value>()?.value
                    )
                }

    }
}

class WrappedParameter<T>(
    val type: Class<T>,
    val name: String,
    val value: String?
)