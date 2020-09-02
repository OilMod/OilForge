package org.oilmod.oilforge.modloader;

import net.minecraftforge.eventbus.api.IEventBus;
import org.oilmod.oilforge.modloading.OilEvents;

public class OilModForgeAdapterBase extends OilEvents {
    public OilModForgeAdapterBase(IEventBus eventbus) {
        super(eventbus);
    }
}
