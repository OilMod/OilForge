package org.oilmod.oilforge.modloading;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.javafmlmod.FMLModContainer;

public class OilJavaModLoadingContext {
    private final OilModContainer container;

    OilJavaModLoadingContext(OilModContainer container) {
        this.container = container;
    }

    /**
     * @return The mod's event bus, to allow subscription to Mod specific events
     */
    public IEventBus getModEventBus()
    {
        return container.getEventBus();
    }


    /**
     * Helper to get the right instance from the {@link ModLoadingContext} correctly.
     * @return The FMLJavaMod language specific extension from the ModLoadingContext
     */
    public static OilJavaModLoadingContext get() {
        return ModLoadingContext.get().extension();
    }
}
