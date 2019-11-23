package org.oilmod.oilforge.modloading;

import org.oilmod.api.OilMod;
import org.oilmod.api.inventory.ItemFilterRegistry;
import org.oilmod.api.items.ItemRegistry;

public final class ModUtil extends OilMod.ModHelper {

    //just accessing the protected method
    public static void invokeRegisterItems(OilMod mod) {
        OilMod.ModHelper.invokeRegister(mod, ItemRegistry.class);
    }
    public static void invokeRegisterItemFilters(OilMod mod) {
        OilMod.ModHelper.invokeRegister(mod, ItemFilterRegistry.class);
    }

    public static void invokeMissingRegistries(OilMod mod) {
        OilMod.ModHelper.invokeMissingRegister(mod);
    }
}
