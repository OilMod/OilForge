package org.oilmod.oilforge.modloader;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.oilmod.api.OilMod;
import org.oilmod.api.unification.Standard;
import org.oilmod.oilforge.OilMain;

@Mod("oiluni")@Mod.EventBusSubscriber
public class OilUniForgeAdapter extends OilModForgeAdapterBase {

    public OilUniForgeAdapter() {
        super(FMLJavaModLoadingContext.get().getModEventBus());
    }

    @Override
    public OilMod getOilMod() {
        return Standard.mod;
    }
}
