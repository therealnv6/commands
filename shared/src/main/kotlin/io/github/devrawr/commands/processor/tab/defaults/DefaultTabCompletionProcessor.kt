package io.github.devrawr.commands.processor.tab.defaults

import io.github.devrawr.commands.Platforms
import io.github.devrawr.commands.command.WrappedCommand
import io.github.devrawr.commands.processor.executor.Executor
import io.github.devrawr.commands.processor.tab.TabCompletionProcessor

object DefaultTabCompletionProcessor : TabCompletionProcessor()
{
    override fun modifyTabCompletion(
        executor: Executor<*>,
        initial: List<String>,
        query: String
    ): List<String>
    {
        val label = query.split(" ")[0]

        val platform = Platforms.usedPlatform!!
        val matchedQuery = platform.commands.filter {
            it.name.any { name ->
                name.startsWith(label)
            }
        }

        if (matchedQuery.isNotEmpty())
        {
            return modifyTabCompletion(
                executor,
                initial,
                matchedQuery,
                query
            )
        }

        val list = initial.toMutableList()

        for (command in platform.commands)
        {
            for (alias in command.name)
            {
                if (list.contains("/$alias"))
                {
                    list.remove("/$alias")
                }
            }

            list.addAll(
                modifyTabCompletion(
                    executor = executor,
                    initial = initial,
                    command = command,
                    query = query
                )
            )
        }

        println(initial)

        return initial
    }

    override fun modifyTabCompletion(
        executor: Executor<*>,
        initial: List<String>,
        command: List<WrappedCommand>,
        query: String
    ): List<String>
    {
        return command.map {
            modifyTabCompletion(executor, initial, it, query)
        }.flatten()
    }

    override fun modifyTabCompletion(
        executor: Executor<*>,
        initial: List<String>,
        command: WrappedCommand,
        query: String
    ): List<String>
    {
        val list = mutableListOf<String>()
        val childQuery = query.split(" ")[1]

        for (child in command.children)
        {
            if (executor.hasPermission(child.permission) && child.label.startsWith(childQuery))
            {
                list.add("/${command.label} ${child.label}")
            }
        }

        return list
    }
}