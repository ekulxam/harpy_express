package dev.doctor4t.trainmurdermystery;

import dev.doctor4t.ratatouille.client.util.OptionLocker;
import dev.doctor4t.trainmurdermystery.client.TMMClient;
import eu.midnightdust.lib.config.MidnightConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;

public class TMMConfig extends MidnightConfig {
    @Entry
    public static boolean ultraPerfMode = false;
    @Entry
    public static boolean disableScreenShake = false;

    @Override
    public void writeChanges(String modid) {
        super.writeChanges(modid);

        int lockedRenderDistance = TMMClient.getLockedRenderDistance(ultraPerfMode);
        OptionLocker.overrideOption("renderDistance", lockedRenderDistance);

        GameOptions gameOptions = MinecraftClient.getInstance().options;
        if (gameOptions != null) {
            gameOptions.viewDistance.setValue(lockedRenderDistance);
        }
    }
}
