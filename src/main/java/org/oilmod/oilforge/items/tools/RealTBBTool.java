package org.oilmod.oilforge.items.tools;

import net.minecraft.world.World;
import org.oilmod.api.blocks.BlockType;
import org.oilmod.api.blocks.IBlockState;
import org.oilmod.api.items.OilItemStack;
import org.oilmod.api.items.type.IDurable;
import org.oilmod.api.items.type.IToolBlockBreaking;
import org.oilmod.api.items.type.ImplementationProvider;
import org.oilmod.api.items.type.TBBType;
import org.oilmod.api.rep.block.BlockFaceRep;
import org.oilmod.api.rep.entity.EntityHumanRep;
import org.oilmod.api.rep.entity.EntityLivingRep;
import org.oilmod.api.rep.world.LocationBlockRep;
import org.oilmod.api.util.InteractionResult;
import org.oilmod.oilforge.rep.location.WorldFR;

public class RealTBBTool extends TBBType {
    private final int blockToolDamage;
    private final int entityToolDamage;


    protected RealTBBTool(TBBEnum tbbEnum, int blockToolDamage, int entityToolDamage) {
        super(tbbEnum);
        this.blockToolDamage = blockToolDamage;
        this.entityToolDamage = entityToolDamage;
    }

    @Override
    protected boolean canHarvestBlock(IToolBlockBreaking item, IBlockState iBlockState, BlockType blockType) {
        return false;
    }

    @Override
    protected float getDestroySpeed(IToolBlockBreaking item, OilItemStack stack, IBlockState iBlockState, BlockType blockType) {
        return 0;
    }

    @Override
    protected boolean onEntityHit(IToolBlockBreaking item, OilItemStack stack, EntityLivingRep target, EntityLivingRep attacker) {
        if (item instanceof IDurable) {
            IDurable durable = (IDurable) item;
            durable.damageItem(stack, entityToolDamage, attacker);
        }
        return true;
    }

    @Override
    protected boolean onBlockDestroyed(IToolBlockBreaking item, OilItemStack stack, IBlockState iBlockState, LocationBlockRep pos, EntityLivingRep entityLivingRep) {
        World world = ((WorldFR)pos.getWorld()).getForge();

        if (!world.isClientSide &&  item instanceof IDurable && blockState.getBlockHardness(pos) != 0) {
            IDurable durable = (IDurable) item;
            durable.damageItem(stack, blockToolDamage, entityLiving);
        }
        return true;
    }

    @Override
    protected InteractionResult onItemUseOnBlock(IToolBlockBreaking item, OilItemStack stack, EntityHumanRep entityHumanRep, LocationBlockRep locationBlockRep, boolean b, BlockFaceRep blockFaceRep, float v, float v1, float v2) {
        return InteractionResult.NONE;
    }

    @Override
    protected ImplementationProvider getImplementationProvider() {
        return null;
    }
}
