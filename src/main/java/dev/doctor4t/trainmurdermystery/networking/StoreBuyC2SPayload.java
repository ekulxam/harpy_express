package dev.doctor4t.trainmurdermystery.networking;

import dev.doctor4t.trainmurdermystery.TMM;
import dev.doctor4t.trainmurdermystery.cca.PlayerShopComponent;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import org.jetbrains.annotations.NotNull;

public record StoreBuyC2SPayload(int index) implements CustomPayload {
    public static final Id<StoreBuyC2SPayload> ID = new Id<>(TMM.id("storebuy"));
    public static final PacketCodec<PacketByteBuf, StoreBuyC2SPayload> CODEC = PacketCodec.tuple(PacketCodecs.INTEGER, StoreBuyC2SPayload::index, StoreBuyC2SPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static class Receiver implements ServerPlayNetworking.PlayPayloadHandler<StoreBuyC2SPayload> {
        @Override
        public void receive(@NotNull StoreBuyC2SPayload payload, ServerPlayNetworking.@NotNull Context context) {
            PlayerShopComponent.KEY.get(context.player()).tryBuy(payload.index());
        }
    }
}