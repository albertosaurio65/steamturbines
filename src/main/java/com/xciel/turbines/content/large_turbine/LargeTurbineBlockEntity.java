package com.xciel.turbines.content.large_turbine;

import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.xciel.turbines.content.green_nentia_block.GreenNentiaBlockEntity;
import com.xciel.turbines.steam.SteamConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;

import java.util.List;

public class LargeTurbineBlockEntity extends GeneratingKineticBlockEntity {

    private int foundSteamHeight;
    private int foundWaterFaceCount;
    private int distanceToNentia;

    public LargeTurbineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        setLazyTickRate(10);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
    }

    @Override
    public void tick() {
        super.tick();
        if (level == null || level.isClientSide) return;
    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        if (level == null || level.isClientSide) return;
        scanDownForNentia();
    }

    public void onNeighborChanged() {
        if (level == null || level.isClientSide) return;
        scanDownForNentia();
    }

    private void scanDownForNentia() {
        if (level == null) return;

        int maxScan = SteamConstants.AIR_TURBINE_MAX_SCAN_DISTANCE;
        BlockPos.MutableBlockPos cursor = worldPosition.mutable().move(0, -1, 0);
        int steps = 0;

        while (steps < maxScan) {
            if (!level.isLoaded(cursor)) return;

            BlockState state = level.getBlockState(cursor);
            if (state.isAir() || state.getFluidState().getType() == Fluids.WATER || state.getFluidState().getType() == Fluids.FLOWING_WATER) {
                cursor.move(0, -1, 0);
                steps++;
                continue;
            }

            var be = level.getBlockEntity(cursor);
            if (be instanceof GreenNentiaBlockEntity nentia) {
                int steam = nentia.getSteamHeight();
                int faces = nentia.getWaterFaceCount();
                if (steam != foundSteamHeight || faces != foundWaterFaceCount || steps != distanceToNentia) {
                    foundSteamHeight = steam;
                    foundWaterFaceCount = faces;
                    distanceToNentia = steps;
                    updateGeneratedRotation();
                    setChanged();
                    sendData();
                }
                return;
            }

            break;
        }

        if (foundSteamHeight > 0 || distanceToNentia > 0) {
            foundSteamHeight = 0;
            foundWaterFaceCount = 0;
            distanceToNentia = 0;
            updateGeneratedRotation();
            setChanged();
            sendData();
        }
    }

    @Override
    public float getGeneratedSpeed() {
        if (foundSteamHeight <= 0 || distanceToNentia < 0) return 0;
        float efficiency = calculateEfficiency();
        return SteamConstants.AIR_TURBINE_BASE_SPEED * efficiency;
    }

    @Override
    public float calculateAddedStressCapacity() {
        if (foundSteamHeight <= 0 || distanceToNentia < 0) {
            this.lastCapacityProvided = 0;
            return 0;
        }
        float efficiency = calculateEfficiency();
        float capacity = foundSteamHeight * SteamConstants.AIR_TURBINE_CAPACITY_PER_STEAM * efficiency;
        this.lastCapacityProvided = Math.round(capacity);
        return this.lastCapacityProvided;
    }

    private float calculateEfficiency() {
        int d = distanceToNentia;
        int opt = SteamConstants.AIR_TURBINE_OPTIMAL_DISTANCE;
        int max = SteamConstants.AIR_TURBINE_MAX_EFFECTIVE_DISTANCE;
        if (d <= opt) return 1.0f;
        if (d >= max) return 0.0f;
        return 1.0f - (float) (d - opt) / (max - opt);
    }

    public int getFoundSteamHeight() {
        return foundSteamHeight;
    }

    public int getFoundWaterFaceCount() {
        return foundWaterFaceCount;
    }

    public int getDistanceToNentia() {
        return distanceToNentia;
    }

    @Override
    protected void read(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(tag, registries, clientPacket);
        foundSteamHeight = tag.getInt("FoundSteamHeight");
        foundWaterFaceCount = tag.getInt("FoundWaterFaceCount");
        distanceToNentia = tag.getInt("DistanceToNentia");
    }

    @Override
    protected void write(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(tag, registries, clientPacket);
        tag.putInt("FoundSteamHeight", foundSteamHeight);
        tag.putInt("FoundWaterFaceCount", foundWaterFaceCount);
        tag.putInt("DistanceToNentia", distanceToNentia);
    }
}
