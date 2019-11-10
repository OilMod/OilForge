package org.oilmod.oilforge.items.tools;

import net.minecraft.world.World;
import org.oilmod.api.blocks.BlockType;
import org.oilmod.api.items.OilItem;
import org.oilmod.api.items.OilItemStack;
import org.oilmod.api.items.type.IDurable;
import org.oilmod.api.items.type.IToolBlockBreaking;
import org.oilmod.api.items.type.ImplementationProvider;
import org.oilmod.api.items.type.TBBType;
import org.oilmod.api.rep.block.BlockFaceRep;
import org.oilmod.api.rep.block.BlockStateRep;
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
    protected boolean canHarvestBlock(IToolBlockBreaking iToolBlockBreaking, OilItemStack oilItemStack, BlockStateRep blockStateRep, BlockType blockType) {
        return false;
    }

    @Override
    protected float getDestroySpeed(IToolBlockBreaking iToolBlockBreaking, OilItemStack oilItemStack, BlockStateRep blockStateRep, BlockType blockType) {
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
    protected InteractionResult onItemUseOnBlock(IToolBlockBreaking iToolBlockBreaking, OilItemStack oilItemStack, EntityLivingRep entityLivingRep, LocationBlockRep locationBlockRep, boolean b, BlockFaceRep blockFaceRep, float v, float v1, float v2) {
        return InteractionResult.NONE;
    }

    @Override
    protected boolean onBlockDestroyed(IToolBlockBreaking iToolBlockBreaking, OilItemStack stack, BlockStateRep blockState, LocationBlockRep pos, EntityLivingRep entityLiving) {
        World world = ((WorldFR)pos.getWorld()).getForge();

        OilItem item = stack.getItem();
        if (!world.isRemote &&  item instanceof IDurable && blockState.getBlockHardness(pos) != 0) {
            IDurable durable = (IDurable) item;
            durable.damageItem(stack, blockToolDamage, entityLiving);
        }
        return true;
    }

    @Override
    protected ImplementationProvider getImplementationProvider() {
        return null;
    }
}
