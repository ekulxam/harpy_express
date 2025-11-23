package dev.doctor4t.trainmurdermystery.mixin.compat.sodium;

import dev.doctor4t.trainmurdermystery.compat.IrisHelper;
import dev.doctor4t.trainmurdermystery.compat.SodiumShaderInterface;
import net.caffeinemc.mods.sodium.client.gl.buffer.GlMutableBuffer;
import net.caffeinemc.mods.sodium.client.gl.shader.uniform.GlUniformBlock;
import net.caffeinemc.mods.sodium.client.render.chunk.shader.ChunkShaderOptions;
import net.caffeinemc.mods.sodium.client.render.chunk.shader.DefaultShaderInterface;
import net.caffeinemc.mods.sodium.client.render.chunk.shader.ShaderBindingContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DefaultShaderInterface.class)
public class DefaultShaderInterfaceMixin implements SodiumShaderInterface {
    @Unique
    private GlUniformBlock trainmurdermystery$uniformOffsets;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void addUniform(ShaderBindingContext context, ChunkShaderOptions options,
                                CallbackInfo ci) {
        if (IrisHelper.isIrisShaderPackInUse()) {
            return;
        }

        trainmurdermystery$uniformOffsets = context.bindUniformBlock("ubo_SectionOffsets", 1);
    }

    @Override
    public void trainmurdermystery$set(GlMutableBuffer buffer) {
        if (trainmurdermystery$uniformOffsets == null) {
            return;
        }

        trainmurdermystery$uniformOffsets.bindBuffer(buffer);
    }
}
