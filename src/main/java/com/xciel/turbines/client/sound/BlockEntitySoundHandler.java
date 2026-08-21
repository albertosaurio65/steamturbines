package com.xciel.turbines.client.sound;

import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Map;
import java.util.WeakHashMap;

public class BlockEntitySoundHandler {

    private static final Map<BlockEntity, BlockLoopingSoundInstance> SOUNDS = new WeakHashMap<>();

    private BlockEntitySoundHandler() {}

    public static void playLooping(BlockEntity blockEntity, SoundEvent soundEvent) {
        BlockLoopingSoundInstance instance = SOUNDS.get(blockEntity);
        if (instance == null || instance.isStopped()) {
            instance = new BlockLoopingSoundInstance(soundEvent, blockEntity.getBlockPos());
            SOUNDS.put(blockEntity, instance);
            Minecraft.getInstance().getSoundManager().play(instance);
        }
        instance.keepAlive();
    }

    public static void setVolume(BlockEntity blockEntity, float volume) {
        BlockLoopingSoundInstance instance = SOUNDS.get(blockEntity);
        if (instance != null) {
            instance.setVolume(volume);
        }
    }

    public static void stop(BlockEntity blockEntity) {
        BlockLoopingSoundInstance instance = SOUNDS.remove(blockEntity);
        if (instance != null) {
            instance.stopSound();
        }
    }
}
