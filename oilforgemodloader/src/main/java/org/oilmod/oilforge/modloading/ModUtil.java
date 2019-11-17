package org.oilmod.oilforge.modloading;

import org.oilmod.api.OilMod;

public final class ModUtil extends OilMod.ModHelper {

    //just accessing the protected method
    public static void invokeRegisterItems(OilMod mod) {
        OilMod.ModHelper.invokeRegisterItems(mod);
    }
}
