package org.oilmod.oilforge;

import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.NotImplementedException;
import org.oilmod.api.OilMod;
import org.oilmod.api.entity.NMSEntity;
import org.oilmod.api.rep.block.BlockStateRep;
import org.oilmod.api.rep.entity.EntityHumanRep;
import org.oilmod.api.rep.entity.EntityLivingRep;
import org.oilmod.api.rep.entity.EntityRep;
import org.oilmod.api.rep.item.ItemRep;
import org.oilmod.api.rep.itemstack.ItemStackRep;
import org.oilmod.api.rep.world.LocationBlockRep;
import org.oilmod.api.rep.world.LocationRep;
import org.oilmod.api.rep.world.WorldRep;
import org.oilmod.api.util.ITicker;
import org.oilmod.api.util.NMSKey;
import org.oilmod.api.util.OilKey;
import org.oilmod.api.util.OilUtil;
import org.oilmod.oilforge.rep.location.WorldFR;

import java.util.List;
import java.util.Random;

public class RealOilUtil extends OilUtil.UtilImpl<RealOilUtil> {
    @Override
    protected ItemStackRep[] getDrops(WorldRep worldRep, BlockStateRep blockStateRep) {
        throw new NotImplementedException("soon");
    }

    @Override
    protected ItemStackRep[] getDropsSilktouch(WorldRep worldRep, BlockStateRep blockStateRep) {
        throw new NotImplementedException("soon");
    }

    @Override
    protected ItemStackRep[] getDropsFortune(WorldRep worldRep, BlockStateRep blockStateRep, int i) {
        throw new NotImplementedException("soon");
    }

    @Override
    protected ItemStackRep getRandomValidVariation(ItemRep itemRep, Random random) {
        throw new NotImplementedException("soon");
    }

    @Override
    protected boolean canBreak(EntityHumanRep entityHumanRep, LocationBlockRep locationBlockRep, BlockStateRep blockStateRep) {
        return true; //todo for events
    }

    @Override
    protected boolean canPlace(EntityHumanRep entityHumanRep, LocationBlockRep locationBlockRep, BlockStateRep blockStateRep, LocationBlockRep locationBlockRep1, ItemStackRep itemStackRep) {
        return true; //todo for events
    }

    @Override
    protected boolean canMultiPlace(EntityHumanRep entityHumanRep, List<BlockStateRep> list, LocationBlockRep locationBlockRep, ItemStackRep itemStackRep) {
        return true; //todo for events
    }

    @Override
    protected <T extends EntityRep> List<T> getNearbyEntities(LocationRep locationRep, LocationRep locationRep1, Class<T> aClass) {
        throw new NotImplementedException("soon");
    }

    @Override
    protected void setLastDamager(EntityLivingRep EntityLivingRep, EntityLivingRep EntityLivingRep1) {
        throw new NotImplementedException("soon");
    }

    @Override
    protected boolean damageEntity(EntityLivingRep EntityLivingRep, double v, EntityLivingRep EntityLivingRep1) {
        throw new NotImplementedException("soon");
    }

    @Override
    protected long getWorldTicksPlayed(WorldRep worldRep) {
        return ((WorldFR)worldRep).getForge().getGameTime();
    }

    @Override
    protected Class<? extends NMSEntity> getMappedNMSClass(Class<? extends EntityRep> aClass) {
        throw new NotImplementedException("soon");
    }

    @Override
    protected NMSKey registerOilKey(OilKey oilKey) {
        return new NMSKeyImpl(new ResourceLocation(oilKey.getMod().getInternalName(), oilKey.getKeyString()));
    }

    @Override
    protected void runTask(Runnable runnable) {
        throw new NotImplementedException("soon");
    }

    @Override
    protected void runTaskLater(Runnable runnable, long l) {
        throw new NotImplementedException("soon");
    }

    @Override
    protected ITicker createTicker(OilMod oilMod, WorldRep worldRep, int i, int i1) {
        throw new NotImplementedException("soon");
    }
}
