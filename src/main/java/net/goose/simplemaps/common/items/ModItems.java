package net.goose.simplemaps.common.items;

import net.goose.simplemaps.SimpleMapMod;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {

    public static final CreativeModeTab TAB = new CreativeModeTab(SimpleMapMod.MOD_ID) {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(MINIMAP::get);
        }
    };
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS,
        SimpleMapMod.MOD_ID);
    public static final RegistryObject<Item> MINIMAP = ITEMS.register("minimap",
        () -> new ItemMapUnlocker(true, unstackable()));
    public static final RegistryObject<Item> WORLD_MAP = ITEMS.register("world_map",
        () -> new ItemMapUnlocker(false, unstackable()));


    public static Item.Properties props() {
        return new Item.Properties().tab(TAB);
    }

    public static Item.Properties unstackable() {
        return props().stacksTo(1);
    }
}
