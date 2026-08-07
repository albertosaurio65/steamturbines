package com.xciel.turbines.content.large_turbine;

import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import com.xciel.turbines.AllBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class LargeTurbineBlock extends Block implements IBE<LargeTurbineBlockEntity>, IWrenchable {

    public LargeTurbineBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos neighborPos, boolean isMoving) {
        super.neighborChanged(state, level, pos, block, neighborPos, isMoving);
        if (level.isClientSide) return;
        if (level.getBlockEntity(pos) instanceof LargeTurbineBlockEntity turbine) {
            turbine.onNeighborChanged();
        }
    }

    @Override
    public Class<LargeTurbineBlockEntity> getBlockEntityClass() {
        return LargeTurbineBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends LargeTurbineBlockEntity> getBlockEntityType() {
        return AllBlockEntityTypes.LARGE_TURBINE.get();
    }
}
