package net.goose.simplemaps.datagen.lootmods;

import net.goose.simplemaps.SimpleMapMod;
import net.goose.simplemaps.common.items.ModItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.LootTableIdCondition;

public class ModLootMods extends GlobalLootModifierProvider {

    public ModLootMods(DataGenerator dataGenerator) {
        super(dataGenerator, SimpleMapMod.MOD_ID);
    }

    @Override
    protected void start() {
        Item[] items = new Item[]{
            ModItems.MINIMAP.get(),
            ModItems.WORLD_MAP.get(),
        };
        String[] names = new String[]{
            "minimap", "world_map"
        };

        for (int i = 0; i < 2; i++) {
            Item item = items[i];
            String name = names[i];

            this.add(name + "_dungeon",
                new ModGlobalLoot.MapModifier(new LootItemCondition[]{
                    LootTableIdCondition.builder(BuiltInLootTables.SIMPLE_DUNGEON).build(),
                    LootItemRandomChanceCondition.randomChance(0.2f).build()
                }, 1));
            this.add(name + "_cartographer",
                    new ModGlobalLoot.MapModifier(new LootItemCondition[]{
                            LootTableIdCondition.builder(BuiltInLootTables.VILLAGE_CARTOGRAPHER).build(),
                            LootItemRandomChanceCondition.randomChance(0.8f).build()
                    }, 1));
            this.add(name + "_shipwreck",
                    new ModGlobalLoot.MapModifier(new LootItemCondition[]{
                            LootTableIdCondition.builder(BuiltInLootTables.SHIPWRECK_MAP).build(),
                            LootItemRandomChanceCondition.randomChance(0.8f).build()
                    }, 1));
            this.add(name + "_stronghold_library",
                    new ModGlobalLoot.MapModifier(new LootItemCondition[]{
                            LootTableIdCondition.builder(BuiltInLootTables.STRONGHOLD_LIBRARY).build(),
                            LootItemRandomChanceCondition.randomChance(0.5f).build()
                    }, 1));
        }
    }
}
