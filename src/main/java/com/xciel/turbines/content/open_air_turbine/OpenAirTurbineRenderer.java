package com.xciel.turbines.content.open_air_turbine;

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

public class OpenAirTurbineRenderer extends SafeBlockEntityRenderer<OpenAirTurbineBlockEntity> {

    private static final PartialModel BLADE_1 = PartialModel.of(Turbines.rl("block/open_air_turbine/open_air_turbine-blade-1"));
    private static final PartialModel BLADE_2 = PartialModel.of(Turbines.rl("block/open_air_turbine/open_air_turbine-blade"));
    private static final PartialModel BLADE_3 = PartialModel.of(Turbines.rl("block/open_air_turbine/open_air_turbine-blade-3"));

    private static final float SPEED = 18f;

    public OpenAirTurbineRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    protected void renderSafe(OpenAirTurbineBlockEntity be, float partialTicks, PoseStack ms,
                              MultiBufferSource buffer, int light, int overlay) {
        BlockState state = be.getBlockState();

        float time = AnimationTickHolder.getRenderTime(be.getLevel());
        float angle1 = (time * SPEED) % 360f;
        float angle2 = (-time * SPEED) % 360f;
        float angle3 = (time * SPEED) % 360f;

        renderBlade(BLADE_1, angle1, state, ms, buffer, light);
        renderBlade(BLADE_2, angle2, state, ms, buffer, light);
        renderBlade(BLADE_3, angle3, state, ms, buffer, light);
    }

    private static void renderBlade(PartialModel blade, float angleDeg,
                                    BlockState state, PoseStack ms, MultiBufferSource buffer, int light) {
        SuperByteBuffer bladeBuffer = CachedBuffers.partial(blade, state);
        float rad = angleDeg * ((float) Math.PI / 180f);
        bladeBuffer.rotateCentered(rad, Direction.UP);
        bladeBuffer.light(light);
        bladeBuffer.renderInto(ms, buffer.getBuffer(RenderType.solid()));
    }

    public static void register() {
        net.minecraft.client.renderer.blockentity.BlockEntityRenderers.register(
            com.xciel.turbines.AllBlockEntityTypes.OPEN_AIR_TURBINE.get(),
            OpenAirTurbineRenderer::new);
    }
}
