package dev.doctor4t.trainmurdermystery.item;

import dev.doctor4t.trainmurdermystery.TMM;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

public class BatItem extends Item {
    public static final Identifier BAT_DEATH_REASON = TMM.id("bat_hit");
    public BatItem(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
    }
}
