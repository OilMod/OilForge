package org.oilmod.oilforge.items.capability;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import org.oilmod.oilforge.items.RealItemStack;

public class OilItemStackHandler {
    @CapabilityInject(RealItemStack.class)
    public static Capability<RealItemStack> CAPABILITY = null;

    public static void register() {
        CapabilityManager.INSTANCE.register(RealItemStack.class, new OilItemStackStorage(), () -> RealItemStack.EMPTY);
    }
}
