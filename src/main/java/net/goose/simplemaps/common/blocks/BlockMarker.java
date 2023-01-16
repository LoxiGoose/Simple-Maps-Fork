package net.goose.simplemaps.common.blocks;

import net.goose.simplemaps.SimpleMapMod;
import net.goose.simplemaps.common.capability.CapMarkerLocations;
import net.goose.simplemaps.common.capability.ModCapabilities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;

import java.util.Optional;

public class BlockMarker extends Block {
    public final DyeColor color;

    public BlockMarker(DyeColor color, BlockBehaviour.Properties properties) {
        super(properties);
        this.color = color;
    }

    @Override
    public void onPlace(BlockState bs, Level world, BlockPos pos, BlockState prevBs, boolean isMoving) {
        Optional<CapMarkerLocations> maybeCap = world.getCapability(ModCapabilities.MARKER_LOCATIONS).resolve();
        maybeCap.ifPresent(locations -> {
            locations.addLocation(pos);
        });

        super.onPlace(bs, world, pos, prevBs, isMoving);
    }

    @Override
    public void onRemove(BlockState bs, Level world, BlockPos pos, BlockState newBs, boolean isMoving) {
        Optional<CapMarkerLocations> maybeCap = world.getCapability(ModCapabilities.MARKER_LOCATIONS).resolve();
        maybeCap.ifPresent(locations -> {
            boolean removed = locations.removeLocation(pos);
            if (!removed) {
                SimpleMapMod.LOGGER.warn("{} didn't actually have a marker on it", pos);
            }
        });

        super.onRemove(bs, world, pos, newBs, isMoving);
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState pState) {
        return PushReaction.BLOCK;
    }
}
