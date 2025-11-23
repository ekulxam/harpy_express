package dev.doctor4t.trainmurdermystery.util;

import dev.doctor4t.trainmurdermystery.TMM;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public class ShopEntry {
    private final ItemStack stack;
    private final int price;
    private final Type type;

    @SuppressWarnings("ClassCanBeRecord")
    public static class Type {
        public static final Type WEAPON = new Type("gui/shop_slot_weapon");
        public static final Type POISON = new Type("gui/shop_slot_poison");
        public static final Type TOOL = new Type("gui/shop_slot_tool");

        private final Identifier texture;

        public Type(String path) {
            this(TMM.id(path));
        }

        public Type(Identifier texture) {
            this.texture = texture;
        }

        public Identifier getTexture() {
            return this.texture;
        }
    }

    public ShopEntry(ItemStack stack, int price, Type type) {
        this.stack = stack;
        this.price = price;
        this.type = type;
    }

    public boolean onBuy(@NotNull PlayerEntity player) {
        return insertStackInFreeSlot(player, this.stack.copy());
    }

    public static boolean insertStackInFreeSlot(@NotNull PlayerEntity player, ItemStack stackToInsert) {
        for (int i = 0; i < 9; i++) {
            ItemStack stack = player.getInventory().getStack(i);
            if (stack.isEmpty()) {
                player.getInventory().setStack(i, stackToInsert);
                return true;
            }
        }
        return false;
    }

    public ItemStack stack() {
        return this.stack;
    }

    public int price() {
        return this.price;
    }

    public Type type() {
        return this.type;
    }
}