package net.goose.simplemaps.client;

import net.goose.simplemaps.SimpleMapMod;
import net.goose.simplemaps.common.PosXZ;
import net.minecraft.FileUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.util.HashMap;

/**
 * Handles storage and updating of client-side data.
 * <p>
 * We use a marching-squares algorithm to save on data: we store chunk positions
 */
public class ClientDataHandler {
    private static final String TAG_SEEN_POSITIONS = "seen_positions";

    private static ClientProfile instance = null;


    public static ClientProfile getProfile() {
        if (instance != null) {
            return instance;
        } else {
            try {
                // java file handling considered harmful
                RandomAccessFile file = new RandomAccessFile(storePath().toFile(), "r");
                CompoundTag tag = CompoundTag.TYPE.load(file, 0, NbtAccounter.UNLIMITED);

                HashMap seenPoses = new HashMap<PosXZ, Float>();
                if (tag.contains(TAG_SEEN_POSITIONS)) {
                    int[] unraveledPoses = tag.getIntArray(TAG_SEEN_POSITIONS);
                    for (int i = 0; i < unraveledPoses.length; i += 3) {
                        int x = unraveledPoses[i];
                        int z = unraveledPoses[i + 1];
                        float amt = (float) unraveledPoses[i + 2] / 255;
                        seenPoses.put(new PosXZ(x, z), amt);
                    }
                }

                instance = new ClientProfile(seenPoses);
                return instance;
            } catch (Exception e) {
                SimpleMapMod.LOGGER.warn("Error trying to load client data: " + e.getMessage());
                instance = defaultProfile();
                return instance;
            }
        }
    }

    private static void saveProfile() {
        // todo :(
    }

    @SubscribeEvent
    public static void closeWorld(PlayerEvent.PlayerLoggedOutEvent evt) {
        if (!(evt.getEntity() instanceof LocalPlayer)) {
            return;
        }

        saveProfile();
    }

    private static ClientProfile defaultProfile() {
        return new ClientProfile(new HashMap<>());
    }

    @Nullable
    private static Path storePath() {
        Minecraft mc = Minecraft.getInstance();

        String name;
        boolean isServer;
        if (mc.isLocalServer()) {
            name = mc.getSingleplayerServer().getWorldData().getLevelName();
            isServer = false;
        } else {
            name = mc.getCurrentServer().name;
            isServer = true;
        }

        Path rootPath = mc.gameDirectory.toPath().resolve("untitledmapmod").resolve(isServer ? "servers" : "worlds");
        try {
            String out = FileUtil.findAvailableName(rootPath, name, "dat");
            return Path.of(out);
        } catch (IOException e) {
            return null;
        }
    }
}
