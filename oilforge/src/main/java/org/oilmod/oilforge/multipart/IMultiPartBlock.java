package org.oilmod.oilforge.multipart;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.extensions.IForgeBlock;

public interface IMultiPartBlock extends IForgeBlock {
    default TileEntity createTileEntity(BlockState state, MultiPartTileEntity multiPart) {
        return createTileEntity(state, (IBlockReader) null);
    }

}
