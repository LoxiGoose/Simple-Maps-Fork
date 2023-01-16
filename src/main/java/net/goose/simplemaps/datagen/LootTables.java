package net.goose.simplemaps.datagen;

import at.petrak.paucal.api.datagen.PaucalLootTableProvider;
import net.goose.simplemaps.common.blocks.ModBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.Map;

public class LootTables extends PaucalLootTableProvider {
    public LootTables(DataGenerator pGenerator) {
        super(pGenerator);
    }

    @Override
    protected void makeLootTables(Map<Block, LootTable.Builder> map, Map<ResourceLocation, LootTable.Builder> map1) {
        dropSelf(map, ModBlocks.MARKERS);
    }
}
