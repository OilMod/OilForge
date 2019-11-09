package org.oilmod.oilforge.items;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import org.apache.commons.lang3.Validate;
import org.oilmod.api.config.Compound;
import org.oilmod.api.data.IData;
import org.oilmod.api.items.NMSItemStack;
import org.oilmod.api.items.OilItemStack;
import org.oilmod.api.rep.itemstack.ItemStackRep;

import java.util.Map;

import static org.oilmod.oilforge.Util.toOil;

public class RealItemStack implements NMSItemStack {
    private final ItemStack itemStack;
    private final OilItemStack oilItemStack;

    public RealItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.oilItemStack = getItem().getApiItem().createOilStack(this);
    }

    public RealItemImplHelper getItem() {
        return (RealItemImplHelper) itemStack.getItem();
    }

    @Override
    public ItemStackRep asBukkitItemStack() {
        return toOil(itemStack);
    }

    @Override
    public OilItemStack getOilItemStack() {
        return oilItemStack;
    }

    @Override
    public int getDataNMS() {
        return itemStack.getDamage();
    }

    @Override
    public void setDataNMS(int i) {
        itemStack.setDamage(i);
    }

    @Override
    public String getRenameNMS() {
        return itemStack.getDisplayName().getFormattedText();
    }

    @Override
    public void setRenameNMS(String s) {
        itemStack.setDisplayName(new TextComponentString(s));
    }

    @Override
    public boolean isRenamedNMS() {
        return itemStack.hasDisplayName();
    }


    public Compound saveModData(Compound compound) {
        for (Map.Entry<String, IData<?>> entry:oilItemStack.getRegisteredIData().entrySet()) {
            entry.getValue().saveTo(compound, entry.getKey());
        }
        return compound;
    }

    public Compound loadModData(Compound compound) {
        for (Map.Entry<String, IData<?>> entry:oilItemStack.getRegisteredIData().entrySet()) {
            entry.getValue().loadFrom(compound, entry.getKey());
        }
        return compound;
    }
}
