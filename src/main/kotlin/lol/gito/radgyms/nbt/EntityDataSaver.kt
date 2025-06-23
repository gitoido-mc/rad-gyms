package lol.gito.radgyms.nbt

import net.minecraft.nbt.NbtCompound

interface EntityDataSaver {
    fun getGymsPersistentData(): NbtCompound
}
