package org.oilmod.oilforge.items.tools;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Items;
import net.minecraft.item.ToolItem;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.Vec3d;
import org.oilmod.api.blocks.BlockType;
import org.oilmod.api.items.OilItemStack;
import org.oilmod.api.items.type.IToolBlockBreaking;
import org.oilmod.api.items.type.ImplementationProvider;
import org.oilmod.api.rep.block.BlockFaceRep;
import org.oilmod.api.rep.block.BlockStateRep;
import org.oilmod.api.rep.entity.EntityLivingRep;
import org.oilmod.api.rep.world.LocationBlockRep;
import org.oilmod.api.util.InteractionResult;
import org.oilmod.oilforge.Util;
import org.oilmod.oilforge.rep.block.BlockStateFR;

import static org.oilmod.oilforge.Util.*;

public class TBBShovel extends RealTBBTool {
    protected TBBShovel() {
        super(TBBEnum.SHOVEL, 1, 2, (ToolItem) Items.IRON_SHOVEL);
    }

    @Override
    protected boolean canHarvestBlock(IToolBlockBreaking item, OilItemStack oilItemStack, BlockStateRep blockStateRep, BlockType blockType) {
        BlockState blockIn = ((BlockStateFR)blockStateRep).getForge();

        //todo change to match for OilMaterials oil tools etc
        Block block = blockIn.getBlock();

        Material material = blockIn.getMaterial();
        return material == Material.SNOW ||
                block == Blocks.SNOW || block == Blocks.SNOW_BLOCK ||
                (/*!block.isVanilla() && */blockType.breakableShovel(blockStateRep));
    }

    @Override
    protected InteractionResult onItemUseOnBlock(IToolBlockBreaking item, OilItemStack stack, EntityLivingRep LivingEntity, LocationBlockRep loc, boolean offhand, BlockFaceRep face, float hitX, float hitY, float hitZ) {
        BlockRayTraceResult rayTraceResult = new BlockRayTraceResult(new Vec3d(hitX, hitY, hitZ), toForge(face), toForge(loc), false); //todo this actually seems to be a useful item, consider copying also the last flag
        PlayerEntity player = (PlayerEntity) toForge(LivingEntity);
        
        //Use mixin to use real shovel method instead directly
        ItemUseContext context = createItemUseContext(toForge(loc.getWorld()), player, offhand? Hand.OFF_HAND:Hand.MAIN_HAND, toForge(stack), rayTraceResult);

        //this might actually be a quite elegant way, consider if choosing any specific tier can have an effect tho
        return toOil(Items.IRON_SHOVEL.onItemUse(context));
    }


    @Override
    protected ImplementationProvider getImplementationProvider() {
        return ImplementationProvider.SHOVEL;
    }
}
