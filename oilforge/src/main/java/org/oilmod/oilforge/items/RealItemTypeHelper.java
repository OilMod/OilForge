package org.oilmod.oilforge.items;

import org.oilmod.api.items.OilItemStack;
import org.oilmod.api.items.type.ItemTypeHelper;
import org.oilmod.api.rep.entity.EntityLivingRep;
import org.oilmod.oilforge.rep.entity.LivingEntityBaseFR;
import org.oilmod.oilforge.rep.entity.LivingEntityFR;

public class RealItemTypeHelper extends ItemTypeHelper {
    @Override
    public boolean handleDamage(OilItemStack stack, int damage, EntityLivingRep entity) {
        return ((RealItemStack)stack.getNmsItemStack()).handleDamageVanilla(damage, ((LivingEntityFR)entity).getForge());
    }

    @Override
    public void damageItem(OilItemStack stack, int damage, EntityLivingRep entity) {//todo make use of onbroken lambda, i think we wanted that
        ((RealItemStack)stack.getNmsItemStack()).getForgeItemStack().damageItem(damage, ((LivingEntityBaseFR)entity).getForge(), i -> {});
    }

    @Override
    public int getItemDamage(OilItemStack stack) {
        return ((RealItemStack)stack.getNmsItemStack()).getForgeItemStack().getDamage();
    }
}