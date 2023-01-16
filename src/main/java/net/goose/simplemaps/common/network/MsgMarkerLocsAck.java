package net.goose.simplemaps.common.network;

import net.goose.simplemaps.client.GuiWorldMap;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.function.Supplier;

public record MsgMarkerLocsAck(List<BlockPos> locations) {
    public static MsgMarkerLocsAck deserialize(ByteBuf buffer) {
        FriendlyByteBuf buf = new FriendlyByteBuf(buffer);
        List list = buf.readList(FriendlyByteBuf::readBlockPos);
        return new MsgMarkerLocsAck(list);
    }

    public void serialize(ByteBuf buffer) {
        FriendlyByteBuf buf = new FriendlyByteBuf(buffer);
        buf.writeCollection(this.locations, FriendlyByteBuf::writeBlockPos);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                Minecraft mc = Minecraft.getInstance();
                Screen screen = mc.screen;
                if (screen instanceof GuiWorldMap worldMap) {
                    worldMap.loadMarkerLocations(this.locations);
                }
            });
        });
        ctx.get().setPacketHandled(true);
    }
}