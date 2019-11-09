package org.oilmod.oilforge.items;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.INBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import org.apache.commons.lang3.NotImplementedException;
import org.oilmod.api.config.Compound;
import org.oilmod.api.config.DataList;
import org.oilmod.api.config.DataType;
import org.oilmod.oilforge.config.nbttag.NBTCompound;

import static org.oilmod.oilforge.config.ConfigUtil.copyTo;

public class TempRealItemHelper {
    public static void saveItemsToCompound(Compound compound, NonNullList<ItemStack> items, String key) {
        DataList<Compound> itemsCompoundList = compound.createList(DataType.Subsection);
        for (int i = 0; i < items.size(); ++i) {
            ItemStack item = items.get(i);
            if (!item.isEmpty()) {
                Compound compound1 = compound.createCompound();
                compound1.setByte("Slot", (byte) i); //TODO use mixins

                //temp workaround
                NBTTagCompound itemNBT = new NBTTagCompound();
                item.write(itemNBT);
                NBTCompound oilNBT = new NBTCompound(itemNBT);
                copyTo(oilNBT, compound1);

                //item.save(compound1);
                itemsCompoundList.append(compound1);
                throw new NotImplementedException("todo");
            }
        }
        compound.setList(key, itemsCompoundList);
    }

    public static  void loadItemsFromCompound(Compound compound, NonNullList<ItemStack> items, String key) {
        DataList<Compound> itemsCompoundList = compound.getList(key);
        items.clear();
        for (int i = 0; i < itemsCompoundList.size(); ++i) {
            Compound compound1 = itemsCompoundList.get(i);
            int j = compound1.getByte("Slot") & 255;

            if (j >= 0 && j < items.size()) {
                items.set(j, ItemStack.read((NBTTagCompound) compound1.nbtClone()));
            }
        }
    }
}
