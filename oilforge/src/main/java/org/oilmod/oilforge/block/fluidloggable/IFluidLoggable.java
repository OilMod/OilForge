package org.oilmod.oilforge.block.fluidloggable;

import net.minecraft.block.BlockState;
import net.minecraft.block.IBucketPickupHandler;
import net.minecraft.block.ILiquidContainer;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.FluidState;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;

public interface IFluidLoggable extends IBucketPickupHandler, ILiquidContainer, IWaterLoggable {


    default boolean canContainFluid(IBlockReader worldIn, BlockPos pos, BlockState state, Fluid fluidIn) {
        FluidState fluidState = getFluidState(state);
        return !fluidState.isSource() && fluidIn.getFluid().isEquivalentTo(getFluid());
    }

    default FlowingFluid getFluid() {
        return Fluids.WATER;
    }

    default boolean receiveFluid(IWorld worldIn, BlockPos pos, BlockState state, FluidState fluidStateIn) {
        FluidState fluidState = getFluidState(state);
        if (!fluidState.isSource() && fluidStateIn.getFluid().isEquivalentTo(getFluid())) {
            if (!worldIn.isRemote()) {
                worldIn.setBlockState(pos, setFluidState(state, fluidStateIn), 3);

                worldIn.getPendingFluidTicks().scheduleTick(pos, fluidStateIn.getFluid(), fluidStateIn.getFluid().getTickRate(worldIn));
            }

            return true;
        } else {
            return false;
        }
    }

    default boolean supportsOnlySources() {
        return false;
    }

    FluidState getFluidState(BlockState state);
    default BlockState setFluidState(BlockState state, FluidState fluidState) {
        if (fluidState.isEmpty()) {
            if (!supportsOnlySources()) {
                state = state.with(BlockStateProperties.LEVEL_0_15, 0);
            }
            return state.with(BlockStateProperties.WATERLOGGED, Boolean.valueOf(false));
        } else {
            if (!supportsOnlySources()) {
                state = state.with(BlockStateProperties.LEVEL_0_15, 8 - fluidState.getLevel());
            }
            return state.with(BlockStateProperties.WATERLOGGED, true);
        }
    }



    default Fluid pickupFluid(IWorld worldIn, BlockPos pos, BlockState state) {
        FluidState fluidState = getFluidState(state);

        if (fluidState.isSource()) {
            worldIn.setBlockState(pos, setFluidState(state, Fluids.EMPTY.getDefaultState()), 3);
            return fluidState.getFluid();
        } else {
            return Fluids.EMPTY;
        }
    }
}
