package org.oilmod.oilforge.inventory.container;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerFurnace;
import org.oilmod.oilforge.inventory.OilInventoryFurnace;
import org.oilmod.oilforge.inventory.container.slot.OilSlotFurnaceOutput;

public class OilContainerFurnace extends ContainerFurnace {

    public OilContainerFurnace(InventoryPlayer playerInventory, OilInventoryFurnace furnaceInventory) {
        super(playerInventory, furnaceInventory);
        replaceSlots(playerInventory, furnaceInventory);
    }

    private void replaceSlots(InventoryPlayer playerInventory, OilInventoryFurnace furnaceInventory) {
        inventorySlots.set(2, new OilSlotFurnaceOutput(playerInventory.player, furnaceInventory, 2, 116, 35));
    }
}