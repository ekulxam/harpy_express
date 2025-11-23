package dev.doctor4t.trainmurdermystery.client.gui;

import dev.doctor4t.trainmurdermystery.api.TMMRoles;
import dev.doctor4t.trainmurdermystery.cca.GameTimeComponent;
import dev.doctor4t.trainmurdermystery.cca.GameWorldComponent;
import dev.doctor4t.trainmurdermystery.game.GameConstants;
import dev.doctor4t.trainmurdermystery.game.GameFunctions;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Pair;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public class TimeRenderer {
    public static TimeNumberRenderer view = new TimeNumberRenderer();
    public static float offsetDelta = 0f;

    private TimeRenderer() {
    }

    public static void renderHud(TextRenderer renderer, @NotNull ClientPlayerEntity player, @NotNull DrawContext context, float delta) {
        GameWorldComponent gameWorldComponent = GameWorldComponent.KEY.get(player.getWorld());
        if (gameWorldComponent.isRunning() && (gameWorldComponent.getGameMode() == GameWorldComponent.GameMode.DISCOVERY || gameWorldComponent.isRole(player, TMMRoles.KILLER) || GameFunctions.isPlayerSpectatingOrCreative(player))) {
            int time = GameTimeComponent.KEY.get(player.getWorld()).getTime();
            if (Math.abs(view.getTarget() - time) > 10) offsetDelta = time > view.getTarget() ? .6f : -.6f;
            if (time < GameConstants.getInTicks(1, 0)) {
                offsetDelta = -0.9f;
            } else {
                offsetDelta = MathHelper.lerp(delta / 16, offsetDelta, 0f);
            }
            view.setTarget(time);
            float red = offsetDelta > 0 ? 1f - offsetDelta : 1f;
            float green = offsetDelta < 0 ? 1f + offsetDelta : 1f;
            float blue = 1f - Math.abs(offsetDelta);
            int colour = MathHelper.packRgb(red, green, blue) | 0xFF000000;
            MatrixStack matrices = context.getMatrices();
            matrices.push();
            matrices.translate(context.getScaledWindowWidth() / 2f, 6, 0);
            view.render(renderer, context, 0, 0, colour, delta);
            matrices.pop();
        }
    }

    public static void tick() {
        view.update();
    }

    public static class TimeNumberRenderer {
        private final Pair<ScrollingDigit, ScrollingDigit> minutes = new Pair<>(new ScrollingDigit(7200, false), new ScrollingDigit(720, false));
        private final Pair<ScrollingDigit, ScrollingDigit> seconds = new Pair<>(new ScrollingDigit(120, true), new ScrollingDigit(12, false));
        private float target;

        public void setTarget(float target) {
            this.target = target;
            float seconds = target / 20;
            float mins = seconds / 60;
            this.seconds.getLeft().setTarget(seconds / 10);
            this.seconds.getRight().setTarget(seconds);
            this.minutes.getLeft().setTarget(mins / 10);
            this.minutes.getRight().setTarget(mins);
        }

        public void update() {
            this.minutes.getLeft().update();
            this.minutes.getRight().update();
            this.seconds.getLeft().update();
            this.seconds.getRight().update();
        }

        public void render(TextRenderer renderer, @NotNull DrawContext context, int x, int y, int colour, float delta) {
            MatrixStack matrices = context.getMatrices();
            matrices.push();
            matrices.translate(x, y, 0);
            matrices.translate(16, 0, 0);
            this.seconds.getRight().render(renderer, context, colour, delta);
            matrices.translate(-8, 0, 0);
            this.seconds.getLeft().render(renderer, context, colour, delta);
            matrices.translate(-8, 0, 0);
            context.drawTextWithShadow(renderer, ":", 2, 0, colour);
            matrices.translate(-8, 0, 0);
            this.minutes.getRight().render(renderer, context, colour, delta);
            matrices.translate(-8, 0, 0);
            this.minutes.getLeft().render(renderer, context, colour, delta);
            matrices.pop();
        }

        public float getTarget() {
            return this.target;
        }
    }

    public static class ScrollingDigit {
        private final int power;
        private final boolean cap6;
        private float target;
        private float value;
        private float lastValue;

        public ScrollingDigit(int power, boolean cap6) {
            this.power = power;
            this.cap6 = cap6;
        }

        public void update() {
            this.lastValue = this.value;
            this.value = MathHelper.lerp(0.15f, this.value, this.target);
            if (Math.abs(this.value - this.target) < 0.01f) this.value = this.target;
        }

        public void render(@NotNull TextRenderer renderer, @NotNull DrawContext context, int colour, float delta) {
            float value = MathHelper.lerp(delta, this.lastValue, this.value);
            int digit = MathHelper.floor(value) % (this.cap6 ? 6 : 10);
            int digitNext = MathHelper.floor(value + 1) % (this.cap6 ? 6 : 10);
            double offset = Math.pow(value % 1, this.power);
            colour &= 0xFFFFFF;
            MatrixStack matrices = context.getMatrices();
            matrices.push();
            matrices.translate(0, -offset * (renderer.fontHeight + 2), 0);
            double alpha = (1.0f - Math.abs(offset)) * 255.0f;
            int baseColour = colour | (int) alpha << 24;
            int nextColour = colour | (int) (Math.abs(offset) * 255.0f) << 24;
            if ((baseColour & -67108864) != 0) {
                context.drawTextWithShadow(renderer, String.valueOf(digit), 0, 0, baseColour);
            }
            if ((nextColour & -67108864) != 0) {
                context.drawTextWithShadow(renderer, String.valueOf(digitNext), 0, renderer.fontHeight + 2, nextColour);
            }
            matrices.pop();
        }

        public void setTarget(float target) {
            this.target = target;
        }
    }
}