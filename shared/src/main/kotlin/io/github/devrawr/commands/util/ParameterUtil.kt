package io.github.devrawr.commands.util

import java.lang.reflect.AnnotatedElement

object ParameterUtil
{
    inline fun <reified T : Annotation> AnnotatedElement.getAnnotation(): T?
    {
        return this.getAnnotation(T::class.java)
    }
}