package org.oilmod.oilforge.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import org.oilmod.api.blocks.BlockType;

import static org.oilmod.oilforge.block.RealBlockTypeHelper.toForge;

public class RealCustomMaterial extends MaterialWrapper {
    private final BlockType blockType;

    public RealCustomMaterial(BlockType blockType) {
        super(new Material(MaterialColor.ADOBE,
                blockType.isLiquid(),
                blockType.isSolid(),
                blockType.isBuildable(),
                blockType.isOpaque(),
                blockType.isFlammable(),
                blockType.isReplaceable(),
                toForge(blockType.getPistonReaction())));
        this.blockType = blockType;
    }

    public BlockType getBlockType() {
        return blockType;
    }
}
