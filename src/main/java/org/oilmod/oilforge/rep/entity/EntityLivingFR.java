package org.oilmod.oilforge.rep.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import org.apache.commons.lang3.NotImplementedException;
import org.oilmod.api.rep.entity.EntityLivingRep;

import java.lang.reflect.Field;

public class EntityLivingFR extends EntityLivingBaseFR {
    public EntityLivingFR(EntityLiving forge) {
        super(forge);
    }

    @Override
    public EntityLiving getForge() {
        return (EntityLiving) super.getForge();
    }
    @Override
    public void setCanPickupItems(boolean pickup) {
        getForge().setCanPickUpLoot(pickup);
    }

    @Override
    public boolean getCanPickupItems() {
        return getForge().canPickUpLoot();
    }

    @Override
    public void setAI(boolean ai) {
        getForge().setNoAI(!ai);
    }

    @Override
    public boolean hasAI() {
        return getForge().isAIDisabled();
    }
}
