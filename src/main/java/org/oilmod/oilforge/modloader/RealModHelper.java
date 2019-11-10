package org.oilmod.oilforge.modloader;

import org.oilmod.api.OilMod;

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


    public static void initialiseAll() {
        for (OilMod mod:OilMod.getAll()) {
            OilMod.ModHelper.initialise(mod);
        }
    }

    public static void invokeRegisterItemsAll() {
        for (OilMod mod:OilMod.getAll()) {
            OilMod.ModHelper.invokeRegisterItems(mod);
        }
    }
}
