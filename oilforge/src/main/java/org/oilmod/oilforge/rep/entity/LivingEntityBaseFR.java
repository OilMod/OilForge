package org.oilmod.oilforge.rep.entity;

import net.minecraft.entity.LivingEntity;
import org.apache.commons.lang3.NotImplementedException;
import org.oilmod.api.rep.entity.EntityLivingRep;

import java.lang.reflect.Field;

public abstract class LivingEntityBaseFR extends EntityFR implements EntityLivingRep {
    public LivingEntityBaseFR(LivingEntity forge) {
        super(forge);
    }

    @Override
    public LivingEntity getForge() {
        return (LivingEntity) super.getForge();
    }

    @Override
    public int getRemainingAir() {
        return getForge().getAir();
    }

    @Override
    public void setRemainingAir(int ticks) {
        getForge().setAir(ticks);
    }

    @Override
    public int getMaximumAir() {
        return getForge().getMaxAir();
    }

    @Override
    public void setMaximumAir(int ticks) { //todo: remove
        throw new NotImplementedException("not implementable");
    }

    @Override
    public int getMaximumNoDamageTicks() {
        return getForge().hurtResistantTime;
    }

    @Override
    public void setMaximumNoDamageTicks(int ticks) {
        getForge().hurtResistantTime = ticks;
    }

    private static final Field LAST_DAMAGE_AMOUNT;
    static {
        try {
            LAST_DAMAGE_AMOUNT = LivingEntity.class.getDeclaredField("lastDamage");
            LAST_DAMAGE_AMOUNT.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public double getLastDamage() { //TODO: fix this with mixins
        try {
            return LAST_DAMAGE_AMOUNT.getDouble(getForge());
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void setLastDamage(double damage) { //todo: consider removing, given temporary resistance by reducing incoming damage by this amount
        try {
            LAST_DAMAGE_AMOUNT.setDouble(getForge(), damage);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public int getNoDamageTicks() {
        return getForge().hurtResistantTime;
    }

    @Override
    public void setNoDamageTicks(int ticks) {
        getForge().hurtResistantTime = ticks;
    }

    @Override
    public boolean isGliding() {
        return getForge().isElytraFlying();
    }

    @Override
    public void setGliding(boolean gliding) { //todo remove and find more elegant way

    }

    @Override
    public void setCollidable(boolean collidable) {//todo use mixins
        throw new NotImplementedException("not implementable");
    }

    @Override
    public boolean isCollidable() {
        return getForge().canBeCollidedWith();
    }

}
