package dev.doctor4t.trainmurdermystery.mixin.client.self;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.doctor4t.trainmurdermystery.client.TMMClient;
import dev.doctor4t.trainmurdermystery.client.particle.HandParticle;
import dev.doctor4t.trainmurdermystery.item.RevolverItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * @author SkyNotTheLimit
 */
@Environment(EnvType.CLIENT)
@Mixin(RevolverItem.class)
public class RevolverItemMixin {

    @ModifyExpressionValue(method = "use", at = @At(value = "INVOKE", target = "Ldev/doctor4t/trainmurdermystery/item/RevolverItem;createHandParticle()Ldev/doctor4t/trainmurdermystery/client/particle/HandParticle;", remap = false))
    private HandParticle spawnHandParticleOnClient(HandParticle original) {
        TMMClient.handParticleManager.spawn(original);
        return original;
    }
}
