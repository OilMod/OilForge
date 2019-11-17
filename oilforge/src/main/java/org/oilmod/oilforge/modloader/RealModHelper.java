package org.oilmod.oilforge.modloader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.oilmod.api.OilMod;
import org.oilmod.api.items.ItemRegistry;
import org.oilmod.oilforge.OilModContext;

import java.lang.reflect.InvocationTargetException;

public class RealModHelper extends OilMod.ModHelper {
    public static final Logger LOGGER = LogManager.getLogger();

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

    public static void invokeRegisterItems(OilMod mod) {
        OilMod.ModHelper.invokeRegisterItems(mod);
    }

    @Override
    protected OilMod.ModContext createDefaultContext() {
        return new OilModContext();
    }
}
