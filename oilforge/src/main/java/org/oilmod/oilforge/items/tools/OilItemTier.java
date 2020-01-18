package org.oilmod.oilforge.items.tools;

import net.minecraft.item.IItemTier;
import net.minecraft.item.crafting.Ingredient;
import org.oilmod.api.items.OilItem;
import org.oilmod.api.items.type.IDurable;
import org.oilmod.api.items.type.IToolBlockBreaking;
import org.oilmod.api.items.type.TBBType;

public class OilItemTier implements IItemTier {
    private final OilItem oilItem;

    public OilItemTier(OilItem oilItem) {
        this.oilItem = oilItem;
    }

    @Override
    public int getMaxUses() {
        return (oilItem instanceof IDurable)?((IDurable) oilItem).getMaxDurability():0;
    }

    @Override
    public float getEfficiency() {
        return (oilItem instanceof IToolBlockBreaking)?((IToolBlockBreaking) oilItem).getDestroySpeed(null):0;
    }

    @Override
    public float getAttackDamage() {
        return 0;
    }

    @Override
    public int getHarvestLevel() {
        return (oilItem instanceof IToolBlockBreaking)?((IToolBlockBreaking) oilItem).getToolStrength(null, TBBType.SHOVEL):0;//this is called by the pickaxe class, we overwrite it
    }

    @Override
    public int getEnchantability() {
        return oilItem.getItemEnchantability();
    }

    @Override
    public Ingredient getRepairMaterial() {
        return null;
    }
}
