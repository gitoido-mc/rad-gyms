/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.threading

import lol.gito.radgyms.common.RadGyms
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

object ThreadManager {
    private val exceptionHandler: Thread.UncaughtExceptionHandler = { thread, throwable ->
        RadGyms.LOGGER.error("Exception in ${thread.name} thread", throwable)
    }

    val teleportExecutor: ExecutorService = Executors.newCachedThreadPool { runnable ->
        val thread = Thread(runnable, "Rad Gyms :: Teleports")
        thread.uncaughtExceptionHandler = exceptionHandler
        return@newCachedThreadPool thread
    }

    @Suppress("unused")
    val structurePlacer: ExecutorService = Executors.newCachedThreadPool { runnable ->
        val thread = Thread(runnable, "Rad Gyms :: Structure placement")
        thread.uncaughtExceptionHandler = exceptionHandler
        return@newCachedThreadPool thread
    }
}
