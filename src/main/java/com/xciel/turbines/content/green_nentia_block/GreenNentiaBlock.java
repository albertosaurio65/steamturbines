package com.xciel.turbines.content.green_nentia_block;

import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import com.xciel.turbines.AllBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class GreenNentiaBlock extends Block implements IBE<GreenNentiaBlockEntity>, IWrenchable {

    public GreenNentiaBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos neighborPos, boolean isMoving) {
        super.neighborChanged(state, level, pos, block, neighborPos, isMoving);
        if (level.isClientSide) return;
        var be = level.getBlockEntity(pos);
        if (be instanceof GreenNentiaBlockEntity nentia) {
            nentia.onNeighborChanged();
        }
    }

    @Override
    public Class<GreenNentiaBlockEntity> getBlockEntityClass() {
        return GreenNentiaBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends GreenNentiaBlockEntity> getBlockEntityType() {
        return AllBlockEntityTypes.GREEN_NENTIA_BLOCK.get();
    }
}
