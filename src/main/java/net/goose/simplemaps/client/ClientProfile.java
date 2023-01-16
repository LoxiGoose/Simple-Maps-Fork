package net.goose.simplemaps.client;

import net.goose.simplemaps.common.PosXZ;
import net.minecraft.core.BlockPos;

import java.util.Map;

/**
 * * seenPoses: maps chunk positions to amount of time spent there
 */
public record ClientProfile(Map<PosXZ, Float> seenPoses) {
    /**
     * Get the visibility amount from 0 (invisible) to 1 (100% visible)
     */
    public float getVisibility(BlockPos pos) {
        PosXZ homeCorner = PosXZ.fromBlockPos(pos);
        // todo some kind of weighted average
        return this.seenPoses.getOrDefault(homeCorner, 0f);
    }
}
