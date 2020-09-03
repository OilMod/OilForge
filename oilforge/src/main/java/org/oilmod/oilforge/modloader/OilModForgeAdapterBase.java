package org.oilmod.oilforge.modloader;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.oilmod.oilforge.modloading.OilEvents;

public class OilModForgeAdapterBase extends OilEvents {
    public OilModForgeAdapterBase() {
        this(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public OilModForgeAdapterBase(IEventBus eventbus) {
        super(eventbus);
    }
}
