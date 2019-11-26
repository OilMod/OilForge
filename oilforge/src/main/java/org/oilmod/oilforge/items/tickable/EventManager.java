package org.oilmod.oilforge.items.tickable;


import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod.EventBusSubscriber
public class EventManager {
    public static final Logger LOGGER = LogManager.getLogger();

    @SubscribeEvent
    public static void onContainerOpen(PlayerContainerEvent.Open event) {
        LOGGER.info("PlayerContainerEvent.Open: Container: {}", event.getContainer());
        TickableItemManager.add(event.getPlayer().world, event.getContainer(), ContainerTicker.class, ContainerTicker::new);
    }


    @SubscribeEvent
    public static void onContainerClose(PlayerContainerEvent.Close event) {
        LOGGER.info("PlayerContainerEvent.Close: Container: {}", event.getContainer());
        TickableItemManager.remove(event.getPlayer().world, event.getContainer(), ContainerTicker.class);
    }


    @SubscribeEvent
    public static void onContainerClose(TickEvent.WorldTickEvent event) {
        TickableItemManager.onTick(event.world);
    }
}
