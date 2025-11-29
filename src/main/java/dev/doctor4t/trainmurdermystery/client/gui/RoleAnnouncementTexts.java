package dev.doctor4t.trainmurdermystery.client.gui;

import dev.doctor4t.trainmurdermystery.TMM;
import dev.doctor4t.trainmurdermystery.api.Role;
import dev.doctor4t.trainmurdermystery.api.TMMRoles;
import dev.doctor4t.trainmurdermystery.game.GameFunctions;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

public class RoleAnnouncementTexts {
    public static final Map<Identifier, RoleAnnouncementText> ID_TO_TEXT = new HashMap<>();
    public static final Map<Role, RoleAnnouncementText> ROLE_TO_TEXT = new HashMap<>();

    public static RoleAnnouncementText register(Role role, RoleAnnouncementText text) {
        ROLE_TO_TEXT.put(role, text);
        return text;
    }

    public static RoleAnnouncementText getFromRole(Role role) {
        if (role == null) return BLANK;
        RoleAnnouncementText text = ROLE_TO_TEXT.get(role);
        if (text == null) return CIVILIAN;
        return text;
    }

    public static final RoleAnnouncementText BLANK = new RoleAnnouncementText("", 0xFFFFFF);
    public static final RoleAnnouncementText CIVILIAN = register(TMMRoles.CIVILIAN, new RoleAnnouncementText("civilian", 0x36E51B));
    public static final RoleAnnouncementText VIGILANTE = register(TMMRoles.VIGILANTE, new RoleAnnouncementText("vigilante", 0x1B8AE5));
    public static final RoleAnnouncementText KILLER = register(TMMRoles.KILLER, new RoleAnnouncementText("killer", 0xC13838));
    public static final RoleAnnouncementText LOOSE_END = register(TMMRoles.LOOSE_END, new RoleAnnouncementText("loose_end", 0x9F0000));

    public static class RoleAnnouncementText {
        private final Identifier id;
        public final int color;
        public final Text roleText;
        public final Text titleText;
        public final Text welcomeText;
        public final Function<Integer, Text> premiseText;
        public final Function<Integer, Text> goalText;
        public final Text winText;

        protected RoleAnnouncementText(String name, int color) {
            this(TMM.id(name), color);
        }

        public RoleAnnouncementText(Identifier id, int color) {
            this.id = id;
            this.color = color;
            String idString = this.getTMMTranslationKey();
            this.roleText = Text.translatable("announcement.role." + idString).withColor(this.color);
            this.titleText = Text.translatable("announcement.title." + idString).withColor(this.color);
            this.welcomeText = Text.translatable("announcement.welcome", this.roleText).withColor(0xF0F0F0);
            this.premiseText = (count) -> Text.translatable(count == 1 ? "announcement.premise" : "announcement.premises", count);
            this.goalText = (count) -> Text.translatable((count == 1 ? "announcement.goal." : "announcement.goals.") + idString, count).withColor(this.color);
            this.winText = Text.translatable("announcement.win." + idString).withColor(this.color);
            ID_TO_TEXT.put(this.id, this);
        }

        public Identifier getId() {
            return this.id;
        }

        public String getTMMTranslationKey() {
            if (this.id.getNamespace().equals(TMM.MOD_ID)) {
                return this.id.getPath().toLowerCase(Locale.ROOT);
            } else {
                return this.id.toTranslationKey().toLowerCase(Locale.ROOT);
            }
        }

        public Text getLoseText() {
            return this == KILLER ? CIVILIAN.winText : KILLER.winText;
        }

        public @Nullable Text getEndText(GameFunctions.@NotNull WinStatus status, Text winner) {
            return switch (status) {
                case NONE -> null;
                case PASSENGERS, TIME -> this == KILLER ? this.getLoseText() : this.winText;
                case KILLERS -> this == KILLER ? this.winText : this.getLoseText();
                case LOOSE_END ->
                        Text.translatable("announcement.win." + this.getTMMTranslationKey(), winner).withColor(LOOSE_END.color);
            };
        }
    }
}
