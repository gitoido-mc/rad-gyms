package lol.gito.radgyms.common.api

import com.cobblemon.mod.common.Environment
import com.cobblemon.mod.common.ModAPI
import com.cobblemon.mod.common.NetworkManager
import com.mojang.brigadier.arguments.ArgumentType
import net.minecraft.commands.synchronization.ArgumentTypeInfo
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.MinecraftServer
import net.minecraft.server.packs.PackType
import net.minecraft.server.packs.resources.PreparableReloadListener
import kotlin.reflect.KClass

@Suppress("TooManyFunctions")
interface RadGymsImplementation {
    /**
     * Returns enum corresponding to mod loader being used.
     * For example fabric mod loader will return ModAPI.FABRIC
     */
    val modAPI: ModAPI

    val networkManager: NetworkManager

    fun environment(): Environment

    fun isModInstalled(id: String): Boolean

    fun registerDataComponents()

    fun registerItems()

    fun registerBlocks()

    fun registerEntityTypes()

    fun registerEntityAttributes()

    fun registerBlockEntityTypes()

    fun registerResourceReloader(
        identifier: ResourceLocation,
        reloader: PreparableReloadListener,
        type: PackType,
        dependencies: Collection<ResourceLocation>
    )

    fun <A : ArgumentType<*>, T : ArgumentTypeInfo.Template<A>> registerCommandArgument(
        identifier: ResourceLocation,
        argumentClass: KClass<A>,
        serializer: ArgumentTypeInfo<A, T>
    )

    fun server(): MinecraftServer?

    fun initialize()
}
