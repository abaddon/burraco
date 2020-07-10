package com.abaddon83.utils.cqs

import com.abaddon83.utils.cqs.commands.Command
import com.abaddon83.utils.cqs.commands.CommandHandler
import com.abaddon83.utils.cqs.queries.Query
import com.abaddon83.utils.cqs.queries.QueryHandler
import org.reflections.Reflections
import java.lang.reflect.ParameterizedType

class ContextImpl(packageNames: List<String>): Context {

    private val commandMap = HashMap<Class<out Command>, CommandHandler<*>>()
    private val queryMap = HashMap<Class<out Query<*>>, QueryHandler<*, *>>()

    init {
        packageNames.forEach {
            val reflections = Reflections(it)
            reflections.getSubTypesOf(QueryHandler::class.java).forEach {
                (it.genericInterfaces).forEach { genericInterface ->
                    if ((genericInterface is ParameterizedType) && genericInterface.rawType as Class<*> == QueryHandler::class.java) {
                        val queryClazz = genericInterface.actualTypeArguments[0]

                        queryMap[queryClazz as Class<out Query<*>>] =
                                (it as Class<out QueryHandler<*, *>>).getDeclaredConstructor().newInstance() as QueryHandler<*, *>
                    }
                }
            }

            reflections.getSubTypesOf(CommandHandler::class.java).forEach {
                (it.genericInterfaces).forEach { genericInterface ->
                    if ((genericInterface is ParameterizedType) && genericInterface.rawType as Class<*> == CommandHandler::class.java) {
                        val commandClazz = genericInterface.actualTypeArguments[0]

                        commandMap[commandClazz as Class<out Command>] =
                                (it as Class<out CommandHandler<*>>).newInstance() as CommandHandler<*>
                    }
                }
            }

        }

    }

    override fun <TCommand : Command> resolveCommandHandler(classOfCommand: Class<TCommand>): CommandHandler<TCommand> {
        val handler = commandMap[classOfCommand]
                ?: throw HandlerNotFoundException("handler not found for ${classOfCommand.name}")
        return handler as CommandHandler<TCommand>
    }

    override fun <TQuery : Query<TResult>, TResult> resolveQueryHandler(classOfQuery: Class<TQuery>): QueryHandler<TQuery, TResult> {
        val handler = queryMap[classOfQuery]
                ?: throw HandlerNotFoundException("handler not found for ${classOfQuery.name}")
        return handler as QueryHandler<TQuery, TResult>
    }
}