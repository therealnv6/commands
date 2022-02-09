package io.github.devrawr.commands.processor.help

interface HelpProcessor
{
    fun createBody(
        message: List<String>
    ): HelpBody
}