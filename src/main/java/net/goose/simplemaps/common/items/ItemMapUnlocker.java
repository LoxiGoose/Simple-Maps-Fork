package net.goose.simplemaps.common.items;

import net.goose.simplemaps.SimpleMapMod;
import net.goose.simplemaps.common.advancement.AdvancementHelper;
import net.goose.simplemaps.datagen.Advancements;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ItemMapUnlocker extends Item {
    public final boolean isMinimap;

    public ItemMapUnlocker(boolean isMinimap, Properties p_41383_) {
        super(p_41383_);
        this.isMinimap = isMinimap;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand used) {
        ResourceLocation id = new ResourceLocation(SimpleMapMod.MOD_ID, this.isMinimap ? "minimap" : "world_map");
        if (!AdvancementHelper.isDone(player, id)) {
            if (player instanceof ServerPlayer splayer) {
                Advancements.USE_MAP_UNLOCKER_TRIGGER.trigger(splayer, this.isMinimap);
            }

            world.playSound(
                player, player.blockPosition(),
                this.isMinimap ? SoundEvents.ENCHANTMENT_TABLE_USE : SoundEvents.BOOK_PAGE_TURN,
                SoundSource.PLAYERS, 1f, 1f);

            ItemStack stack = player.getItemInHand(used);
            stack.shrink(1);
            return InteractionResultHolder.sidedSuccess(stack, world.isClientSide());
        } else {
            return InteractionResultHolder.fail(player.getItemInHand(used));
        }
    }
}
