package dev.doctor4t.trainmurdermystery.networking;

import dev.doctor4t.trainmurdermystery.TMM;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public final class TaskCompleteS2CPayload implements CustomPayload {
    public static final TaskCompleteS2CPayload INSTANCE = new TaskCompleteS2CPayload();

    public static final Id<TaskCompleteS2CPayload> ID = new Id<>(TMM.id("taskcomplete"));
    public static final PacketCodec<PacketByteBuf, TaskCompleteS2CPayload> CODEC = PacketCodec.unit(INSTANCE);

    private TaskCompleteS2CPayload() {
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}