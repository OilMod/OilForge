package org.oilmod.oilforge.inventory.container;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.AbstractFurnaceContainer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IIntArray;
import org.oilmod.oilforge.inventory.IItemFilter;
import org.oilmod.oilforge.inventory.OilIInventory;

import static org.oilmod.oilforge.items.OilForgeItemHelper.replaceSlots;

public class OilFurnaceContainer extends AbstractFurnaceContainer implements IOilContainer {
    private IInventory top;

    public OilFurnaceContainer(ContainerType<?> containerTypeIn, IRecipeType<? extends AbstractCookingRecipe> recipeTypeIn, int id, PlayerInventory playerInventory, PacketBuffer buffer) {
        super(containerTypeIn, recipeTypeIn, id, playerInventory);

    }
    public OilFurnaceContainer(ContainerType<?> containerTypeIn, IRecipeType<? extends AbstractCookingRecipe> recipeTypeIn, int id, PlayerInventory playerInventory, IInventory inventory, IIntArray prop) {
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