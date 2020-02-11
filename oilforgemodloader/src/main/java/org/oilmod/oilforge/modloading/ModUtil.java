package org.oilmod.oilforge.modloading;

import org.oilmod.api.OilMod;
import org.oilmod.api.blocks.BlockRegistry;
import org.oilmod.api.inventory.ItemFilterRegistry;
import org.oilmod.api.items.ItemRegistry;
import org.oilmod.api.registry.RegistryBase;

public final class ModUtil extends OilMod.ModHelper<ModUtil> {

    //just accessing the protected method
    public static void invokeRegisterItems(OilMod mod) {
        OilMod.ModHelper.invokeRegister(mod, ItemRegistry.class);
    }
    public static void invokeRegisterItemFilters(OilMod mod) {
        OilMod.ModHelper.invokeRegister(mod, ItemFilterRegistry.class);
    }
    public static void invokeRegisterBlocks(OilMod mod) {
        OilMod.ModHelper.invokeRegister(mod, BlockRegistry.class);
    }


    public static <T extends RegistryBase<?, T, ?, ?>> void invokeRegister(OilMod mod, Class<T> clazz) {
        OilMod.ModHelper.invokeRegister(mod, clazz);
    }

    public static void invokeMissingRegistries(OilMod mod) {
        OilMod.ModHelper.invokeMissingRegister(mod);
    }
}
