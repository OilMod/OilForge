package org.oilmod.oilforge.rep.itemstack.state;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.registry.Registry;
import org.oilmod.api.rep.enchant.EnchantmentRep;
import org.oilmod.api.rep.itemstack.state.Enchantments;
import org.oilmod.api.rep.itemstack.state.ItemStackStateRep;
import org.oilmod.api.util.ConvertedReadSet;
import org.oilmod.api.util.ReadSet;
import org.oilmod.oilforge.rep.enchantment.EnchantmentFR;

public class EnchantmentHelperFR extends Enchantments.EnchantmentHelper {
    @Override
    protected int getEnchantmentLevel(ItemStackStateRep state, EnchantmentRep ench) {
        ItemStack forgeState = ((ItemStackStateFR)state).getForgeState();
        return EnchantmentHelper.getEnchantmentLevel(((EnchantmentFR)ench).getForge(), forgeState);
    }

    @Override
    protected void addEnchantment(ItemStackStateRep state, EnchantmentRep enchRep, int level, boolean force) {
        ItemStack forgeState = ((ItemStackStateFR)state).getForgeState();
        net.minecraft.enchantment.Enchantment ench = ((EnchantmentFR)enchRep).getForge();

        if (!force || (EnchantmentHelper.areAllCompatibleWith(EnchantmentHelper.getEnchantments(forgeState).keySet(), ench) && ench.getMaxLevel()>=level))
        forgeState.addEnchantment(ench, level); //todo consider checking validity or remobing force altogether, seems like a weird choice anyway
    }

    @Override
    protected int removeEnchantment(ItemStackStateRep state, EnchantmentRep enchRep) {
        ItemStack forgeState = ((ItemStackStateFR)state).getForgeState();
        if (!forgeState.isEnchanted())return 0;
        net.minecraft.enchantment.Enchantment ench = ((EnchantmentFR)enchRep).getForge();
        String id = String.valueOf(Registry.ENCHANTMENT.getKey(ench)); //todo find better way

        ListNBT ListNBT = forgeState.getEnchantmentTagList();
        for (int i = 0; i < ListNBT.size(); ++i) {
            CompoundNBT CompoundNBT = (CompoundNBT) ListNBT.get(i);
            if (CompoundNBT.getString("id").equals(id)) {
                short short0 = CompoundNBT.getShort("lvl");
                ListNBT.remove(i);
                return short0;
            }
        }
        return 0;
    }

    @Override
    protected ReadSet<? extends EnchantmentRep> getEnchantments(ItemStackStateRep state) {
        ItemStack forgeState = ((ItemStackStateFR)state).getForgeState();
        //todo mixins store EnchantmentFR (dont use EnchantmentFR::new)
        return new ConvertedReadSet<>(EnchantmentHelper.getEnchantments(forgeState).keySet(), EnchantmentFR::new, EnchantmentFR::getForge);
    }

    @Override
    protected void removeAll(ItemStackStateRep state) {
        ItemStack forgeState = ((ItemStackStateFR)state).getForgeState();
    }

    @Override
    public boolean isApplicable(ItemStackStateRep state) {
        return state instanceof ItemStackStateFR;
    }

    @Override
    public boolean hasFeature(ItemStackStateRep state) {
        return state instanceof ItemStackStateFR && ((ItemStackStateFR)state).getForgeState().isEnchanted();
    }

    @Override
    public boolean isGeneral() {
        return true;
    }
}
