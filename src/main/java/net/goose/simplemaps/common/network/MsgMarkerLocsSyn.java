package net.goose.simplemaps.common.network;

import net.goose.simplemaps.common.capability.CapMarkerLocations;
import net.goose.simplemaps.common.capability.ModCapabilities;
import io.netty.buffer.ByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.Optional;
import java.util.function.Supplier;

public record MsgMarkerLocsSyn() {
    public static MsgMarkerLocsSyn deserialize(ByteBuf buffer) {
        return new MsgMarkerLocsSyn();
    }

    public void serialize(ByteBuf buffer) {
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer sender = ctx.get().getSender();
            if (sender != null) {
                // Todo: some kind of not sending everyone kb of data
                /*
                LoxiGoose porter here, have no idea how I'd do this differently than what you're already doing since I'm still a new modder LOL.
                Perhaps in future I can do this TODO for you :thinking:
                 */
                Optional<CapMarkerLocations> maybeCap = sender.level.getCapability(ModCapabilities.MARKER_LOCATIONS).resolve();
                maybeCap.ifPresent(locations -> {
                    ModMessages.getNetwork()
                        .send(PacketDistributor.PLAYER.with(() -> sender),
                            new MsgMarkerLocsAck(locations.getLocations()));
                });
            }
        });
        ctx.get().setPacketHandled(true);
    }
}