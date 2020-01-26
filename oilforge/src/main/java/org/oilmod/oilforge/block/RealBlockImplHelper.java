package org.oilmod.oilforge.block;

import net.minecraft.block.BlockState;
import net.minecraftforge.common.extensions.IForgeBlock;
import org.oilmod.api.blocks.OilBlock;
import org.oilmod.oilforge.rep.block.BlockStateFR;

public interface RealBlockImplHelper extends IForgeBlock {


    default BlockState getVanillaFakeBlock() {
        return ((BlockStateFR) getOilBlock().getVanillaBlock().getProvidedBlockState()).getForge();
    }

    OilBlock getOilBlock();

}
