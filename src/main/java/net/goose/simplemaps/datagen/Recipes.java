package net.goose.simplemaps.datagen;

import at.petrak.paucal.api.datagen.PaucalRecipeProvider;
import net.goose.simplemaps.SimpleMapMod;
import net.goose.simplemaps.common.blocks.BlockMarker;
import net.goose.simplemaps.common.blocks.ModBlocks;
import net.goose.simplemaps.common.items.ModItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Items;

import java.util.function.Consumer;

public class Recipes extends PaucalRecipeProvider {


    public Recipes(DataGenerator gen) {
        super(gen, SimpleMapMod.MOD_ID);
    }
    @Override
    protected void makeRecipes(Consumer<FinishedRecipe> consumer) {
        for (int i = 0; i < DyeColor.values().length; i++) {
            DyeColor color = DyeColor.values()[i];
            BlockMarker marker = ModBlocks.MARKERS[i].get();

            ShapedRecipeBuilder.shaped(marker)
                    .define('D', DyeItem.byColor(color))
                    .define('C', Items.COMPASS)
                    .define('B', Items.CHISELED_STONE_BRICKS)
                    .pattern(" C ")
                    .pattern("BDB")
                    .pattern("BBB")
                    .unlockedBy("has_item", has(ModItems.WORLD_MAP.get()))
                    .save(consumer);
        }
    }
}
