package org.oilmod.oilforge;

import jdk.nashorn.internal.ir.Block;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.NotImplementedException;
import org.oilmod.api.OilMod;
import org.oilmod.api.blocks.IBlockState;
import org.oilmod.api.entity.NMSEntity;
import org.oilmod.api.rep.entity.EntityHumanRep;
import org.oilmod.api.rep.entity.EntityLivingRep;
import org.oilmod.api.rep.entity.EntityRep;
import org.oilmod.api.rep.item.ItemRep;
import org.oilmod.api.rep.itemstack.ItemStackRep;
import org.oilmod.api.rep.world.LocationRep;
import org.oilmod.api.rep.world.WorldRep;
import org.oilmod.api.util.ITicker;
import org.oilmod.api.util.NMSKey;
import org.oilmod.api.util.OilKey;
import org.oilmod.api.util.OilUtil;
import org.oilmod.oilforge.rep.location.WorldFR;

import java.util.List;
import java.util.Random;

public class RealOilUtil  extends OilUtil.UtilImpl {
    @Override
    protected ItemStackRep[] getDrops(Block block) {
        throw new NotImplementedException("soon");
    }

    @Override
    protected ItemStackRep[] getDropsSilktouch(Block block) {
        throw new NotImplementedException("soon");
    }

    @Override
    protected ItemStackRep[] getDropsFortune(Block block, int i) {
        throw new NotImplementedException("soon");
    }

    @Override
    protected ItemStackRep getRandomValidVariation(ItemRep itemRep, Random random) {
        throw new NotImplementedException("soon");
    }

    @Override
    protected boolean canBreak(EntityHumanRep entityHumanRep, Block block) {
        return true; //for events
    }

    @Override
    protected boolean canPlace(EntityHumanRep entityHumanRep, Block block, IBlockState iBlockState, Block block1, ItemStackRep itemStackRep) {
        return true;
    }

    @Override
    protected boolean canMultiPlace(EntityHumanRep entityHumanRep, List<IBlockState> list, Block block, ItemStackRep itemStackRep) {
        return true;
    }

    @Override
    protected <T extends EntityRep> List<T> getNearbyEntities(LocationRep locationRep, LocationRep locationRep1, Class<T> aClass) {
        throw new NotImplementedException("soon");
    }

    @Override
    protected void setLastDamager(EntityLivingRep entityLivingRep, EntityLivingRep entityLivingRep1) {
        throw new NotImplementedException("soon");
    }

    @Override
    protected boolean damageEntity(EntityLivingRep entityLivingRep, double v, EntityLivingRep entityLivingRep1) {
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
