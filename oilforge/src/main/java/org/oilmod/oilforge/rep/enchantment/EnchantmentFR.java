package org.oilmod.oilforge.rep.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.registry.Registry;
import org.oilmod.api.rep.IKey;
import org.oilmod.api.rep.enchant.EnchantmentRep;
import org.oilmod.api.rep.item.ItemRep;
import org.oilmod.api.rep.item.ItemStateRep;
import org.oilmod.api.rep.itemstack.ItemStackFactory;
import org.oilmod.api.rep.itemstack.ItemStackRep;
import org.oilmod.api.rep.itemstack.state.ItemStackStateRep;
import org.oilmod.oilforge.NMSKeyImpl;
import org.oilmod.oilforge.rep.item.ItemFR;
import org.oilmod.oilforge.rep.itemstack.ItemStackFR;


public class EnchantmentFR implements EnchantmentRep {
    private final Enchantment forge;

    public EnchantmentFR(Enchantment forge) {
        this.forge = forge;
    }

    public Enchantment getForge() {
        return forge;
    }

    @Override
    public IKey getKey() {
        return new NMSKeyImpl(Registry.ENCHANTMENT.getKey(forge));
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
        return forge.canApply(((ItemFR)item).getForge().getDefaultInstance()); //TODO overwrites default logic that specifies same at api level. should have same functionality, add test and if same remove (also test custom enchantments)
    }

    @Override
    public boolean canEnchantItem(ItemRep item, ItemStateRep state) {
        return canEnchantItem(ItemStackFactory.create(item, state));
    }

    @Override
    public boolean canEnchantItem(ItemRep item, ItemStackStateRep stackState) {
        return canEnchantItem(ItemStackFactory.create(item, stackState));
    }
    private boolean canEnchantItem(ItemStackRep stack) {
        return forge.canApply(((ItemStackFR)stack).getForge());
    }
}
