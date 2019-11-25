package org.oilmod.oilforge.inventory.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import org.apache.commons.lang3.Validate;
import org.oilmod.oilforge.inventory.*;
import org.oilmod.oilforge.inventory.container.slot.OilSlot;

import static org.oilmod.oilforge.inventory.container.ContainerUtil.GuiSlotSize;
import static org.oilmod.oilforge.items.OilForgeItemHelper.replaceSlots;

public class OilChestLikeContainer extends Container implements IOilContainer {
    private final IInventory top;
    private final PlayerInventory playerInventory;
    private final int numRows;
    private final int numColumns;
    private final boolean customOrdering;
    private IItemFilter itemFilter;

    public OilChestLikeContainer(OilContainerType<OilChestLikeContainer> type, int id, PlayerInventory player, PacketBuffer buffer) {
        this(type, id, player, FlexChestPacket.decode(buffer));
    }
    public OilChestLikeContainer(OilContainerType<OilChestLikeContainer> type, int id, PlayerInventory player, FlexChestPacket info) {
        this(null, type, id, player, new Inventory(info.getSize()), info.rows, info.columns, info.customOrdering);
        itemFilter = info.itemFilterPacket.itemFilter;
        initSlots();
    }

    public OilChestLikeContainer(OilContainerType<OilChestLikeContainer> type, int id, PlayerInventory player, IInventory inventory, int rows, int columns) {
        this(null, type, id, player, inventory, rows, columns, false);
        initSlots();
    }
    private OilChestLikeContainer(Void unused, OilContainerType<OilChestLikeContainer> type, int id, PlayerInventory playerInventory, IInventory inventory, int rows, int columns, boolean customOrdering) {
        super(type, id);
        assertInventorySize(inventory, rows * columns);
        this.top = inventory;
        this.playerInventory = playerInventory;
        this.numRows = rows;
        this.numColumns = columns;
        this.customOrdering = customOrdering;
        inventory.openInventory(playerInventory.player);


        if (inventory instanceof OilIInventory) {
            itemFilter = ((OilIInventory) inventory).getItemFilter();
        }
    }

    protected void initSlots() {
        Validate.notNull(itemFilter, "no itemfilter set. huh?");
        int i = (this.numRows - 4) * GuiSlotSize;
        int xDiffHalf = Math.abs((numColumns-9)*GuiSlotSize/2);

        //whichever taken up less space gets moved in the middle
        int xOff = 8 + (numColumns<9?xDiffHalf:0);
        for(int j = 0; j < this.numRows; ++j) {
            for(int k = 0; k < this.numColumns; ++k) {
                this.addSlot(new OilSlot(top, k + j * numColumns, xOff + k * GuiSlotSize, GuiSlotSize + j * GuiSlotSize, itemFilter));
            }
        }

        xOff = 8 + (numColumns>9?xDiffHalf:0);
        for(int l = 0; l < 3; ++l) {
            for(int j1 = 0; j1 < 9; ++j1) {
                this.addSlot(new Slot(playerInventory, j1 + l * 9 + 9, xOff + j1 * GuiSlotSize, 103 + l * GuiSlotSize + i));
            }
        }

        for(int i1 = 0; i1 < 9; ++i1) {
            this.addSlot(new Slot(playerInventory, i1, xOff + i1 * GuiSlotSize, 161 + i));
        }
    }

    @Override
    public void setItemFilter(IItemFilter filter) {
        replaceSlots(inventorySlots, filter, slot -> slot.inventory.equals(top));
    }



    /**
     * Determines whether supplied player can use this container
     */
    public boolean canInteractWith(PlayerEntity playerIn) {
        return this.top.isUsableByPlayer(playerIn);
    }

    /**
     * Handle when the stack in slot {@code index} is shift-clicked. Normally this moves the stack between the player
     * inventory and the other inventory(s).
     */
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (index < this.numRows * 9) {
                if (!this.mergeItemStack(itemstack1, this.numRows * 9, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, this.numRows * 9, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }

    /**
     * Called when the container is closed.
     */
    public void onContainerClosed(PlayerEntity playerIn) {
        super.onContainerClosed(playerIn);
        this.top.closeInventory(playerIn);
    }

    public int getNumRows() {
        return this.numRows;
    }

    public int getNumColumns() {
        return numColumns;
    }

    public boolean isCustomOrdering() {
        return customOrdering;
    }
}
