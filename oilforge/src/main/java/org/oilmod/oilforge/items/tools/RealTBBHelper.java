package org.oilmod.oilforge.items.tools;

import org.oilmod.api.blocks.BlockType;
import org.oilmod.api.items.OilItemStack;
import org.oilmod.api.items.type.IToolBlockBreaking;
import org.oilmod.api.items.type.ImplementationProvider;
import org.oilmod.api.items.type.TBBType;
import org.oilmod.api.rep.block.BlockFaceRep;
import org.oilmod.api.rep.block.BlockStateRep;
import org.oilmod.api.rep.entity.EntityLivingRep;
import org.oilmod.api.rep.world.LocationBlockRep;
import org.oilmod.api.util.InteractionResult;

public class RealTBBHelper extends TBBType.TBBHelper {
    @Override
    protected void apiInit() {
        
    }

    @Override
    protected void apiPostInit() {

    }

    @Override
    protected TBBType getVanilla(TBBType.TBBEnum tbbEnum) {
        switch (tbbEnum) {
            case PICKAXE:
                return new TBBPickaxe();
            case SHOVEL:
                return new TBBShovel();
            case AXE:
            case SHEARS:
            case SWORD:
            case CUSTOM:
                default:
                    return new TBBType(tbbEnum) {

                        @Override
                        protected boolean canHarvestBlock(IToolBlockBreaking iToolBlockBreaking, OilItemStack oilItemStack, BlockStateRep blockStateRep, BlockType blockType) {
                            return true;
                        }

                        @Override
                        protected float getDestroySpeed(IToolBlockBreaking iToolBlockBreaking, OilItemStack oilItemStack, BlockStateRep blockStateRep, BlockType blockType) {
                            return 34;
                        }

                        @Override
                        protected boolean onEntityHit(IToolBlockBreaking iToolBlockBreaking, OilItemStack oilItemStack, EntityLivingRep entityLivingRep, EntityLivingRep entityLivingRep1) {
                            return false;
                        }

                        @Override
                        protected boolean onBlockDestroyed(IToolBlockBreaking iToolBlockBreaking, OilItemStack oilItemStack, BlockStateRep blockStateRep, LocationBlockRep locationBlockRep, EntityLivingRep entityLivingRep) {
                            return false;
                        }

                        @Override
                        protected InteractionResult onItemUseOnBlock(IToolBlockBreaking iToolBlockBreaking, OilItemStack oilItemStack, EntityLivingRep entityLivingRep, LocationBlockRep locationBlockRep, boolean b, BlockFaceRep blockFaceRep, float v, float v1, float v2) {
                            return InteractionResult.SUCCESS;
                        }

                        @Override
                        protected ImplementationProvider getImplementationProvider() {
                            return ImplementationProvider.TOOL_CUSTOM;
                        }
                    };
        }
    }
}
