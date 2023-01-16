package net.goose.simplemaps.common.advancement;

import net.goose.simplemaps.datagen.Advancements;
import net.goose.simplemaps.mixin.client.AccessorClientAdvancementsProgress;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientAdvancements;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.ServerAdvancementManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.Map;

public class AdvancementHelper {
    /**
     * Side-agnostic code to get a player's advancement status.
     */
    public static boolean isDone(Player player, ResourceLocation id) {
        if (player instanceof ServerPlayer splayer) {
            PlayerAdvancements advs = splayer.getAdvancements();
            ServerAdvancementManager karen = splayer.getLevel().getServer().getAdvancements();
            Advancement adv = karen.getAdvancement(id);
            if (adv != null) {
                return advs.getOrStartProgress(adv).isDone();
            }
        } else {
            // Problem: these aren't all loaded on world load!
            // We cheat by just always returning "no progress" until they're all loaded
            // If you're removing all advancements for some reason it won't throw an error when you try to
            // get a nonexistent advancement but why the hell are you doing that
            // Also sometimes the connection is null (before we connect to the server right as we log in)
            // so we check that too
            ClientPacketListener conn = Minecraft.getInstance().getConnection();
            if (conn != null) {
                ClientAdvancements advs = conn.getAdvancements();
                Advancement adv = advs.getAdvancements().get(id);
                if (adv != null) {
                    Map<Advancement, AdvancementProgress> progresses = ((AccessorClientAdvancementsProgress) advs).umm$GetProgress();
                    AdvancementProgress prog = progresses.get(adv);
                    return prog != null && prog.isDone();
                }
            }
        }
        return false;
    }
}
