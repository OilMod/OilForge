package org.oilmod.oilforge.modloader;

import org.oilmod.api.OilMod;
import org.oilmod.api.items.ItemRegistry;
import org.oilmod.oilforge.modloading.OilModContext;

public class RealModHelper extends OilMod.ModHelper {
    @Override
    protected void register(OilMod mod) {
        //todo do forge stuff
        super.register(mod);
    }

    @Override
    protected void unregister(OilMod mod) {
        super.unregister(mod);
    }

    @Override
    protected ItemRegistry createItemRegistry(OilMod mod) {
        return super.createItemRegistry(mod);
    }

    public static ItemRegistry getItemRegistry(OilMod mod) {
        return OilMod.ModHelper.getItemRegistry(mod);
    }
    public static void initialise(OilMod mod) {
        OilMod.ModHelper.initialise(mod);
    }

    public static void invokeRegisterItems(OilMod mod) {
        OilMod.ModHelper.invokeRegisterItems(mod);

    }

    @Override
    protected OilMod.ModContext createDefaultContext() {
        return new OilModContext();
    }
}
