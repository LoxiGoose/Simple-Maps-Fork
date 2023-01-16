package net.goose.simplemaps.client;

import net.goose.simplemaps.SimpleMapMod;
import com.mojang.blaze3d.platform.InputConstants;
import net.goose.simplemaps.common.advancement.AdvancementHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.Input;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

// https://github.com/VazkiiMods/Quark/blob/master/src/main/java/vazkii/quark/base/client/handler/ModKeybindHandler.java
public class ModKeybinds {
    public static final String GROUP = SimpleMapMod.MOD_ID + ".gui.keygroup";

    public static KeyMapping OPEN_WORLD_MAP = bind("open_world_map", "m");

    public static KeyMapping bind(String name, String key) {
        KeyMapping kb = new KeyMapping(SimpleMapMod.MOD_ID + ".keybind." + name,
                KeyConflictContext.IN_GAME,
                InputConstants.Type.KEYSYM,
                (key == null ? InputConstants.UNKNOWN :
                        InputConstants.getKey("key.keyboard." + key)).getValue(),
                GROUP);
        return kb;
    }


    @Mod.EventBusSubscriber(modid = SimpleMapMod.MOD_ID, value = Dist.CLIENT)
    public static class ClientForgeEvents {
        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event) {
            if(OPEN_WORLD_MAP.isDown()){
                Minecraft mc = Minecraft.getInstance();
                if (mc.player != null && mc.screen == null) {
                    if (AdvancementHelper.isDone(mc.player, new ResourceLocation(SimpleMapMod.MOD_ID, "world_map"))) {
                        mc.setScreen(new GuiWorldMap(mc.player));
                    } else {
                        mc.player.displayClientMessage(
                                Component.translatable(SimpleMapMod.MOD_ID + ".message.fail_open_map"),
                                true);
                    }
                }
            }
        }
    }

    @Mod.EventBusSubscriber(modid = SimpleMapMod.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents {
        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event) {
            event.register(OPEN_WORLD_MAP);
        }
    }
}
