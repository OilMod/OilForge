package org.oilmod.oilforge.modloader.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.oilmod.oilforge.inventory.container.OilChestLikeContainer;
import org.oilmod.oilforge.inventory.container.OilContainerType;
import org.oilmod.oilforge.inventory.container.screen.CustomChestScreen;
import org.oilmod.oilforge.inventory.container.screen.CustomUIScreen;
import org.oilmod.oilforge.inventory.container.screen.OilFurnaceScreen;
import org.oilmod.oilforge.ui.container.UIContainer;

import static net.minecraft.client.gui.ScreenManager.registerFactory;


@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber
public class PhysicalClientEvents {
    public static final Logger LOGGER = LogManager.getLogger();

    static {

        registerFactory(OilContainerType.CHESS_LIKE, CustomChestScreen<OilChestLikeContainer>::new);
        registerFactory(OilContainerType.FURNACE, OilFurnaceScreen::new);
        registerFactory(OilContainerType.CUSTOM_UI, CustomUIScreen<UIContainer>::new);
        LOGGER.debug("Registered menus!");
    }
}
