package net.goose.simplemaps;

import net.goose.simplemaps.client.GuiWorldMap;
import net.goose.simplemaps.client.MinimapOverlay;
import net.goose.simplemaps.common.blocks.ModBlocks;
import net.goose.simplemaps.common.capability.ModCapabilities;
import net.goose.simplemaps.common.items.ModItems;
import net.goose.simplemaps.common.network.ModMessages;
import net.goose.simplemaps.datagen.Advancements;
import net.goose.simplemaps.datagen.ModDataGenerators;
import net.goose.simplemaps.datagen.lootmods.ModGlobalLoot;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(SimpleMapMod.MOD_ID)
public class SimpleMapMod {
    public static final String MOD_ID = "simplemaps";

    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();

    public SimpleMapMod() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus evBus = MinecraftForge.EVENT_BUS;

        modBus.register(SimpleMapMod.class);
        modBus.register(ModDataGenerators.class);
        modBus.register(Advancements.class); // register triggers
        ModGlobalLoot.GLOBAL_LOOT.register(modBus);
        ModItems.ITEMS.register(modBus);
        ModBlocks.BLOCKS.register(modBus);

        evBus.register(ModCapabilities.class);

        ModMessages.register();

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            evBus.register(MinimapOverlay.class);
            evBus.register(GuiWorldMap.class);
        });
    }

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent evt) {
        evt.enqueueWork(() -> {
            MinimapOverlay.initTextures();
            GuiWorldMap.initTextures();
        });
    }
}
