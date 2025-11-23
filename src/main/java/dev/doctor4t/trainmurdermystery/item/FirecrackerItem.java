package dev.doctor4t.trainmurdermystery.item;

import dev.doctor4t.trainmurdermystery.entity.FirecrackerEntity;
import dev.doctor4t.trainmurdermystery.index.TMMEntities;
import dev.doctor4t.trainmurdermystery.util.AdventureUsable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class FirecrackerItem extends Item implements AdventureUsable {
    public FirecrackerItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(@NotNull ItemUsageContext context) {
        if (!context.getSide().equals(Direction.UP)) {
            return ActionResult.PASS;
        }

        PlayerEntity player = context.getPlayer();
        if (player == null) {
            return ActionResult.PASS;
        }

        World world = player.getWorld();
        if (world.isClient) {
            return ActionResult.SUCCESS;
        }

        FirecrackerEntity firecracker = TMMEntities.FIRECRACKER.create(world);
        if (firecracker == null) {
            return ActionResult.SUCCESS;
        }

        Vec3d spawnPos = context.getHitPos();

        firecracker.setPosition(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
        firecracker.setYaw(player.getHeadYaw());
        world.spawnEntity(firecracker);

        if (!player.isCreative()) {
            player.getStackInHand(context.getHand()).decrement(1);
        }

        return ActionResult.SUCCESS;
    }
}