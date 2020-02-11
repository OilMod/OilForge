package org.oilmod.oilforge.mixin.transformers;


import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.oilmod.api.rep.entity.EntityRep;
import org.oilmod.oilforge.mixin.IEntityCache;
import org.oilmod.oilforge.rep.entity.EntityFR;
import org.oilmod.oilforge.rep.entity.EntityPlayerFR;
import org.oilmod.oilforge.rep.entity.LivingEntityFR;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.lang.ref.SoftReference;

@Mixin(value = Entity.class)
public abstract class EntityMixin implements IEntityCache {
    private SoftReference<EntityFR> oilEntityRep;


    @Override
    @SuppressWarnings("ConstantConditions")
    public <E extends EntityFR> E getOilEntityRep() {
        EntityFR rep;
        if (oilEntityRep == null || (rep=oilEntityRep.get()) == null) {
            Entity forge = (Entity)(Object)this;
            if (forge instanceof PlayerEntity) {
                rep =new EntityPlayerFR((PlayerEntity) forge);
            } else if (forge instanceof MobEntity) {
                rep =new LivingEntityFR((MobEntity) forge);
            } else {
                rep = new EntityFR(forge);
            }
            oilEntityRep = new SoftReference<>(rep);
        }
        //noinspection unchecked
        return (E)rep;
    }
}
