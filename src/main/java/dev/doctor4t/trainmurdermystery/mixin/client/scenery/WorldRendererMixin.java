package dev.doctor4t.trainmurdermystery.mixin.client.scenery;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.doctor4t.trainmurdermystery.cca.TrainWorldComponent;
import dev.doctor4t.trainmurdermystery.client.TMMClient;
import dev.doctor4t.trainmurdermystery.client.util.AlwaysVisibleFrustum;
import net.minecraft.client.render.BackgroundRenderer.FogData;
import net.minecraft.client.render.BackgroundRenderer.FogType;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.FogShape;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {

    @Shadow
    @Nullable
    private ClientWorld world;

    @Inject(method = "method_52816", at = @At(value = "RETURN"), cancellable = true)
    private static void setFrustumToAlwaysVisible(Frustum frustum, @NotNull CallbackInfoReturnable<Frustum> cir) {
        cir.setReturnValue(new AlwaysVisibleFrustum(frustum));
    }

    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;renderSky(Lorg/joml/Matrix4f;Lorg/joml/Matrix4f;FLnet/minecraft/client/render/Camera;ZLjava/lang/Runnable;)V"))
    public void disableSky(WorldRenderer instance, Matrix4f matrix4f, Matrix4f projectionMatrix, float tickDelta, Camera camera, boolean thickFog, Runnable fogCallback, Operation<Void> original) {
        if (TMMClient.isTrainMoving(this.world)) {
            return;
        }
        TrainWorldComponent trainComponent = TMMClient.getTrainComponent(this.world);
        if (trainComponent != null && trainComponent.getTimeOfDay() != TrainWorldComponent.TimeOfDay.SUNDOWN) {
            return;
        }
        original.call(instance, matrix4f, projectionMatrix, tickDelta, camera, thickFog, fogCallback);
    }

    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/BackgroundRenderer;applyFog(Lnet/minecraft/client/render/Camera;Lnet/minecraft/client/render/BackgroundRenderer$FogType;FZF)V"))
    public void applyBlizzardFog(Camera camera, FogType fogType, float viewDistance, boolean thickFog, float tickDelta, Operation<Void> original) {
        TrainWorldComponent trainComponent = TMMClient.getTrainComponent(this.world);
        if (trainComponent == null || !trainComponent.isFoggy()) {
            return;
        }

        if (TMMClient.isTrainMoving(this.world)) {
            trainmurdermystery$doFog(0, 130);
        } else {
            trainmurdermystery$doFog(0, 200);
        }
    }

    @SuppressWarnings("SameParameterValue")
    @Unique
    private static void trainmurdermystery$doFog(float fogStart, float fogEnd) {
        FogData fogData = new FogData(FogType.FOG_SKY);

        fogData.fogStart = fogStart;
        fogData.fogEnd = fogEnd;

        fogData.fogShape = FogShape.SPHERE;

        RenderSystem.setShaderFogStart(fogData.fogStart);
        RenderSystem.setShaderFogEnd(fogData.fogEnd);
        RenderSystem.setShaderFogShape(fogData.fogShape);
    }

}