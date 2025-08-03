/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.common.network

//@Suppress("EmptyMethod")
//object NetworkStackHandler {
//    val PACKET_LEAVE = modId("net.gym_leave")
//    val PACKET_OPEN_CACHE = modId("net.cache_open")
//    val PACKET_CACHE_POKEMONS_SYNC = modId("net.cache_poke_sync")
//
//    fun register() {
//        debug("Registering network stack handler")
//        CHANNEL.registerServerbound(GymEnterWithCoords::class.java, ::handleGymEnterBlockPacket)
//        CHANNEL.registerServerbound(GymEnterWithoutCoords::class.java, ::handleGymEnterKeyPacket)
//        CHANNEL.registerServerbound(GymLeave::class.java, ::handleGymLeavePacket)
//        CHANNEL.registerServerbound(CacheOpen::class.java, ::handleCacheOpenPacket)
//        CHANNEL.registerServerbound(
//            CachePokeSync::class,
//            EndecRegistry::CACHE_POKE_SYNC_ENDEC,
//            ::handleCachePokeSyncPacket
//        )
//        CHANNEL.registerClientboundDeferred(CacheOpen::class.java)
//        CHANNEL.registerClientboundDeferred(GymLeave::class.java)
//    }
//
//    private fun handleGymEnterBlockPacket(packet: GymEnterWithCoords, context: ServerAccess) {
//        val serverPlayer = context.player() as ServerPlayerEntity
//        val serverWorld = serverPlayer.world as ServerWorld
//
//        val blockPos = BlockPos.fromLong(packet.blockPos)
//        if (serverWorld.getBlockEntity(blockPos) is GymEntranceEntity) {
//            val gymEntrance = serverPlayer.world.getBlockEntity(blockPos) as GymEntranceEntity
//            gymEntrance.incrementPlayerUseCount(serverPlayer)
//            debug("Gym Entrance at $blockPos - increased usage for player ${serverPlayer.name.string}")
//        }
//        serverWorld.chunkManager.markForUpdate(blockPos)
//        executeEnter(serverPlayer, serverWorld, packet.level, packet.type, false)
//    }
//
//    private fun handleGymEnterKeyPacket(packet: GymEnterWithoutCoords, context: ServerAccess) {
//        val serverPlayer = context.player() as ServerPlayerEntity
//        val serverWorld = serverPlayer.world as ServerWorld
//
//        executeEnter(serverPlayer, serverWorld, packet.level, packet.type, true)
//    }
//
//    private fun executeEnter(
//        serverPlayer: ServerPlayerEntity,
//        serverWorld: ServerWorld,
//        level: Int,
//        type: String,
//        key: Boolean
//    ) {
//        debug("Handling GymEnter packet for player ${serverPlayer.name}")
//        serverWorld.server.execute {
//            GymEnterPacketHandler(
//                serverPlayer,
//                serverWorld,
//                level,
//                key,
//                type
//            )
//        }
//    }
//
//    private fun handleGymLeavePacket(packet: GymLeave, context: ServerAccess) {
//        val serverPlayer = context.player()
//        debug("Handling GymLeave ${packet.id} packet for player ${serverPlayer.name}")
//
//        context.player.server.execute { GymLeavePacketHandler(context.player) }
//    }
//
//    private fun handleCacheOpenPacket(packet: CacheOpen, context: ServerAccess) {
//        debug("Handling CacheOpen packet for player ${context.player.name}")
//        context.player.server.execute {
//            CacheOpenPacketHandler(
//                context.player,
//                ElementalTypes.get(packet.type)!!,
//                packet.rarity,
//                packet.shinyBoost,
//            )
//        }
//    }
//
//    @Suppress("unused_parameter")
//    private fun handleCachePokeSyncPacket(packet: CachePokeSync, context: ServerAccess) {
//        ifClient {
//            SpeciesManager.SPECIES_BY_RARITY = packet.data
//        }
//    }
//
//    @Suppress("unused_parameter")
//    fun handleGymServerLeavePacket(packet: GymLeave, context: ClientAccess) {
//        CHANNEL.clientHandle().send(packet)
//    }
//
//    @Suppress("unused_parameter")
//    fun handleCacheServerOpenPacket(packet: CacheOpen, context: ClientAccess) {
//        CHANNEL.clientHandle().send(packet)
//    }
//}
