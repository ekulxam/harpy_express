package dev.doctor4t.trainmurdermystery.api;

import dev.doctor4t.trainmurdermystery.TMM;
import dev.doctor4t.trainmurdermystery.client.gui.RoleAnnouncementTexts;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public final class TMMRoles {
    public static final List<Role> ROLES = new ArrayList<>();

    @SuppressWarnings("unused")
    public static final Role CIVILIAN = registerRole(
            Role.builder()
                    .color(0x36E51B)
                    .innocent()
                    .announcementText(RoleAnnouncementTexts.CIVILIAN)
                    .build(TMM.id("civilian"))
    );
    public static final Role VIGILANTE = registerRole(
            Role.builder()
                    .color(0x1B8AE5)
                    .innocent()
                    .announcementText(RoleAnnouncementTexts.VIGILANTE)
                    .build(TMM.id("vigilante"))
    );
    public static final Role KILLER = registerRole(
            Role.builder()
                    .color(0xC13838)
                    .killer()
                    .announcementText(RoleAnnouncementTexts.KILLER)
                    .build(TMM.id("killer"))
    );
    @SuppressWarnings("unused")
    public static final Role LOOSE_END = registerRole(
            Role.builder()
                    .color(0x9F0000)
                    .announcementText(RoleAnnouncementTexts.LOOSE_END)
                    .build(TMM.id("loose_end"))
    );

    private TMMRoles() {
    }

    public static Role registerRole(Role role) {
        ROLES.add(role);
        return role;
    }

    @SuppressWarnings("ClassCanBeRecord")
    public static class Role {
        protected final Identifier identifier;
        protected final int color;
        protected final boolean innocent;
        protected final boolean canUseKiller;
        protected final RoleAnnouncementTexts.RoleAnnouncementText announcementText;

        /**
         * @param identifier       the mod id and name of the role
         * @param color            the role announcement color
         * @param innocent         whether the gun drops when a person with this role is shot
         * @param canUseKiller     can see and use the killer features
         * @param announcementText the welcome text to show this role
         */
        public Role(Identifier identifier, int color, boolean innocent, boolean canUseKiller, RoleAnnouncementTexts.RoleAnnouncementText announcementText) {
            this.identifier = identifier;
            this.color = color;
            this.innocent = innocent;
            this.canUseKiller = canUseKiller;
            this.announcementText = announcementText;
        }

        public Identifier getId() {
            return this.identifier;
        }

        public int getColor() {
            return this.color;
        }

        public boolean isInnocent() {
            return this.innocent;
        }

        public boolean canUseKiller() {
            return this.canUseKiller;
        }

        public RoleAnnouncementTexts.RoleAnnouncementText getAnnouncementText() {
            return this.announcementText;
        }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder {
            protected int color = -1;
            protected boolean innocent = false;
            protected boolean canUseKiller = false;
            protected RoleAnnouncementTexts.RoleAnnouncementText announcementText = RoleAnnouncementTexts.BLANK;

            public Builder() {
            }

            public Builder color(int color) {
                this.color = color;
                return this;
            }

            public Builder setInnocent(boolean innocent) {
                this.innocent = innocent;
                return this;
            }

            public Builder innocent() {
                return this.setInnocent(true);
            }

            public Builder setKiller(boolean canUseKiller) {
                this.canUseKiller = canUseKiller;
                return this;
            }

            public Builder killer() {
                return this.setKiller(true);
            }

            public Builder announcementText(RoleAnnouncementTexts.RoleAnnouncementText announcementText) {
                this.announcementText = announcementText;
                return this;
            }

            public Role build(Identifier id) {
                return new Role(id, this.color, this.innocent, this.canUseKiller, this.announcementText);
            }
        }
    }
}
