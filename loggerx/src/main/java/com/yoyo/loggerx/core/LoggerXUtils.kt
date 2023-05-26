package com.yoyo.loggerx.core

import com.rommansabbir.commander.Command
import com.rommansabbir.commander.CommanderManager
import com.yoyo.loggerx.ext.gson

object LoggerXUtils {
    const val SUBSCRIPTION_KEY: String = "LOGGER_X_SUBSCRIPTION_KEY"
    const val LOG_COMMAND: String = "LOGGER_X_LOG_COMMAND"

    fun sendUpdate(update: Any) {
        CommanderManager.getInstance()
            .broadcastCommand(Command(LOG_COMMAND, gson.toJson(update), SUBSCRIPTION_KEY))
    }
}