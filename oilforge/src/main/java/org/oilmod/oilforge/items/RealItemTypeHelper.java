package org.oilmod.oilforge.items;

import org.oilmod.api.items.OilItemStack;
import org.oilmod.api.items.type.ItemTypeHelper;
import org.oilmod.api.rep.entity.EntityLivingRep;
import org.oilmod.oilforge.OilMain;
import org.oilmod.oilforge.rep.entity.EntityLivingBaseFR;
import org.oilmod.oilforge.rep.entity.EntityLivingFR;

public class RealItemTypeHelper extends ItemTypeHelper {
    @Override
    public boolean handleDamage(OilItemStack stack, int damage, EntityLivingRep entity) {
        return ((RealItemStack)stack.getNmsItemStack()).handleDamageVanilla(damage, ((EntityLivingBaseFR)entity).getForge());
    }

    @Override
    public void damageItem(OilItemStack stack, int damage, EntityLivingRep entity) {
        ((RealItemStack)stack.getNmsItemStack()).getForgeItemStack().damageItem(damage, ((EntityLivingBaseFR)entity).getForge());
    }

    @Override
    public int getItemDamage(OilItemStack stack) {
        return ((RealItemStack)stack.getNmsItemStack()).getForgeItemStack().getDamage();
    }
}