package org.oilmod.oilforge.modloader.client;

import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.oilmod.oilforge.inventory.container.OilChestLikeContainer;
import org.oilmod.oilforge.inventory.container.OilContainerType;
import org.oilmod.oilforge.inventory.container.screen.CustomChestScreen;
import org.oilmod.oilforge.inventory.container.screen.CustomUIScreen;
import org.oilmod.oilforge.inventory.container.screen.OilFurnaceScreen;
import org.oilmod.oilforge.modloader.Setup;
import org.oilmod.oilforge.ui.container.UIContainer;

import static net.minecraft.client.gui.ScreenManager.registerFactory;

public class ClientSetup extends Setup {
    public static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void clientSetup(FMLClientSetupEvent event) {
        registerFactory(OilContainerType.CHESS_LIKE, CustomChestScreen<OilChestLikeContainer>::new);
        registerFactory(OilContainerType.FURNACE, OilFurnaceScreen::new);
        registerFactory(OilContainerType.CUSTOM_UI, CustomUIScreen<UIContainer>::new);
        LOGGER.debug("Registered menus!");
    }
}
