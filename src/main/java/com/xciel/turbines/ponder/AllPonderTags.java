package com.xciel.turbines.ponder;

import com.simibubi.create.infrastructure.ponder.AllCreatePonderTags;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.xciel.turbines.AllBlocks;
import com.xciel.turbines.Turbines;
import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.minecraft.resources.ResourceLocation;

public class AllPonderTags {

    public static final ResourceLocation STEAM = Turbines.rl("steam");

    public static void register(PonderTagRegistrationHelper<ResourceLocation> helper) {
        PonderTagRegistrationHelper<BlockEntry<?>> HELPER = helper.withKeyFunction(RegistryEntry::getId);

        HELPER.registerTag(STEAM)
                .addToIndex()
                .item(AllBlocks.STEAM_BOILER.get(), true, false)
                .title("Steam Power")
                .description("Components which produce, transport and consume pressurized steam")
                .register();

        HELPER.addToTag(AllCreatePonderTags.KINETIC_SOURCES)
                .add(AllBlocks.STEAM_BOILER);

        HELPER.addToTag(AllCreatePonderTags.KINETIC_APPLIANCES)
                .add(AllBlocks.STEAM_TURBINE)
                .add(AllBlocks.STEAM_COMPRESSOR);

        HELPER.addToTag(AllCreatePonderTags.FLUIDS)
                .add(AllBlocks.STEAM_BOILER)
                .add(AllBlocks.PRESSURE_PIPE)
                .add(AllBlocks.STEAM_PUMP);
    }
}
