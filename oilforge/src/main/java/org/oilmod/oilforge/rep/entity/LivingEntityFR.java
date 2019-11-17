package org.oilmod.oilforge.rep.entity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import org.apache.commons.lang3.NotImplementedException;
import org.oilmod.api.rep.entity.EntityLivingRep;

import java.lang.reflect.Field;

public class LivingEntityFR extends LivingEntityBaseFR implements EntityLivingRep {
    public LivingEntityFR(MobEntity forge) {
        super(forge);
    }

    @Override
    public MobEntity getForge() {
        return (MobEntity) super.getForge();
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
