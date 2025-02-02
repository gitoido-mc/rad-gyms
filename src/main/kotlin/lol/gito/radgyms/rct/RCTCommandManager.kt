package lol.gito.radgyms.rct

import com.gitlab.srcmc.rctapi.commands.CommandsContext
import com.gitlab.srcmc.rctapi.commands.RCTApiCommands
import lol.gito.radgyms.RadGyms

object RCTCommandManager: CommandsContext() {
    override fun getPrefix(): String = RadGyms.MOD_ID
    override fun getWinCommandsPermission(): Int = 2

    fun register() {
        RCTApiCommands.register(this)
    }
}