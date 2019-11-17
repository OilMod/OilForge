package org.oilmod.oilforge.items.capability;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.oilmod.oilforge.items.RealItemStack;

public class OilItemStackHandler {
    public static final Logger LOGGER = LogManager.getLogger();
    @CapabilityInject(RealItemStack.class)
    public static Capability<RealItemStack> CAPABILITY = null;

    public static void register() {
        if (isReady()) {
            LOGGER.info("OilItemStackHandler has already been registered. Presumably item got initialised before commonSetup");
            return;
        }
        CapabilityManager.INSTANCE.register(RealItemStack.class, new OilItemStackStorage(), () -> RealItemStack.EMPTY);
    }

    public static boolean isReady() {
        return CAPABILITY != null;
    }
}
