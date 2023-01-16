package net.goose.simplemaps.client;

import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import net.goose.simplemaps.SimpleMapMod;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.MaterialColor;

public class MapHelper {
    public static final ResourceLocation TEX_MAP_MAIN = new ResourceLocation(SimpleMapMod.MOD_ID,
        "textures/gui/map.png");
    public static final ResourceLocation TEX_MAP_DECO = new ResourceLocation(SimpleMapMod.MOD_ID,
        "textures/gui/decorations.png");
    public static final ResourceLocation TEX_VANILLA_MAP_DECO = new ResourceLocation("minecraft",
        "textures/map/map_icons.png");

    /**
     * Draw the region around the player to the texture and upload it.
     */
    public static void blitMapToTexture(LocalPlayer player, BlockPos targetPos, boolean onlyShowTop,
        DynamicTexture texture) {
        Level level = player.getLevel();
        boolean canPlayerSeeSky = onlyShowTop
            || (level.canSeeSky(player.getOnPos().above()) && !level.dimensionType().hasCeiling());

        int mapWidth = texture.getPixels().getWidth();
        int mapHeight = texture.getPixels().getHeight();

        for (int px = 0; px < mapWidth; px++) {
            for (int py = 0; py < mapHeight; py++) {
                int x = targetPos.getX() + px - mapWidth / 2;
                int z = targetPos.getZ() + py - mapHeight / 2;
                int y = targetPos.getY() + 1;
                BlockPos pos = new BlockPos(x, y, z); // check at the eye position

                MaterialColor color = MaterialColor.COLOR_BLACK;
                int brightness = 220;
                BlockState bs = level.getBlockState(pos);
                if (canPlayerSeeSky) {
                    // Get the top layer of the level
                    int height = level.getHeight(Heightmap.Types.WORLD_SURFACE, x, z);
                    BlockPos topPos = new BlockPos(x, height - 1, z);
                    BlockState topBs = level.getBlockState(topPos);
                    color = topBs.getMapColor(level, topPos);
                    // A block's color is darker if placed at a lower elevation than the block north of it,
                    // or brighter if placed at a higher elevation than the block north of it.
                    // (This is a case of the mapping convention of top lighting.)
                    // -- https://minecraft.fandom.com/wiki/Map_item_format#Map_Pixel_Art
                    int northHeight = level.getHeight(Heightmap.Types.WORLD_SURFACE, x, z - 1);
                    int delta = height - northHeight;
                    brightness = 220 + delta * 8;
                } else if (bs.isAir() || !bs.getFluidState().isEmpty()) {
                    // As per popular map mods, display "solid" things as black/dark and the floor as bright
                    // Scan down
                    int maxDepth = 16;
                    for (int dy = 0; dy < maxDepth; dy++) {
                        BlockPos herePos = new BlockPos(x, y - dy, z);
                        BlockState hereBs = level.getBlockState(herePos);
                        if (!hereBs.isAir()) {
                            // we found our limit
                            color = hereBs.getMapColor(level, herePos);
                            double leftoverY = Mth.frac(player.getX());
                            float prop = (float) (dy + leftoverY) / (float) (maxDepth - 1);
                            brightness -= (int) (100f * prop);

                            BlockPos northUpPos = herePos.offset(0, 1, -1);
                            BlockState northUpBs = level.getBlockState(northUpPos);
                            BlockPos northPos = herePos.offset(0, 0, -1);
                            BlockState northBs = level.getBlockState(northPos);
                            if (!northUpBs.isAir()) {
                                brightness -= 40;
                            } else if (northBs.isAir()) {
                                brightness += 40;
                            }

                            break;
                        }
                    }
                } // else keep it as black

                if (color == MaterialColor.NONE) {
                    // transparent
                    texture.getPixels().setPixelRGBA(px, py, 0);
                } else {
                    brightness = Mth.clamp(brightness, 0, 255);
                    int r = (color.col >> 16 & 255) * brightness / 255;
                    int g = (color.col >> 8 & 255) * brightness / 255;
                    int b = (color.col & 255) * brightness / 255;
                    int colorInt = -16777216 | b << 16 | g << 8 | r;
                    texture.getPixels().setPixelRGBA(px, py, colorInt | 0xFF000000);
                }
            }
        }

        texture.upload();
    }

    /**
     * Make sure you have the `PositionTexColorShader` set
     */
    public static void renderQuad(PoseStack ps, float width, float height, float u, float v, float uw, float vh,
        ResourceLocation tex) {
        renderQuad(ps, width, height, u, v, uw, vh, 0xffffffff, tex);
    }

    /**
     * Make sure you have the `PositionTexColorShader` set
     */
    public static void renderQuad(PoseStack ps, float width, float height, float u, float v, float uw, float vh,
        int color, ResourceLocation tex) {
        Matrix4f mat = ps.last().pose();
        Tesselator tess = Tesselator.getInstance();
        BufferBuilder buf = tess.getBuilder();

        RenderSystem.setShaderTexture(0, tex);

        buf.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        buf.vertex(mat, 0, 0, 0)
            .uv(u, v)
            .color(color)
            .endVertex();
        buf.vertex(mat, 0, height, 0)
            .uv(u, v + vh)
            .color(color)
            .endVertex();
        buf.vertex(mat, width, height, 0)
            .uv(u + uw, v + vh)
            .color(color)
            .endVertex();
        buf.vertex(mat, width, 0, 0)
            .uv(u + uw, v)
            .color(color)
            .endVertex();
        tess.end();
    }
}
