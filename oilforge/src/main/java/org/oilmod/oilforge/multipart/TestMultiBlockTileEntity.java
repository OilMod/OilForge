package org.oilmod.oilforge.multipart;

import codechicken.lib.colour.EnumColour;
import codechicken.lib.math.MathHelper;
import codechicken.lib.raytracer.IndexedCuboid6;
import codechicken.lib.vec.Rotation;
import codechicken.lib.vec.Translation;
import codechicken.lib.vec.Vector3;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;

public class TestMultiBlockTileEntity extends TileEntity {
    int lastX, lastY;
    Direction.Axis lastAxis;
    EnumColour lastColour;
    private  VoxelShape shape = null;

    public TestMultiBlockTileEntity(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }


    private boolean validateShape() {
        BlockState state = getBlockState();

        int x = state.get(TestMultiBlock.X);
        int y = state.get(TestMultiBlock.Y);
        Direction.Axis axis = state.get(TestMultiBlock.AXIS);
        EnumColour colour = state.get(TestMultiBlock.COLOUR);
        if (lastX != x || lastY != y || lastAxis != axis) {
            return true;
        }
        return shape == null;
    }


    public VoxelShape getShape(ISelectionContext context) {
        BlockState state = getBlockState();
        int x = state.get(TestMultiBlock.X);
        int y = state.get(TestMultiBlock.Y);
        Direction.Axis axis = state.get(TestMultiBlock.AXIS);
        if (shape == null || lastX != x || lastY != y || lastAxis != axis) {
            lastX = x;
            lastY = y;
            lastAxis = axis;


            IndexedCuboid6 cuboid = TestMultiBlock.SHAPE_C.copy();

            if (axis != Direction.Axis.X) {
                cuboid.apply(new Translation(-1/16D, -1/16D, -1/16D));
                cuboid.apply(new Rotation(90 * MathHelper.torad, axis == Direction.Axis.Z?Vector3.Y_NEG:Vector3.Z_POS));
                cuboid.apply(new Translation(1/16D, 1/16D, 1/16D));
            }
            cuboid.apply(new Translation(x/16D, y/16D, 0));
            shape = VoxelShapes.create(cuboid.aabb());//new SubHitVoxelShape(, Collections.singletonList(cuboid));

        }
        return shape;
    }

}
