package dev.doctor4t.trainmurdermystery.mixin.client.ui;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.doctor4t.ratatouille.client.lib.render.helpers.Easing;
import dev.doctor4t.trainmurdermystery.TMM;
import dev.doctor4t.trainmurdermystery.cca.GameWorldComponent;
import dev.doctor4t.trainmurdermystery.cca.TrainWorldComponent;
import dev.doctor4t.trainmurdermystery.client.TMMClient;
import dev.doctor4t.trainmurdermystery.client.gui.*;
import dev.doctor4t.trainmurdermystery.game.GameConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Shadow
    @Final
    private MinecraftClient client;
    @Unique
    private static final Identifier trainmurdermystery$HOTBAR_TEXTURE = TMM.id("hud/hotbar");
    @Unique
    private static final Identifier trainmurdermystery$HOTBAR_SELECTION_TEXTURE = TMM.id("hud/hotbar_selection");

    // Look into using Fabric HUD APIs? - SkyNotTheLimit
    @Inject(method = "renderMainHud", at = @At("TAIL"))
    private void renderGameHud(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        TrainWorldComponent trainComponent = TMMClient.getTrainComponent(this.client.world);
        if (trainComponent == null || !trainComponent.hasHud()) {
            return;
        }

        ClientPlayerEntity player = this.client.player;
        if (player == null) {
            return;
        }

        TextRenderer renderer = MinecraftClient.getInstance().textRenderer;
        MoodRenderer.renderHud(player, renderer, context, tickCounter);
        RoleNameRenderer.renderHud(renderer, player, context, tickCounter);
        RoundTextRenderer.renderHud(renderer, player, context);
        if (MinecraftClient.getInstance().currentScreen == null) {
            StoreRenderer.renderHud(renderer, player, context, tickCounter.getTickDelta(true));
        }
        TimeRenderer.renderHud(renderer, player, context, tickCounter.getTickDelta(true));
        LobbyPlayersRenderer.renderHud(renderer, player, context);
    }

    @WrapMethod(method = "renderCrosshair")
    private void renderCrosshair(DrawContext context, RenderTickCounter tickCounter, Operation<Void> original) {
        if (!TMMClient.isPlayerAliveAndInSurvival()) {
            original.call(context, tickCounter);
            return;
        }
        ClientPlayerEntity player = this.client.player;
        if (player == null) {
            return;
        }
        CrosshairRenderer.renderCrosshair(this.client, player, context, tickCounter);
    }

    @WrapMethod(method = "renderStatusBars")
    private void removeStatusBars(DrawContext context, Operation<Void> original) {
        if (TMMClient.isPlayerAliveAndInSurvival()) {
            return;
        }
        original.call(context);
    }

    @WrapMethod(method = "renderExperienceBar")
    private void removeExperienceBar(DrawContext context, int x, Operation<Void> original) {
        if (TMMClient.isPlayerAliveAndInSurvival()) {
            return;
        }
        original.call(context, x);
    }

    @WrapMethod(method = "renderPlayerList")
    private void removePlayerList(DrawContext context, RenderTickCounter tickCounter, Operation<Void> original) {
        if (TMMClient.isPlayerAliveAndInSurvival()) {
            return;
        }
        original.call(context, tickCounter);
    }

    @WrapMethod(method = "renderExperienceLevel")
    private void removeExperienceLevel(DrawContext context, RenderTickCounter tickCounter, Operation<Void> original) {
        if (TMMClient.isPlayerAliveAndInSurvival()) {
            return;
        }
        original.call(context, tickCounter);
    }

    @WrapOperation(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V", ordinal = 0))
    private void overrideHotbarTexture(DrawContext instance, Identifier texture, int x, int y, int width, int height, @NotNull Operation<Void> original) {
        original.call(instance, TMMClient.isPlayerAliveAndInSurvival() ? trainmurdermystery$HOTBAR_TEXTURE : texture, x, y, width, height);
    }

    @WrapOperation(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V", ordinal = 1))
    private void overrideHotbarSelectionTexture(DrawContext instance, Identifier texture, int x, int y, int width, int height, @NotNull Operation<Void> original) {
        original.call(instance, TMMClient.isPlayerAliveAndInSurvival() ? trainmurdermystery$HOTBAR_SELECTION_TEXTURE : texture, x, y, width, height);
    }

    @WrapMethod(method = "renderMiscOverlays")
    private void moveSleepOverlayToUnderUI(DrawContext context, RenderTickCounter tickCounter, Operation<Void> original) {
        if (this.client.player == null || this.client.player.getSleepTimer() <= 0) {
            // I would like to render other overlays when not sleeping (I think this is fine) - SkyNotTheLimit
            original.call(context, tickCounter);
            return;
        }

        // sleep overlay
        this.client.getProfiler().push("sleep");

        float f = (float) this.client.player.getSleepTimer();

        float g = Math.min(1, f / 30f);

        if (f > 100f) {
            g = 1 - (f - 100f) / 10f;
        }

        float fadeAlpha = MathHelper.lerp(MathHelper.clamp(Easing.SINE_IN.ease(g, 0, 1, 1), 0, 1), 0f, 1f);
        Color color = new Color(0.04f, 0f, 0.08f, fadeAlpha);
        context.fill(RenderLayer.getGuiOverlay(), 0, 0, context.getScaledWindowWidth(), context.getScaledWindowHeight(), color.getRGB());

        this.client.getProfiler().pop();
    }

    @WrapMethod(method = "renderSleepOverlay")
    private void removeSleepOverlayAndDoGameFade(DrawContext context, RenderTickCounter tickCounter, Operation<Void> original) {
        GameWorldComponent gameComponent = TMMClient.getGameComponent(this.client.world);
        if (gameComponent == null) {
            return;
        }
        // game start / stop fade in / out
        float fadeIn = gameComponent.getFade();
        if (fadeIn >= 0) {
            this.client.getProfiler().push("tmmFade");
            float fadeAlpha = MathHelper.lerp(Math.min(fadeIn / GameConstants.FADE_TIME, 1), 0f, 1f);
            Color color = new Color(0f, 0f, 0f, fadeAlpha);

            context.fill(RenderLayer.getGuiOverlay(), 0, 0, context.getScaledWindowWidth(), context.getScaledWindowHeight(), color.getRGB());
            this.client.getProfiler().pop();
        }
    }
}
