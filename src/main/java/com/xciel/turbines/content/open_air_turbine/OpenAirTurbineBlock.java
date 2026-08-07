package com.xciel.turbines.content.open_air_turbine;

import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import com.xciel.turbines.AllBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class OpenAirTurbineBlock extends Block implements IBE<OpenAirTurbineBlockEntity>, IWrenchable {

    private static final VoxelShape SHAPE = Shapes.or(
        Block.box(0, 0, 0, 16, 15, 16),
        Block.box(1, 15, 15, 15, 16, 16),
        Block.box(0, 15, 0, 1, 16, 16),
        Block.box(1, 15, 0, 15, 16, 1),
        Block.box(15, 15, 0, 16, 16, 16)
    );

    public OpenAirTurbineBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos neighborPos, boolean isMoving) {
        super.neighborChanged(state, level, pos, block, neighborPos, isMoving);
        if (level.isClientSide) return;
        if (level.getBlockEntity(pos) instanceof OpenAirTurbineBlockEntity turbine) {
            turbine.onNeighborChanged();
        }
    }

    @Override
    public Class<OpenAirTurbineBlockEntity> getBlockEntityClass() {
        return OpenAirTurbineBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends OpenAirTurbineBlockEntity> getBlockEntityType() {
        return AllBlockEntityTypes.OPEN_AIR_TURBINE.get();
    }
}
