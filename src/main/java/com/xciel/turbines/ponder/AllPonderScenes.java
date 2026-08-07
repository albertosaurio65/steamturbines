package com.xciel.turbines.ponder;

import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.xciel.turbines.AllBlocks;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.api.scene.Selection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;

public class AllPonderScenes {

    public static void register(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        PonderSceneRegistrationHelper<BlockEntry<?>> HELPER = helper.withKeyFunction(RegistryEntry::getId);

        HELPER.addStoryBoard(AllBlocks.STEAM_BOILER, "steam_boiler", AllPonderScenes::steamBoiler, AllPonderTags.STEAM);
        HELPER.addStoryBoard(AllBlocks.PRESSURE_PIPE, "steam_setup", AllPonderScenes::steamSetup, AllPonderTags.STEAM);
        HELPER.addStoryBoard(AllBlocks.STEAM_TURBINE, "steam_setup", AllPonderScenes::steamSetup, AllPonderTags.STEAM);
    }

    public static void steamBoiler(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("steam_boiler", "Generating Steam with the Steam Boiler");
        scene.configureBasePlate(0, 0, 5);
        scene.scaleSceneView(0.9f);
        scene.showBasePlate();

        BlockPos heatPos = util.grid().at(2, 0, 2);
        BlockPos boilerPos = util.grid().at(2, 1, 2);

        scene.idle(5);
        scene.world().setBlocks(util.select().position(heatPos), Blocks.CAMPFIRE.defaultBlockState(), false);
        scene.idle(10);
        scene.world().setBlocks(util.select().position(boilerPos), AllBlocks.STEAM_BOILER.getDefaultState(), false);

        scene.idle(15);
        scene.overlay().showText(60)
                .text("The Steam Boiler produces pressurized steam when heated from below")
                .attachKeyFrame()
                .pointAt(util.vector().topOf(boilerPos))
                .placeNearTarget();
        scene.idle(70);

        scene.overlay().showText(60)
                .text("Any flame source works, from Campfires to Blaze Burners")
                .pointAt(util.vector().blockSurface(heatPos, Direction.UP))
                .placeNearTarget();
        scene.idle(70);

        scene.overlay().showControls(util.vector().blockSurface(boilerPos, Direction.UP), Pointing.DOWN, 40)
                .rightClick();
        scene.idle(10);
        scene.overlay().showText(60)
                .text("Right-click with a Bucket to fill it with Water or a liquid fuel")
                .pointAt(util.vector().topOf(boilerPos))
                .placeNearTarget();
        scene.idle(70);

        scene.effects().indicateSuccess(boilerPos);
        scene.markAsFinished();
    }

    public static void steamSetup(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("steam_setup", "Steam Generation Setup");
        scene.configureBasePlate(0, 0, 7);
        scene.scaleSceneView(0.8f);
        scene.showBasePlate();

        BlockPos heatPos = util.grid().at(2, 0, 3);
        BlockPos boilerPos = util.grid().at(2, 1, 3);
        Selection pipes = util.select().fromTo(2, 1, 3, 5, 1, 3);
        Selection turbine = util.select().position(5, 1, 3);

        scene.idle(5);
        scene.world().setBlocks(util.select().position(heatPos), Blocks.CAMPFIRE.defaultBlockState(), false);
        scene.idle(5);
        scene.world().setBlocks(util.select().position(boilerPos), AllBlocks.STEAM_BOILER.getDefaultState(), false);
        scene.idle(10);
        scene.world().setBlocks(pipes, AllBlocks.PRESSURE_PIPE.getDefaultState(), false);
        scene.idle(10);
        scene.world().setBlocks(turbine, AllBlocks.STEAM_TURBINE.getDefaultState(), false);

        scene.idle(15);
        scene.overlay().showText(60)
                .text("Pressure Pipes carry steam from the Boiler to your machines")
                .attachKeyFrame()
                .pointAt(util.vector().topOf(util.grid().at(3, 1, 3)))
                .placeNearTarget();
        scene.idle(70);

        scene.overlay().showText(60)
                .text("Steam Turbines convert that pressure into Rotational Force")
                .pointAt(util.vector().topOf(util.grid().at(5, 1, 3)))
                .placeNearTarget();
        scene.idle(70);

        scene.world().setKineticSpeed(util.select().position(5, 1, 3), 32);
        scene.effects().rotationDirectionIndicator(util.grid().at(5, 1, 3));
        scene.idle(40);

        scene.overlay().showText(60)
                .text("Connect the turbine's shaft output to your own machinery")
                .pointAt(util.vector().blockSurface(util.grid().at(5, 1, 3), Direction.EAST))
                .placeNearTarget();
        scene.idle(70);

        scene.markAsFinished();
    }
}
