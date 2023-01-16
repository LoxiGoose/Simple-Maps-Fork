package net.goose.simplemaps.datagen;

import net.goose.simplemaps.SimpleMapMod;
import net.goose.simplemaps.common.blocks.BlockMarker;
import net.goose.simplemaps.common.blocks.ModBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

public class ModBlockTags extends BlockTagsProvider {
    public ModBlockTags(DataGenerator dataGenerator, @Nullable ExistingFileHelper existingFileHelper) {
        super(dataGenerator, SimpleMapMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        for (RegistryObject<BlockMarker> blockRobj : ModBlocks.MARKERS) {
            this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(blockRobj.get());
        }
    }
}
