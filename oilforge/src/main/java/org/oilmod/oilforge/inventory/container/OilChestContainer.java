package org.oilmod.oilforge.inventory.container;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.network.PacketBuffer;
import org.oilmod.oilforge.inventory.*;
import org.oilmod.oilforge.inventory.container.slot.OilSlot;

import static org.oilmod.oilforge.items.OilForgeItemHelper.replaceSlot;
import static org.oilmod.oilforge.items.OilForgeItemHelper.replaceSlots;

public class OilChestContainer extends ChestContainer implements IOilContainer {
    private IInventory top;

    public OilChestContainer(OilContainerType<OilChestContainer> type, int id, PlayerInventory player, int rows, PacketBuffer buffer) {
        this(type, id, player, new Inventory(9 * rows), rows);
        SetItemFilterPacket packet = SetItemFilterPacket.decode(buffer);
        setItemFilter(packet.itemFilter);
    }

    public OilChestContainer(OilContainerType<OilChestContainer> type, int id, PlayerInventory playerInventoryIn, IInventory inventory, int rows) {
        super(type, id, playerInventoryIn, inventory, rows);
        top = inventory;
        if (inventory instanceof OilIInventory) {
            IItemFilter filter = ((OilIInventory) inventory).getItemFilter();
            setItemFilter(filter);
        }
    }

    @Override
    public void setItemFilter(IItemFilter filter) {
        replaceSlots(inventorySlots, filter, slot -> slot.inventory.equals(top));
    }


}
