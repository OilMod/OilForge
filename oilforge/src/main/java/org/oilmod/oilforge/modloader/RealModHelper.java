package org.oilmod.oilforge.modloader;

import org.oilmod.api.OilMod;
import org.oilmod.api.items.ItemRegistry;
import org.oilmod.oilforge.modloading.ModHelperBase;
import org.oilmod.oilforge.modloading.OilModContext;

public class RealModHelper extends ModHelperBase {
    @Override
    protected void register(OilMod mod) {
        //todo do forge stuff
        super.register(mod);
        initialise(mod);
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


    @Override
    protected OilMod.ModContext createDefaultContext() {
        return new OilModContext();
    }
}