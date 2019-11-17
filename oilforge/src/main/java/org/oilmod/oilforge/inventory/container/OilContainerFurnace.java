package org.oilmod.oilforge.inventory.container;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.AbstractFurnaceContainer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import org.oilmod.oilforge.inventory.OilInventoryFurnace;
import org.oilmod.oilforge.inventory.container.slot.OilSlotFurnaceOutput;

public class OilContainerFurnace extends AbstractFurnaceContainer {

    public OilContainerFurnace(ContainerType<?> containerTypeIn, IRecipeType<? extends AbstractCookingRecipe> recipeTypeIn, int id, PlayerInventory playerInventory, OilInventoryFurnace furnaceInventory, IIntArray prop) {
        super(containerTypeIn, recipeTypeIn, id,playerInventory, furnaceInventory, prop);
        replaceSlots(playerInventory, furnaceInventory);
    }

    private void replaceSlots(PlayerInventory playerInventory, OilInventoryFurnace furnaceInventory) {
        inventorySlots.set(2, new OilSlotFurnaceOutput(playerInventory.player, furnaceInventory, 2, 116, 35));
    }
}