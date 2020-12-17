package org.oilmod.oilforge.block;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.*;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.FluidTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;
import org.oilmod.api.blocks.OilBlock;
import org.oilmod.api.blocks.type.IBlockComplexStateable;
import org.oilmod.api.data.IData;
import org.oilmod.api.inventory.InventoryData;
import org.oilmod.api.stateable.complex.IComplexState;
import org.oilmod.api.stateable.complex.IInventoryState;
import org.oilmod.api.util.InteractionResult;
import org.oilmod.oilforge.NMSKeyImpl;
import org.oilmod.oilforge.block.fluidloggable.IFluidLoggable;
import org.oilmod.oilforge.block.tileentity.RealTileEntity;
import org.oilmod.oilforge.block.tileentity.RealTileEntityType;
import org.oilmod.oilforge.inventory.OilInventoryBase;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

import static org.oilmod.oilforge.Util.*;
import static org.oilmod.oilforge.Util.toForge;
import static org.oilmod.oilforge.block.RealBlockTypeHelper.toForge;
import static org.oilmod.util.LamdbaCastUtils.cast;


public class RealBlock extends Block implements RealBlockImplHelper, IFluidLoggable
{

    private final OilBlock oilBlock;

    private static Properties createProperties(OilBlock block) {
        Properties result=  Properties.create(toForge(block.getBlockType()));
        result.hardnessAndResistance(block.getHardness(), block.getResistance());
        result.harvestLevel(block.getHarvestLevel());
        result.harvestTool((ToolType) block.getHarvestTool().getNMS());
        if (!block.isBlocksMovement()) result.doesNotBlockMovement();
        result.setLightLevel(value ->  block.getLightSourceValue());
        result.slipperiness(block.getSlipperiness());
        return result;
    }

    @Override
    public PushReaction getPushReaction(BlockState state) {
        return toForge(oilBlock.getPistonReaction());
    }

    public RealBlock(OilBlock oilBlock) {
        super(createProperties(oilBlock));
        this.oilBlock = oilBlock;
        setRegistryName(((NMSKeyImpl) oilBlock.getOilKey().getNmsKey()).resourceLocation);
        setDefaultState(this.stateContainer.getBaseState().with(WATERLOGGED, false));
    }

    @Override
    public OilBlock getOilBlock() {
        return oilBlock;
    }



    @Override
    public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
        BlockState state = getVanillaFakeBlock();
        return state.getBlock().getShape(state, p_220053_2_, p_220053_3_, p_220053_4_);
    }

    @Override
    public void onBlockClicked(BlockState state, World world, BlockPos pos, PlayerEntity player) { //todo enumState
        TileEntity tileEntity =  world.getTileEntity(pos);
        getOilBlock().onLeftClickOnBlock(toOilState(state), toOilState(tileEntity), toOil(player), toOil(pos, world));
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) { //todo enumState
        TileEntity tileEntity =  world.getTileEntity(pos);
        Vector3d hitVec = hit.getHitVec();
        return toForge(getOilBlock().onRightClickOnBlock(toOilState(state), toOilState(tileEntity), toOil(player), toOil(pos, world), handIn==Hand.OFF_HAND, toOil(hit.getFace()), (float)hitVec.x, (float)hitVec.y, (float)hitVec.z));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {//todo enumState
        if (getOilBlock() instanceof IBlockComplexStateable) {
            IBlockComplexStateable<?> oilblock = (IBlockComplexStateable<?>) getOilBlock();
            return oilblock.hasComplexState(toOilState(state));
        }
        return false;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {//todo enumState
        if (getOilBlock() instanceof IBlockComplexStateable) {
            IBlockComplexStateable<?> oilblock = (IBlockComplexStateable<?>) getOilBlock();
            IComplexState complexState = oilblock.getDefaultComplexState(toOilState(state)).apply(toOilState(state));
            RealTileEntityType<?> realTileEntityType = (RealTileEntityType<?>) complexState.getComplexStateType().getNms();
            return realTileEntityType.create(cast(complexState));
        }
        return null;
    }

    //<editor-fold desc="Fluid Stuff" defaultstate="collapsed">
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final IntegerProperty LEVEL = BlockStateProperties.LEVEL_0_15;
    private final List<FluidState> fluidStates = new ObjectArrayList<>();
    private FlowingFluid lastFluid; //only for debug purposes

    private List<FluidState> getFluidStates() {
        if (lastFluid != getFluid()) { //so that i can change that during debugging
            lastFluid = getFluid();
            fluidStates.clear();
        }
        if (fluidStates.size() == 0) {
            this.fluidStates.add(getFluid().getStillFluidState(false));

            for (int i = 1; i < 8; ++i)
            {
                this.fluidStates.add(getFluid().getFlowingFluidState(8 - i, false));
            }

            this.fluidStates.add(getFluid().getFlowingFluidState(8, true));
        }
        return fluidStates;
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED, LEVEL);
    }

    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (stateIn.get(WATERLOGGED)) {
            if (stateIn.getFluidState().isSource() || facingState.getFluidState().isSource()) {
                worldIn.getPendingFluidTicks().scheduleTick(currentPos, stateIn.getFluidState().getFluid(), this.getFluid().getTickRate(worldIn));
            }

            return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
        }

        return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    public FlowingFluid getFluid() {
        return Fluids.LAVA;
    }


    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ?getFluidStates().get(Math.min(state.get(LEVEL), 8)) : super.getFluidState(state);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
        getFluidState(state).randomTick(worldIn, pos, random);
        super.randomTick(state, worldIn, pos, random);
    }


    /**
     * Returns whether or not this block is of a type that needs random ticking. Called for ref-counting purposes by
     * ExtendedBlockStorage in order to broadly cull a chunk from the random chunk update list for efficiency's sake.
     */
    public boolean ticksRandomly(BlockState state) {
        return state.getFluidState().ticksRandomly();
    }





    @Override
    public int getLightValue(BlockState state, IBlockReader world, BlockPos pos) {
        FluidState fluidState = getFluidState(state);
        return Math.max(fluidState.getBlockState().getLightValue(), super.getLightValue(state, world, pos));
    }



    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        FluidState newFluidState = newState.getFluidState();
        FluidState oldFluidState = state.getFluidState();
        boolean fluidFlag = false;
        if (newFluidState != oldFluidState && newState.getBlock() instanceof FlowingFluidBlock && canContainFluid(worldIn, pos, state, newFluidState.getFluid())) { //looks like we got replaced by a fluid, but actually we support this fluid so lets not do that
            fluidFlag = true;
        }
        if (state.getBlock() != newState.getBlock()) {
            TileEntity tileentity = worldIn.getTileEntity(pos);
            if (fluidFlag) {
                worldIn.setBlockState(pos, setFluidState(state, newFluidState), 3); //nope we keep being ourselves!
            } else {
                if (tileentity instanceof RealTileEntity && ((RealTileEntity<?>) tileentity).getComplexState() instanceof IInventoryState) {
                    IInventoryState inventoryState = (IInventoryState) ((RealTileEntity<?>) tileentity).getComplexState();
                    for (IData<?> data:inventoryState.getRegisteredIData().values()) {
                        if (data instanceof InventoryData) {
                            OilInventoryBase<?> inventory = (OilInventoryBase<?>) data.getData();
                            inventory.dropInventory(worldIn, pos);
                        }
                    }
                    worldIn.updateComparatorOutputLevel(pos, this);
                }

                super.onReplaced(state, worldIn, pos, newState, isMoving);
            }
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockState blockState = super.getStateForPlacement(context);

        BlockPos blockpos = context.getPos();
        FluidState ifluidstate = context.getWorld().getFluidState(context.getPos());
        if (this.canContainFluid(context.getWorld(), blockpos, blockState, ifluidstate.getFluid())) {
            blockState = setFluidState(blockState, ifluidstate);
        }
        return blockState;
    }






    //</editor-fold>
}

