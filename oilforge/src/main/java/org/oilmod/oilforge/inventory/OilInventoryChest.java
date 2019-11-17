package org.oilmod.oilforge.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.ItemStack;
import org.oilmod.api.inventory.ModInventoryObject;
import org.oilmod.api.rep.inventory.InventoryHolderRep;

/**
 * Created by sirati97 on 17.01.2016.
 */
public class OilInventoryChest extends OilInventoryBase<ModInventoryObject> {

    public OilInventoryChest(InventoryHolderRep owner, int size, String title, IItemFilter itemFilter) {
        super(owner, title, size, null, itemFilter, true);
    }

    public boolean b(int i, ItemStack itemstack) {
        return true;
    }

    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
        return new ContainerChest(playerInventory, this, playerIn);
    }

    public String getGuiID() {
        return "minecraft:chest";
    }

    //@Override
    //public Container createContainer(PlayerInventory playerinventory, EntityHuman entityhuman) {
    //    return getSize()==5?new OilContainerHopper(entityhuman.inventory, this, entityhuman):new OilContainerChest(entityhuman.inventory, this, entityhuman);
    //}

    //@Override
    //public String getContainerName() {
    //    return getSize()==5?"minecraft:hopper":"minecraft:chest";
    //}
}
