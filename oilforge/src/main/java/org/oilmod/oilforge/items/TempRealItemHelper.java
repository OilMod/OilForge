package org.oilmod.oilforge.items;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.oilmod.api.config.Compound;
import org.oilmod.api.config.DataList;
import org.oilmod.api.config.DataType;
import org.oilmod.oilforge.config.nbttag.OilNBTCompound;

import java.util.Optional;

import static org.oilmod.oilforge.config.ConfigUtil.copyTo;

public class TempRealItemHelper {
    public static final Logger LOGGER = LogManager.getLogger();
    public static void saveItemsToCompound(Compound compound, NonNullList<ItemStack> items, String key) {
        if (items.stream().allMatch(ItemStack::isEmpty)) return;

        DataList<Compound> itemsCompoundList = compound.createList(DataType.Subsection);
        for (int i = 0; i < items.size(); ++i) {
            ItemStack item = items.get(i);
            if (!item.isEmpty()) {
                Compound compound1 = compound.createCompound();
                compound1.setByte("Slot", (byte) i); //TODO use mixins

                //temp workaround
                CompoundNBT itemNBT = new CompoundNBT();
                item.write(itemNBT);
                OilNBTCompound oilNBT = new OilNBTCompound(itemNBT);
                copyTo(oilNBT, compound1); //todo should be doable as a itemsCompoundList.append(compound1) even if not of same compound config type (on the fly detection and translation)

                //item.save(compound1);
                itemsCompoundList.append(compound1);
            }
        }
        compound.setList(key, itemsCompoundList);
    }

    public static  void loadItemsFromCompound(Compound compound, NonNullList<ItemStack> items, String key) {
        Optional<ItemStack> find = items.stream().filter(i->!i.isEmpty()).findAny();
        if (find.isPresent()) {
            LOGGER.warn("found {} before deserialization!",find::get);
        }
        items.clear();
        if (!compound.containsKey(key)) {
            return;
        }
        DataList<Compound> itemsCompoundList = compound.getList(key);
        for (int i = 0; i < itemsCompoundList.size(); ++i) {
            Compound compound1 = itemsCompoundList.get(i);
            int j = compound1.getByte("Slot") & 255;

            if (j >= 0 && j < items.size()) {
                items.set(j, ItemStack.read((CompoundNBT) compound1.nbtClone()));
            }
        }
    }
}
