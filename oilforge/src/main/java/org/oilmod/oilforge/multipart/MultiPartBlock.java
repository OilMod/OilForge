package org.oilmod.oilforge.multipart;

import codechicken.lib.colour.EnumColour;
import codechicken.lib.raytracer.RayTracer;
import codechicken.lib.util.ItemUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class MultiPartBlock extends Block {


    private static final VoxelShape RENDER_SHAPE = VoxelShapes.empty();


    private static Properties createProperties(Properties properties) {
        properties.hardnessAndResistance(-1.0F, 3600000.0F);
        return properties;
    }

    @Override
    public boolean isTransparent(BlockState state) {
        return true;
    }


    public MultiPartBlock(Properties properties) {
        super(properties);
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
    }

    @Override
    public boolean hasTileEntity(BlockState state) {//todo enumState
        return true;
    }

    @Nullable
    @Override
    public MultiPartTileEntity createTileEntity(BlockState state, IBlockReader world) {
        return MultiPartMod.tileEntity.create();
    }


    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.INVISIBLE;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        TileEntity t = worldIn.getTileEntity(pos);
        if (t instanceof MultiPartTileEntity) {


        }
        return RENDER_SHAPE;
    }

    @Override
    public VoxelShape getRenderShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return RENDER_SHAPE;
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult clientHit) {
        if (world.isRemote) {
            return ActionResultType.SUCCESS;
        }
        TileEntity teLookup = world.getTileEntity(pos);
        if (!(teLookup instanceof MultiPartTileEntity)) {
            return ActionResultType.FAIL;
        }
        MultiPartTileEntity te = (MultiPartTileEntity) teLookup;


        //Normal block trace.
        BlockRayTraceResult hit = RayTracer.retraceBlock(world, player, pos);


        ItemStack item = player.inventory.getCurrentItem();
        IMultiPartBlock block;
        if (!(item.getItem() instanceof BlockItem) || !(((BlockItem) item.getItem()).getBlock() instanceof IMultiPartBlock)) {
            if (hit != null && hit.subHit >= 0) {
                BlockState hitBlock = te.getBlockState();
                return hitBlock.onBlockActivated(world, player, hand, hit);
            }
            return ActionResultType.PASS;
        } else {
            block = (IMultiPartBlock) ((BlockItem) item.getItem()).getBlock();
        }



        if (hit == null) {
            return ActionResultType.FAIL;
        }
        if (hit.subHit == 4) {
            if (player.isCrouching() && owner.getFrequency().hasOwner()) {
                if (!player.abilities.isCreativeMode && !player.inventory.addItemStackToInventory(EnderStorageConfig.personalItem.copy())) {
                    return ActionResultType.FAIL;
                }

                owner.setFreq(owner.getFrequency().copy().setOwner(null));
                return ActionResultType.SUCCESS;
            } else if (!item.isEmpty() && ItemUtils.areStacksSameOrTagged(item, EnderStorageConfig.personalItem)) {
                if (!owner.getFrequency().hasOwner()) {
                    owner.setFreq(owner.getFrequency().copy()//
                            .setOwner(player.getUniqueID())//
                            .setOwnerName(player.getName())//
                    );
                    if (!player.abilities.isCreativeMode) {
                        item.shrink(1);
                    }
                    return ActionResultType.SUCCESS;
                }
            }
        } else if (hit.subHit >= 1 && hit.subHit <= 3) {
            ItemStack item = player.inventory.getCurrentItem();
            if (!item.isEmpty()) {
                EnumColour dye = EnumColour.fromDyeStack(item);
                if (dye != null) {
                    EnumColour[] colours = { null, null, null };
                    if (colours[hit.subHit - 1] == dye) {
                        return ActionResultType.FAIL;
                    }
                    colours[hit.subHit - 1] = dye;
                    owner.setFreq(owner.getFrequency().copy().set(colours));
                    if (!player.abilities.isCreativeMode) {
                        item.shrink(1);
                    }
                    return ActionResultType.FAIL;
                }
            }
        }
        return !player.isCrouching() && owner.activate(player, hit.subHit, hand) ? ActionResultType.SUCCESS : ActionResultType.FAIL;
    }
}
