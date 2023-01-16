package net.goose.simplemaps.datagen.lootmods;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.goose.simplemaps.SimpleMapMod;
import net.goose.simplemaps.common.items.ModItems;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class ModGlobalLoot {
    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> GLOBAL_LOOT = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, SimpleMapMod.MOD_ID);
    public static final RegistryObject<Codec<MapModifier>> MAP_MODIFIER = GLOBAL_LOOT.register("loot", MapModifier.CODEC);

    public static class MapModifier extends LootModifier{
        public static final Supplier<Codec<MapModifier>> CODEC = Suppliers.memoize(() -> RecordCodecBuilder.create(
                inst -> codecStart(inst)
                .and(ExtraCodecs.POSITIVE_INT.optionalFieldOf("chance", 2).forGetter(m -> (int) m.chance))
                .apply(inst, MapModifier::new)
        ));
        ;
        private final float chance;
        public MapModifier(LootItemCondition[] conditions, float chance) {
            super(conditions);
            this.chance = chance;
        }

        @Override
        protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
            RandomSource rand = context.getRandom();
            if (rand.nextFloat() < chance) {
                Boolean minimap = rand.nextBoolean();
                generatedLoot.add(new ItemStack((minimap ? ModItems.MINIMAP : ModItems.WORLD_MAP).get()));
            }
            return generatedLoot;
        }

        @Override
        public Codec<? extends IGlobalLootModifier> codec() {
            return CODEC.get();
        }
    }
}
