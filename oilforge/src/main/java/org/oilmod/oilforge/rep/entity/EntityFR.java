package org.oilmod.oilforge.rep.entity;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import org.apache.commons.lang3.NotImplementedException;
import org.oilmod.api.rep.entity.EntityRep;
import org.oilmod.api.rep.stdimpl.world.LocFactoryImpl;
import org.oilmod.api.rep.world.LocationEntityRep;
import org.oilmod.api.rep.world.LocationRep;
import org.oilmod.api.rep.world.VectorRep;
import org.oilmod.api.rep.world.WorldRep;

import java.lang.reflect.Field;
import java.util.UUID;

import static org.oilmod.oilforge.Util.toOil;

public class EntityFR implements EntityRep {
    private final Entity forge;

    public EntityFR(Entity forge) {
        this.forge = forge;
    }

    public Entity getForge() {
        return forge;
    }

    @Override
    public LocationEntityRep getLocationRep() {
        return toOil(forge.getPositionVector(), forge.rotationYaw, forge.rotationPitch, forge.world);
    }

    @Override
    public void setVelocity(VectorRep velocity) {

        forge.setMotion(velocity.getX(), velocity.getY(), velocity.getZ());
    }

    @Override
    public VectorRep getVelocityRep() {
        Vec3d v = forge.getMotion();
        return LocFactoryImpl.getInstance().createVector(v.x, v.y, v.z);
    }

    @Override
    public boolean isOnGround() {
        return forge.onGround;
    }

    @Override
    public WorldRep getWorld() {
        return toOil(forge.world);
    }

    @Override
    public boolean teleport(LocationRep location) {
        return false;
    }

    private static final Field FIRE_TICKS;
    static {
        try {
            FIRE_TICKS = Entity.class.getDeclaredField("fire");
            FIRE_TICKS.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public int getFireTicks() { //TODO: fix this with mixins
        try {
            return FIRE_TICKS.getInt(forge);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public int getMaxFireTicks() { //TODO remove, not actually supported, bukkit just stupid
        throw new NotImplementedException("method not implementable");
    }

    @Override
    public void setFireTicks(int ticks) {
        forge.setFire(ticks/20);
    }

    @Override
    public void remove() {
        forge.remove();
    }

    @Override
    public boolean isDead() {
        return !forge.isAlive();
    }

    @Override
    public boolean isValid() {
        return !forge.removed;
    }

    @Override
    public float getFallDistance() {
        return 0;
    }

    @Override
    public void setFallDistance(float distance) {
        forge.fallDistance = distance;
    }

    @Override
    public UUID getUniqueId() {
        return forge.getUniqueID();
    }
}
