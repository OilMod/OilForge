package org.oilmod.oilforge.items.tools;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemTool;
import net.minecraft.item.ItemUseContext;
import org.oilmod.api.blocks.BlockType;
import org.oilmod.api.items.OilItemStack;
import org.oilmod.api.items.type.IPickaxe;
import org.oilmod.api.items.type.IToolBlockBreaking;
import org.oilmod.api.items.type.ImplementationProvider;
import org.oilmod.api.rep.block.BlockFaceRep;
import org.oilmod.api.rep.block.BlockStateRep;
import org.oilmod.api.rep.entity.EntityLivingRep;
import org.oilmod.api.rep.world.LocationBlockRep;
import org.oilmod.api.util.InteractionResult;
import org.oilmod.oilforge.rep.block.BlockStateFR;

import static org.oilmod.oilforge.Util.toForge;
import static org.oilmod.oilforge.Util.toOil;

public class TBBShovel extends RealTBBTool {
    protected TBBShovel() {
        super(TBBEnum.SHOVEL, 1, 2, (ItemTool) Items.IRON_SHOVEL);
    }

    @Override
    protected boolean canHarvestBlock(IToolBlockBreaking item, OilItemStack oilItemStack, BlockStateRep blockStateRep, BlockType blockType) {
        IBlockState blockIn = ((BlockStateFR)blockStateRep).getForge();

        //todo change to match for OilMaterials oil tools etc
        Block block = blockIn.getBlock();

        Material material = blockIn.getMaterial();
        return material == Material.SNOW ||
                block == Blocks.SNOW || block == Blocks.SNOW_BLOCK ||
                (/*!block.isVanilla() && */blockType.breakableShovel(blockStateRep));
    }

    @Override
    protected InteractionResult onItemUseOnBlock(IToolBlockBreaking item, OilItemStack stack, EntityLivingRep entityLiving, LocationBlockRep loc, boolean offhand, BlockFaceRep face, float hitX, float hitY, float hitZ) {
        //Use mixin to use real shovel method instead directly
        ItemUseContext context = new ItemUseContext((EntityPlayer) toForge(entityLiving), toForge(stack), toForge(loc), toForge(face), hitX, hitY, hitZ);

        //this might actually be a quite elegant way, consider if choosing any specific tier can have an effect tho
        return toOil(Items.IRON_SHOVEL.onItemUse(context));
    }


    @Override
    protected ImplementationProvider getImplementationProvider() {
        return ImplementationProvider.SHOVEL;
    }
}
