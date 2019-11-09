package org.oilmod.oilforge.items;



import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.oilmod.api.config.Compound;

import java.util.Map;

public interface ItemStackCreator {
    public ItemStack createStack(Compound compound);
    public ItemStack createStack(Item item, int i, int j, NBTTagCompound tag, Compound mTag);
    public String getInternalName();
}
