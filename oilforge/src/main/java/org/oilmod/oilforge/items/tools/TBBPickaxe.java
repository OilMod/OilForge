package org.oilmod.oilforge.items.tools;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.ItemTool;
import org.oilmod.api.blocks.BlockType;
import org.oilmod.api.items.OilItemStack;
import org.oilmod.api.items.type.IPickaxe;
import org.oilmod.api.items.type.IToolBlockBreaking;
import org.oilmod.api.items.type.ImplementationProvider;
import org.oilmod.api.items.type.TBBType;
import org.oilmod.api.rep.block.BlockFaceRep;
import org.oilmod.api.rep.block.BlockStateRep;
import org.oilmod.api.rep.entity.EntityLivingRep;
import org.oilmod.api.rep.world.LocationBlockRep;
import org.oilmod.api.util.InteractionResult;
import org.oilmod.oilforge.modloader.RealModHelper;
import org.oilmod.oilforge.rep.block.BlockStateFR;

public class TBBPickaxe extends RealTBBTool {
    protected TBBPickaxe() {
        super(TBBType.TBBEnum.PICKAXE, 1, 2, (ItemTool) Items.IRON_PICKAXE);
    }

    @Override
    protected boolean canHarvestBlock(IToolBlockBreaking item, OilItemStack oilItemStack, BlockStateRep blockStateRep, BlockType blockType) {
        IBlockState blockIn = ((BlockStateFR)blockStateRep).getForge();
        IPickaxe pickaxe = (IPickaxe) item;

        //todo change to match for OilMaterials oil tools etc
        Block block = blockIn.getBlock();
        int i = pickaxe.getPickaxeStrength();
        if (blockIn.getHarvestTool() == net.minecraftforge.common.ToolType.PICKAXE) {
            return i >= blockIn.getHarvestLevel();
        }
        Material material = blockIn.getMaterial();
        return material == Material.ROCK || material == Material.IRON || material == Material.ANVIL ||
                (/*!block.isVanilla() && */blockType.breakablePickaxe(blockStateRep));
    }

    @Override
    protected float getDestroySpeed(IToolBlockBreaking item, OilItemStack stack, BlockStateRep blockStateRep, BlockType blockType) {
        IBlockState state = ((BlockStateFR)blockStateRep).getForge();

        //todo change to match for OilMaterials oil tools etc
        Material material = state.getMaterial();
        return material == Material.IRON || material == Material.ANVIL || material == Material.ROCK ||
                (/*!block.isVanilla() && */blockType.breakablePickaxe(blockStateRep))
                ? item.getDestroySpeed(stack) : super.getDestroySpeed(item, stack, blockStateRep, blockType);


    }

    @Override
    protected ImplementationProvider getImplementationProvider() {
        return ImplementationProvider.PICKAXE;
    }
}
