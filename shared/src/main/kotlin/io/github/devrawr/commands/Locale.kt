package io.github.devrawr.commands

import io.github.devrawr.commands.command.argument.context.Contexts
import io.github.devrawr.commands.processor.executor.Executor
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*
import kotlin.collections.HashMap

object Locale
{
    var defaultLocale = "en_US"
    var localeDirectory = File("locales/")

    var loaded = false

    val locales = hashMapOf(
        "en_US" to hashMapOf(
            LocaleKeys.USER_NOT_FOUND to "User could not be parsed from provided executor.",
            LocaleKeys.UNABLE_TO_PARSE_EXECUTOR to "Executor could not be parsed from provided user.",
            LocaleKeys.DOES_NOT_MEET_ARGUMENTS to "Incorrect usage, try: /{label} {arguments}",
            LocaleKeys.UNABLE_TO_PARSE_ARGUMENT to "Unable to parse argument from \"{arg}\"",
            LocaleKeys.REQUIRED_ARGUMENT to "<{name}>",
            LocaleKeys.OPTIONAL_ARGUMENT to "[{name}]",
            LocaleKeys.ERROR_PREFIX to "Error: ",
            LocaleKeys.VARARG_ARGUMENT to "...",
            LocaleKeys.HELP_ENTRY_PER_PAGE to "6",
            LocaleKeys.INSUFFICIENT_PERMISSIONS to "You have insufficient permissions to execute this.",

            LocaleKeys.HELP_TITLE to "=== Showing help for /{parent} ===",
            LocaleKeys.HELP_ENTRY to "/{label} {args} - {description}",
            LocaleKeys.HELP_FOOTER to "Showing page {page-current} of {page-max} ({results} results)"
        )
    )
        get()
        {
            if (!loaded)
            {
                this.loadLocales()
            }

            return field
        }

    private fun loadLocales()
    {
        this.loaded = true

        if (!this.localeDirectory.exists())
        {
            this.localeDirectory.mkdirs()

            val file = File(this.localeDirectory, defaultLocale)

            val properties = Properties().apply {
                this.putAll(locales[defaultLocale]!!)
            }

            properties.store(FileOutputStream(file), null)
        }

        val files = localeDirectory.listFiles()

        if (files != null)
        {
            for (file in files)
            {
                val map = hashMapOf<String, String>()
                val properties = Properties()

                properties.load(FileInputStream(file))

                for (entry in properties.entries)
                {
                    map[entry.key.toString()] = entry.value.toString()
                }

                this.locales[file.name] = map
            }
        }
    }

    fun retrieveLocale(
        executor: Executor<*>? = null
    ): HashMap<String, String>
    {
        return locales[executor?.localeType ?: defaultLocale]!!
    }

    inline fun <reified T : Any> retrieveLocaleField(
        field: String,
        executor: Executor<*>? = null
    ): T
    {
        return Contexts.retrieveContext<T>().fromString(
            retrieveLocale(executor)[field]!!
        )!!
    }
}

object LocaleKeys
{
    const val USER_NOT_FOUND = "user-not-found"
    const val INSUFFICIENT_PERMISSIONS = "insufficient-permissions"
    const val UNABLE_TO_PARSE_EXECUTOR = "unable-to-parse-executor"
    const val DOES_NOT_MEET_ARGUMENTS = "does-not-meet-arguments"
    const val UNABLE_TO_PARSE_ARGUMENT = "unable-to-parse-argument"
    const val REQUIRED_ARGUMENT = "required-argument"
    const val OPTIONAL_ARGUMENT = "optional-argument"
    const val ERROR_PREFIX = "error-prefix"
    const val VARARG_ARGUMENT = "vararg-argument"
    const val HELP_ENTRY_PER_PAGE = "help-entry-per-page"
    const val HELP_TITLE = "help-title"
    const val HELP_ENTRY = "help-entry"
    const val HELP_FOOTER = "help-footer"
}