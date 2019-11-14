package org.oilmod.oilforge.rep.itemstack.state;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.registry.IRegistry;
import org.apache.commons.lang3.NotImplementedException;
import org.oilmod.api.rep.enchant.EnchantmentRep;
import org.oilmod.api.rep.item.ItemStateRep;
import org.oilmod.api.rep.itemstack.state.ItemStackStateRep;
import org.oilmod.api.rep.providers.ItemStackStateProvider;
import org.oilmod.api.util.ConvertedReadSet;
import org.oilmod.api.util.ReadSet;
import org.oilmod.oilforge.rep.enchantment.EnchantmentFR;
import org.oilmod.oilforge.rep.item.ItemStateFR;

public class ItemStackStateFR implements ItemStackStateRep {
    private final ItemStack forgeState;

    public ItemStackStateFR(ItemStack forgeState) {
        this.forgeState = forgeState;
    }

    public ItemStack getForgeState() {
        return forgeState;
    }

    @Override
    public ItemStateRep getItemState() {
        return new ItemStateFR(forgeState.getItem(), forgeState.getDamage());
    }

    @Override
    public void applyItemState(ItemStateRep state) {
        throw new NotImplementedException("todo"); //todo
    }

    @Override
    public boolean isAttached() {
        return true;
    }

    @Override
    public void setItemDamage(int itemDamage) {
        forgeState.setDamage(itemDamage);
    }

    public int getItemDamage() {
        return forgeState.getDamage();
    }


    @Override
    public boolean isSimilar(ItemStackStateProvider state) {
        ItemStackStateFR stateFR = (ItemStackStateFR)state.getProvidedItemStackState();
        ItemStack forge2 = stateFR.getForgeState();
        return ItemStack.areItemsEqual(forgeState, forge2);
    }

}