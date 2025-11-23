package dev.doctor4t.trainmurdermystery.networking;

import dev.doctor4t.trainmurdermystery.TMM;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public final class GunDropS2CPayload implements CustomPayload {
    public static final GunDropS2CPayload INSTANCE = new GunDropS2CPayload();

    public static final Id<GunDropS2CPayload> ID = new Id<>(TMM.id("gundrop"));
    public static final PacketCodec<PacketByteBuf, GunDropS2CPayload> CODEC = PacketCodec.unit(INSTANCE);

    private GunDropS2CPayload() {
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}