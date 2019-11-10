package org.oilmod.oilforge.rep.itemstack;

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
    public int getEnchantmentLevel(EnchantmentRep ench) {
        return EnchantmentHelper.getEnchantmentLevel(((EnchantmentFR)ench).getForge(), forgeState);
    }

    @Override
    public void addEnchantment(EnchantmentRep ench, int level, boolean force) {
        forgeState.addEnchantment(((EnchantmentFR)ench).getForge(), level); //todo consider checking validity or remobing force altogether, seems like a weird choice anyway
    }

    @Override
    public int removeEnchantment(EnchantmentRep enchRep) {
        if (!forgeState.isEnchanted())return 0;
        Enchantment ench = ((EnchantmentFR)enchRep).getForge();
        String id = String.valueOf((Object) IRegistry.field_212628_q.getKey(ench));

        NBTTagList nbttaglist = forgeState.getEnchantmentTagList();
        for (int i = 0; i < nbttaglist.size(); ++i) {
            NBTTagCompound nbttagcompound = (NBTTagCompound) nbttaglist.get(i);
            if (nbttagcompound.getString("id").equals(id)) {
                short short0 = nbttagcompound.getShort("lvl");
                nbttaglist.remove(i);
                return short0;
            }
        }
        return 0;
    }

    @Override
    public boolean isSimilar(ItemStackStateProvider state) {
        ItemStackStateFR stateFR = (ItemStackStateFR)state.getProvidedItemStackState();
        ItemStack forge2 = stateFR.getForgeState();
        return ItemStack.areItemsEqual(forgeState, forge2);
    }

    @Override
    public ReadSet<EnchantmentFR> getEnchantments() {
        //todo mixins store EnchantmentFR (dont use EnchantmentFR::new)
        return new ConvertedReadSet<>(EnchantmentHelper.getEnchantments(forgeState).keySet(), EnchantmentFR::new, EnchantmentFR::getForge);
    }
}
