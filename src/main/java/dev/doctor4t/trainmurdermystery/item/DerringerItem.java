package dev.doctor4t.trainmurdermystery.item;

import dev.doctor4t.trainmurdermystery.index.TMMDataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class DerringerItem extends RevolverItem {
    public DerringerItem(Settings settings) {
        super(settings);
    }

    @Override
    public boolean hasBeenUsed(ItemStack stack, PlayerEntity user) {
        return stack.getOrDefault(TMMDataComponentTypes.USED, false);
    }

    @Override
    public float getRange(PlayerEntity user, ItemStack stack) {
        return 7f;
    }
}
