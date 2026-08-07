package com.xciel.turbines.content.green_nentia_block;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

import java.util.List;

public class GreenNentiaBlockEntity extends SmartBlockEntity {

    private static final int MAX_STEAM_HEIGHT = 10;
    private static final int SCAN_INTERVAL = 20;

    private int waterFaceCount;
    private int steamHeight;
    private int scanTimer;

    public GreenNentiaBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        setLazyTickRate(5);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
    }

    @Override
    public void tick() {
        super.tick();
        if (level == null) return;

        if (level.isClientSide) {
            spawnSteamParticles();
            return;
        }

        if (scanTimer-- <= 0) {
            scanTimer = SCAN_INTERVAL;
            scanWaterFaces();
        }
    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        if (level == null || level.isClientSide) return;
        scanWaterFaces();
    }

    public void onNeighborChanged() {
        if (level == null || level.isClientSide) return;
        scanWaterFaces();
    }

    private void scanWaterFaces() {
        int count = 0;
        for (Direction dir : Direction.values()) {
            BlockPos neighborPos = worldPosition.relative(dir);
            if (!level.isLoaded(neighborPos)) continue;
            FluidState fluid = level.getFluidState(neighborPos);
            if (fluid.isSource() && fluid.getType() == Fluids.WATER)
                count++;
        }

        if (count != waterFaceCount) {
            waterFaceCount = count;
            steamHeight = Math.min(count, MAX_STEAM_HEIGHT);
            setChanged();
            sendData();
        }
    }

    public int getSteamHeight() {
        return steamHeight;
    }

    public int getWaterFaceCount() {
        return waterFaceCount;
    }

    private void spawnSteamParticles() {
        if (level == null || steamHeight <= 0) return;

        int waterSurface = 0;
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for (int y = 1; y <= MAX_STEAM_HEIGHT + 1; y++) {
            pos.set(worldPosition.getX(), worldPosition.getY() + y, worldPosition.getZ());
            FluidState fluid = level.getFluidState(pos);
            if (fluid.getType() == Fluids.WATER) {
                waterSurface = y;
            } else {
                break;
            }
        }

        if (waterSurface > 0) {
            waterSurface++;
            double baseY = worldPosition.getY() + waterSurface;
            int particlesPerTick = Math.min(3 + waterFaceCount, 8);

            for (int i = 0; i < particlesPerTick; i++) {
                double x = worldPosition.getX() + 0.5 + (level.random.nextDouble() - 0.5) * 1.4;
                double z = worldPosition.getZ() + 0.5 + (level.random.nextDouble() - 0.5) * 1.4;
                double y = baseY + level.random.nextDouble() * steamHeight;

                level.addParticle(ParticleTypes.BUBBLE_COLUMN_UP,
                    x, y, z, 0, 0.3 + level.random.nextDouble() * 0.4, 0);

                if (level.random.nextInt(3) != 0) {
                    level.addParticle(ParticleTypes.CLOUD,
                        x + (level.random.nextDouble() - 0.5) * 0.5,
                        y + 0.3 + level.random.nextDouble() * 0.5,
                        z + (level.random.nextDouble() - 0.5) * 0.5,
                        (level.random.nextDouble() - 0.5) * 0.02,
                        0.08 + level.random.nextDouble() * 0.06,
                        (level.random.nextDouble() - 0.5) * 0.02);
                }
            }
        }
    }

    @Override
    protected void read(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(tag, registries, clientPacket);
        waterFaceCount = tag.getInt("WaterFaceCount");
        steamHeight = tag.getInt("SteamHeight");
    }

    @Override
    protected void write(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(tag, registries, clientPacket);
        tag.putInt("WaterFaceCount", waterFaceCount);
        tag.putInt("SteamHeight", steamHeight);
    }
}
