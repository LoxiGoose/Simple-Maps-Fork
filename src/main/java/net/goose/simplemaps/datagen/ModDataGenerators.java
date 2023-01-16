package net.goose.simplemaps.datagen;

import at.petrak.paucal.api.forge.datagen.PaucalForgeDatagenWrappers;
import net.goose.simplemaps.common.advancement.AdvancementHelper;
import net.goose.simplemaps.datagen.lootmods.ModLootMods;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.concurrent.CompletableFuture;

public class ModDataGenerators {
    @SubscribeEvent
    public static void generateData(GatherDataEvent ev) {
        final DataGenerator gen = ev.getGenerator();
        final ExistingFileHelper efh = ev.getExistingFileHelper();

        if (ev.includeClient()) {
            gen.addProvider(ev.includeClient(), new ItemModels(gen, efh));
            gen.addProvider(ev.includeClient(), new BlockModels(gen, efh));
            gen.addProvider(ev.includeClient(), new ModLootMods(gen));
        }
        if (ev.includeServer()) {
            gen.addProvider(ev.includeServer(), PaucalForgeDatagenWrappers.addEFHToAdvancements(new Advancements(gen), efh));
            gen.addProvider(ev.includeServer(), new Recipes(gen));
            gen.addProvider(ev.includeServer(), new LootTables(gen));
            gen.addProvider(ev.includeServer(), new ModBlockTags(gen, efh));
        }
    }
}
