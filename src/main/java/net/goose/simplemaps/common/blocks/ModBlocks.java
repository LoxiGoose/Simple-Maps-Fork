package net.goose.simplemaps.common.blocks;

import net.goose.simplemaps.SimpleMapMod;
import net.goose.simplemaps.common.items.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
        DeferredRegister.create(ForgeRegistries.BLOCKS, SimpleMapMod.MOD_ID);

    public static final RegistryObject<BlockMarker>[] MARKERS = new RegistryObject[16];

    static {
        BlockBehaviour.Properties markerProps = BlockBehaviour.Properties.of(Material.STONE)
            .strength(3.5f);
        for (int i = 0; i < DyeColor.values().length; i++) {
            DyeColor color = DyeColor.values()[i];
            String name = "marker_" + color.getName();

            RegistryObject<BlockMarker> marker = BLOCKS.register(name, () -> new BlockMarker(color, markerProps));
            MARKERS[i] = marker;
            int finalI = i;
            ModItems.ITEMS.register(name, () -> new BlockItem(MARKERS[finalI].get(), ModItems.props()));
        }
    }
}
