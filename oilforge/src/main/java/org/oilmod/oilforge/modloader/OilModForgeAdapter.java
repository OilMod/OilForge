package org.oilmod.oilforge.modloader;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.oilmod.api.OilMod;
import org.oilmod.oilforge.OilMain;

@Mod("oilmod")@Mod.EventBusSubscriber
public class OilModForgeAdapter extends OilModForgeAdapterBase {

    public OilModForgeAdapter() {
        super(FMLJavaModLoadingContext.get().getModEventBus());
    }

    @Override
    public OilMod getOilMod() {
        return OilMain.ModOilMod;
    }
}
