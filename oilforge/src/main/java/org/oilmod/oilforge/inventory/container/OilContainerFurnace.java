package org.oilmod.oilforge.inventory.container;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.AbstractFurnaceContainer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import org.oilmod.oilforge.inventory.IItemFilter;
import org.oilmod.oilforge.inventory.OilIInventory;
import org.oilmod.oilforge.inventory.OilInventoryFurnace;
import org.oilmod.oilforge.inventory.container.slot.OilSlot;
import org.oilmod.oilforge.inventory.container.slot.OilSlotFurnaceOutput;

import static org.oilmod.oilforge.items.OilForgeItemHelper.replaceSlot;
import static org.oilmod.oilforge.items.OilForgeItemHelper.replaceSlots;

public class OilContainerFurnace extends AbstractFurnaceContainer implements IOilContainer {
    private IInventory top;

    public OilContainerFurnace(ContainerType<?> containerTypeIn, IRecipeType<? extends AbstractCookingRecipe> recipeTypeIn, int id, PlayerInventory playerInventory) {
        super(containerTypeIn, recipeTypeIn, id, playerInventory);

    }
    public OilContainerFurnace(ContainerType<?> containerTypeIn, IRecipeType<? extends AbstractCookingRecipe> recipeTypeIn, int id, PlayerInventory playerInventory, IInventory inventory, IIntArray prop) {
        super(containerTypeIn, recipeTypeIn, id,playerInventory, inventory, prop);
        top = inventory;
        if (inventory instanceof OilIInventory) {
            IItemFilter filter = ((OilIInventory) inventory).getItemFilter();
            setItemFilter(filter);
        }
    }



    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        if (top instanceof OilIInventory) {
            ((OilIInventory) top).tick(); //to simulate ticks as long as we havent implemented a working ticker
        }
    }

    @Override
    public void setItemFilter(IItemFilter filter) {
        replaceSlots(inventorySlots, filter, slot -> slot.inventory.equals(top));
    }






}