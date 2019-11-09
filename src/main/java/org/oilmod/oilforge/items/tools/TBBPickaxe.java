package org.oilmod.oilforge.items.tools;

import org.oilmod.api.blocks.BlockType;
import org.oilmod.api.blocks.IBlockState;
import org.oilmod.api.items.OilItemStack;
import org.oilmod.api.items.type.IToolBlockBreaking;
import org.oilmod.api.items.type.ImplementationProvider;
import org.oilmod.api.items.type.TBBType;
import org.oilmod.api.rep.block.BlockFaceRep;
import org.oilmod.api.rep.entity.EntityHumanRep;
import org.oilmod.api.rep.entity.EntityLivingRep;
import org.oilmod.api.rep.world.LocationBlockRep;
import org.oilmod.api.util.InteractionResult;

public class TBBPickaxe extends TBBType {
    protected TBBPickaxe(TBBEnum tbbEnum) {
        super(tbbEnum);
    }

    @Override
    protected boolean canHarvestBlock(IToolBlockBreaking iToolBlockBreaking, IBlockState iBlockState, BlockType blockType) {
        return false;
    }

    @Override
    protected float getDestroySpeed(IToolBlockBreaking iToolBlockBreaking, OilItemStack oilItemStack, IBlockState iBlockState, BlockType blockType) {
        return 0;
    }

    @Override
    protected boolean onEntityHit(IToolBlockBreaking iToolBlockBreaking, OilItemStack oilItemStack, EntityLivingRep entityLivingRep, EntityLivingRep entityLivingRep1) {
        return false;
    }

    @Override
    protected boolean onBlockDestroyed(IToolBlockBreaking iToolBlockBreaking, OilItemStack oilItemStack, IBlockState iBlockState, LocationBlockRep locationBlockRep, EntityLivingRep entityLivingRep) {
        return false;
    }

    @Override
    protected InteractionResult onItemUseOnBlock(IToolBlockBreaking iToolBlockBreaking, OilItemStack oilItemStack, EntityHumanRep entityHumanRep, LocationBlockRep locationBlockRep, boolean b, BlockFaceRep blockFaceRep, float v, float v1, float v2) {
        return null;
    }

    @Override
    protected ImplementationProvider getImplementationProvider() {
        return null;
    }
}
