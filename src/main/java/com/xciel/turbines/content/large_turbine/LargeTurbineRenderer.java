package com.xciel.turbines.content.large_turbine;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import com.xciel.turbines.Turbines;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public class LargeTurbineRenderer extends SafeBlockEntityRenderer<LargeTurbineBlockEntity> {

    private static final PartialModel BLADES = PartialModel.of(Turbines.rl("block/large_turbine/blades"));

    private static final float SPEED = 18f;

    public LargeTurbineRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    protected void renderSafe(LargeTurbineBlockEntity be, float partialTicks, PoseStack ms,
                              MultiBufferSource buffer, int light, int overlay) {
        BlockState state = be.getBlockState();

        float time = AnimationTickHolder.getRenderTime(be.getLevel());
        float angle = (time * SPEED) % 360f;

        SuperByteBuffer bladeBuffer = CachedBuffers.partial(BLADES, state);
        float rad = angle * ((float) Math.PI / 180f);
        bladeBuffer.rotateCentered(rad, Direction.UP);
        bladeBuffer.light(light);
        bladeBuffer.renderInto(ms, buffer.getBuffer(RenderType.solid()));
    }

    public static void register() {
        net.minecraft.client.renderer.blockentity.BlockEntityRenderers.register(
            com.xciel.turbines.AllBlockEntityTypes.LARGE_TURBINE.get(),
            LargeTurbineRenderer::new);
    }
}
