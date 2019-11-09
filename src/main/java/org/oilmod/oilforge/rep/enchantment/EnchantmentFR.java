package org.oilmod.oilforge.rep.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import org.oilmod.api.rep.enchant.EnchantmentRep;
import org.oilmod.api.rep.item.ItemRep;
import org.oilmod.oilforge.rep.item.ItemFR;

public class EnchantmentFR implements EnchantmentRep {
    private final Enchantment forge;

    public EnchantmentFR(Enchantment forge) {
        this.forge = forge;
    }

    public Enchantment getForge() {
        return forge;
    }

    @Override
    public String getName() {
        return forge.getName();
    }

    @Override
    public int getMaxLevel() {
        return forge.getMaxLevel();
    }

    @Override
    public int getStartLevel() {
        return forge.getMinLevel();
    }

    @Override
    public boolean isTreasure() {
        return forge.isTreasureEnchantment();
    }

    @Override
    public boolean isCurse() {
        return forge.isCurse();
    }

    @Override
    public boolean conflictsWith(EnchantmentRep other) {
        return forge.isCompatibleWith(((EnchantmentFR)other).getForge());
    }

    @Override
    public boolean canEnchantItem(ItemRep item) {
        return forge.canApply(((ItemFR)item).getForge().getDefaultInstance()); //TODO fix this, generify
    }
}
