package org.oilmod.oilforge.items.tickable;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class PlayerTicker extends Ticker<PlayerEntity> {
    @Override
    public void processAll(World w, long now) {
        for(PlayerEntity p:w.getPlayers()) {
            PlayerInventory inv = p.inventory;
            for (int i = 0; i < inv.getSizeInventory(); i++) {
                TickableItemManager.dealWith(inv.getStackInSlot(i), now);
            }
        }
    }
}
