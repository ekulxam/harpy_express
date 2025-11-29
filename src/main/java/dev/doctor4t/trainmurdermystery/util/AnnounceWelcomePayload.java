package dev.doctor4t.trainmurdermystery.util;

import dev.doctor4t.trainmurdermystery.TMM;
import dev.doctor4t.trainmurdermystery.client.gui.RoleAnnouncementTexts;
import dev.doctor4t.trainmurdermystery.client.gui.RoundTextRenderer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public record AnnounceWelcomePayload(Identifier roleText, int killers, int targets) implements CustomPayload {
    public static final Id<AnnounceWelcomePayload> ID = new Id<>(TMM.id("announcewelcome"));
    public static final PacketCodec<PacketByteBuf, AnnounceWelcomePayload> CODEC = PacketCodec.tuple(Identifier.PACKET_CODEC, AnnounceWelcomePayload::roleText, PacketCodecs.INTEGER, AnnounceWelcomePayload::killers, PacketCodecs.INTEGER, AnnounceWelcomePayload::targets, AnnounceWelcomePayload::new);

    public AnnounceWelcomePayload(RoleAnnouncementTexts.RoleAnnouncementText roleAnnouncementText, int killers, int targets) {
        this(roleAnnouncementText.getId(), killers, targets);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static class Receiver implements ClientPlayNetworking.PlayPayloadHandler<AnnounceWelcomePayload> {
        @Override
        public void receive(@NotNull AnnounceWelcomePayload payload, ClientPlayNetworking.@NotNull Context context) {
            RoundTextRenderer.startWelcome(RoleAnnouncementTexts.ID_TO_TEXT.get(payload.roleText), payload.killers(), payload.targets());
        }
    }
}