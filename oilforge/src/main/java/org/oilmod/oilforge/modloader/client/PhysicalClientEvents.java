package org.oilmod.oilforge.modloader.client;

import net.minecraft.client.gui.screen.inventory.ChestScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.oilmod.oilforge.inventory.container.OilContainerType;

import static net.minecraft.client.gui.ScreenManager.registerFactory;


@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber
public class PhysicalClientEvents {
    public static final Logger LOGGER = LogManager.getLogger();

    static {

        registerFactory(OilContainerType.GENERIC_9X1, ChestScreen::new);
        registerFactory(OilContainerType.GENERIC_9X2, ChestScreen::new);
        registerFactory(OilContainerType.GENERIC_9X3, ChestScreen::new);
        registerFactory(OilContainerType.GENERIC_9X4, ChestScreen::new);
        registerFactory(OilContainerType.GENERIC_9X5, ChestScreen::new);
        registerFactory(OilContainerType.GENERIC_9X6, ChestScreen::new);
        LOGGER.debug("Registered menus!");
    }
}
