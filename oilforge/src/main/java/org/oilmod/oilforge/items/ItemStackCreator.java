package org.oilmod.oilforge.items;


import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import org.oilmod.api.config.Compound;

public interface ItemStackCreator {
    ItemStack createStack(Compound compound);
    ItemStack createStack(Item item, int i, int j, CompoundNBT tag, Compound mTag);
    String getInternalName();
}
