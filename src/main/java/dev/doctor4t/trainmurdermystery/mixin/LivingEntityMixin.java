package dev.doctor4t.trainmurdermystery.mixin;

import dev.doctor4t.trainmurdermystery.index.TMMItems;
import dev.doctor4t.trainmurdermystery.item.KnifeItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvent;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends EntityMixin {

    @Shadow
    public abstract void playSound(@Nullable SoundEvent sound);

    @Inject(method = "tick", at = @At("HEAD"))
    public void addKnockbackWithKnife(CallbackInfo ci) {
        //noinspection ConstantValue
        if (!((Object) this instanceof PlayerEntity player)) {
            return;
        }
        EntityAttributeModifier modifier = new EntityAttributeModifier(KnifeItem.KNOCKBACK_MODIFIER_ID, .5f, EntityAttributeModifier.Operation.ADD_VALUE);
        trainmurdermystery$updateAttribute(player.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_KNOCKBACK), modifier, player.getMainHandStack().isOf(TMMItems.KNIFE));
    }

    @Unique
    private static void trainmurdermystery$updateAttribute(EntityAttributeInstance attributeInstance, EntityAttributeModifier modifier, boolean addOrKeep) {
        if (attributeInstance == null) {
            return;
        }

        boolean alreadyHasModifier = attributeInstance.hasModifier(modifier.id());
        if (addOrKeep && !alreadyHasModifier) {
            attributeInstance.addPersistentModifier(modifier);
        } else if (!addOrKeep && alreadyHasModifier) {
            attributeInstance.removeModifier(modifier);
        }
    }
}
