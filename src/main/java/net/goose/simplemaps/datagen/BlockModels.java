package net.goose.simplemaps.datagen;

import at.petrak.paucal.api.forge.datagen.PaucalBlockStateAndModelProvider;
import net.goose.simplemaps.SimpleMapMod;
import net.goose.simplemaps.common.blocks.BlockMarker;
import net.goose.simplemaps.common.blocks.ModBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

/**
 * Also does block models for some reason
 */
public class BlockModels extends PaucalBlockStateAndModelProvider {
    public BlockModels(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, SimpleMapMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        ResourceLocation bottomLoc = new ResourceLocation(SimpleMapMod.MOD_ID, "block/marker_bottom");
        for (RegistryObject<BlockMarker> markerRobj : ModBlocks.MARKERS) {
            BlockMarker marker = markerRobj.get();
            String name = "marker_" + marker.color.getName();
            ResourceLocation sideLoc = new ResourceLocation(SimpleMapMod.MOD_ID,
                "block/marker_" + marker.color.getName() + "_side");
            ResourceLocation topLoc = new ResourceLocation(SimpleMapMod.MOD_ID, "block/marker_" + marker.color.getName() + "_top");
            BlockModelBuilder model = models().cubeBottomTop(name, sideLoc, bottomLoc, topLoc);
            simpleBlock(marker, model);
            simpleBlockItem(marker, model);
        }
    }
}
