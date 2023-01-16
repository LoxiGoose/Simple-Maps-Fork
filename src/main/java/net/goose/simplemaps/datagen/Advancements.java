package net.goose.simplemaps.datagen;

import at.petrak.paucal.api.datagen.PaucalAdvancementProvider;
import net.goose.simplemaps.SimpleMapMod;
import net.goose.simplemaps.common.advancement.UseMapUnlockerTrigger;
import net.goose.simplemaps.common.items.ModItems;
import net.minecraft.advancements.*;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.TickingTracker;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class Advancements extends PaucalAdvancementProvider {
    public static final UseMapUnlockerTrigger USE_MAP_UNLOCKER_TRIGGER = new UseMapUnlockerTrigger();

    public Advancements(DataGenerator dataGenerator) {
        super(dataGenerator, SimpleMapMod.MOD_ID);
    }
    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent evt) {
        evt.enqueueWork(Advancements::registerTriggers);
    }

    public static void registerTriggers() {
        CriteriaTriggers.register(USE_MAP_UNLOCKER_TRIGGER);
    }

    @Override
    protected void makeAdvancements(Consumer<Advancement> consumer) {
        Advancement root = Advancement.Builder.advancement()
                .display(simpleDisplayWithBackground(Items.COMPASS, "root", FrameType.TASK, new ResourceLocation("minecraft:textures/block/cartography_table_side3.png")))
                .addCriterion("tick", new PlayerTrigger.TriggerInstance(CriteriaTriggers.TICK.getId(), EntityPredicate.Composite.ANY))
                .save(consumer, modLoc("root"), fileHelper);

        Advancement.Builder.advancement()
                .display(simpleDisplay(ModItems.WORLD_MAP.get(), "world_map", FrameType.TASK))
                .parent(root)
                .addCriterion("on_use", new UseMapUnlockerTrigger.Instance(EntityPredicate.Composite.ANY, false))
                .save(consumer, modLoc("world_map"), fileHelper);

        Advancement.Builder.advancement()
                .display(simpleDisplay(ModItems.MINIMAP.get(), "minimap", FrameType.TASK))
                .parent(root)
                .addCriterion("on_use", new UseMapUnlockerTrigger.Instance(EntityPredicate.Composite.ANY, true))
                .save(consumer, modLoc("minimap"), fileHelper);
    }
}
