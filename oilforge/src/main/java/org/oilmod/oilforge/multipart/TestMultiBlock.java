package org.oilmod.oilforge.multipart;

import codechicken.lib.colour.EnumColour;
import codechicken.lib.raytracer.IndexedCuboid6;
import codechicken.lib.render.CCModel;
import codechicken.lib.vec.Cuboid6;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

import static org.oilmod.oilforge.Util.toForge;
import static org.oilmod.oilforge.Util.toOilState;

public class TestMultiBlock extends Block implements IMultiPartBlock {
    public static final IntegerProperty X = IntegerProperty.create("x", 0, 14);
    public static final IntegerProperty Y = IntegerProperty.create("y", 0, 14);
    public static final EnumProperty<Direction.Axis> AXIS = EnumProperty.create("axis", Direction.Axis.class);
    public static final EnumProperty<EnumColour> COLOUR = EnumProperty.create("colour", EnumColour.class);


    public static final IndexedCuboid6 SHAPE_C = new IndexedCuboid6(0, new Cuboid6(0, 0, 0, 8 / 16D, 2 / 16D, 2 / 16D));
    public static final CCModel model = CCModel.quadModel(24).generateBlock(0, SHAPE_C).computeNormals();

    private static final VoxelShape RENDER_SHAPE = VoxelShapes.empty();


    private static Properties createProperties(Properties properties) {
        properties.hardnessAndResistance(-1.0F, 3600000.0F);
        return properties;
    }

    @Override
    public boolean isTransparent(BlockState state) {
        return true;
    }

    @Override
    public boolean isSideInvisible(BlockState state, BlockState adjacentBlockState, Direction side) {
        return false;
    }

    public TestMultiBlock(Properties properties) {
        super(properties);
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(COLOUR, AXIS, X, Y);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {//todo enumState
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {//todo enumState
        return MultiPartMod.testBlockTileEntity.create();
    }


    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.INVISIBLE;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        TileEntity t = worldIn.getTileEntity(pos);
        if (t instanceof TestMultiBlockTileEntity) {
            return ((TestMultiBlockTileEntity) t).getShape(context);
        }
        return RENDER_SHAPE;
    }

    @Override
    public VoxelShape getRenderShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return RENDER_SHAPE;
    }



}
