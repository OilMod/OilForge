package org.oilmod.oilforge.items.tools;

import net.minecraftforge.common.ToolType;
import org.oilmod.api.blocks.BlockType;
import org.oilmod.api.items.OilItemStack;
import org.oilmod.api.items.type.IToolBlockBreaking;
import org.oilmod.api.items.type.ItemImplementationProvider;
import org.oilmod.api.items.type.TBBType;
import org.oilmod.api.rep.block.BlockFaceRep;
import org.oilmod.api.rep.block.BlockStateRep;
import org.oilmod.api.rep.entity.EntityLivingRep;
import org.oilmod.api.rep.world.LocationBlockRep;
import org.oilmod.api.util.InteractionResult;
import org.oilmod.api.util.OilKey;

public class RealTBBHelper extends TBBType.Helper<RealTBBHelper> {
    @Override
    protected void apiInit() {
        
    }

    @Override
    protected void apiPostInit() {

    }

    @Override
    protected <T extends TBBType> void onRegister2(OilKey key, TBBType.Registry registry, T entry) {
        super.onRegister2(key, registry, entry);
        if (entry instanceof RealTBBTool) {
            entry.setNMS(((RealTBBTool) entry).getForge());
        } else {
            entry.setNMS(ToolType.get(entry.getOilKey().getKeyString())); //todo shall we consider namespace or not?
        }
    }

    @Override
    protected TBBType getProvider(TBBType.TBBEnum tbbEnum) {
        switch (tbbEnum) {
            case PICKAXE:
                return new TBBPickaxe();
            case SHOVEL:
                return new TBBShovel();
            case NONE:
                return new TBBTypeNone();
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
                        protected boolean onEntityHit(IToolBlockBreaking iToolBlockBreaking, OilItemStack oilItemStack, EntityLivingRep EntityLivingRep, EntityLivingRep EntityLivingRep1) {
                            return false;
                        }

                        @Override
                        protected boolean onBlockDestroyed(IToolBlockBreaking iToolBlockBreaking, OilItemStack oilItemStack, BlockStateRep blockStateRep, LocationBlockRep locationBlockRep, EntityLivingRep EntityLivingRep) {
                            return false;
                        }

                        @Override
                        protected InteractionResult onItemUseOnBlock(IToolBlockBreaking iToolBlockBreaking, OilItemStack oilItemStack, EntityLivingRep EntityLivingRep, LocationBlockRep locationBlockRep, boolean b, BlockFaceRep blockFaceRep, float v, float v1, float v2) {
                            return InteractionResult.SUCCESS;
                        }

                        @Override
                        protected ItemImplementationProvider getImplementationProvider() {
                            return  ItemImplementationProvider.TOOL_CUSTOM.getValue();
                        }
                    };
        }
    }
}
