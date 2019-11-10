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
import org.oilmod.api.util.ConvertedReadSet;
import org.oilmod.api.util.ReadSet;
import org.oilmod.oilforge.rep.enchantment.EnchantmentFR;

public class ItemStackStateFR implements ItemStackStateRep {
    private final ItemStack stack;

    public ItemStackStateFR(ItemStack stack) {
        this.stack = stack;
    }

    @Override
    public ItemStateRep getItemState() {
        throw new NotImplementedException("todo"); //todo
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
        stack.setDamage(itemDamage);
    }

    public int getItemDamage() {
        return stack.getDamage();
    }

    @Override
    public int getEnchantmentLevel(EnchantmentRep ench) {
        return EnchantmentHelper.getEnchantmentLevel(((EnchantmentFR)ench).getForge(), stack);
    }

    @Override
    public void addEnchantment(EnchantmentRep ench, int level, boolean force) {
        stack.addEnchantment(((EnchantmentFR)ench).getForge(), level); //todo consider checking validity or remobing force altogether, seems like a weird choice anyway
    }

    @Override
    public int removeEnchantment(EnchantmentRep enchRep) {
        if (!stack.isEnchanted())return 0;
        Enchantment ench = ((EnchantmentFR)enchRep).getForge();
        String id = String.valueOf((Object) IRegistry.field_212628_q.getKey(ench));

        NBTTagList nbttaglist = stack.getEnchantmentTagList();
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
    public ReadSet<EnchantmentFR> getEnchantments() {
        //todo mixins store EnchantmentFR (dont use EnchantmentFR::new)
        return new ConvertedReadSet<>(EnchantmentHelper.getEnchantments(stack).keySet(), EnchantmentFR::new, EnchantmentFR::getForge);
    }
}
