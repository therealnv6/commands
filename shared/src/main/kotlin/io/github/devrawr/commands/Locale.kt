package io.github.devrawr.commands

import io.github.devrawr.commands.processor.executor.Executor

object Locale
{
    const val defaultLocale = "en_US"
    val locales = hashMapOf(
        "en_US" to hashMapOf(
            "user-not-found" to "User could not be parsed from provided executor.",
            "unable-to-parse-executor" to "Executor could not be parsed from provided user.",
            "does-not-meet-arguments" to "Usage: /{label} {arguments}",
            "unable-to-parse-argument" to "Unable to parse argument from {arg}",
            "required-argument" to "<{name}>",
            "optional-argument" to "[{name}]",
            "error-prefix" to "Error: ",
            "vararg-argument" to "..."
        ),
    )

    fun retrieveLocale(
        executor: Executor<*>? = null
    ): HashMap<String, String>
    {
        return locales[defaultLocale]!!
    }

    fun retrieveLocaleField(
        executor: Executor<*>? = null,
        field: String
    ): String
    {
        return this.retrieveLocale(executor)[field]!!
    }
}