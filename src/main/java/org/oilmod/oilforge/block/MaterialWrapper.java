package org.oilmod.oilforge.block;

import net.minecraft.block.material.Material;
import org.oilmod.api.blocks.nms.NMSBlockType;

public class MaterialWrapper implements NMSBlockType {
    private final Material material;

    public MaterialWrapper(Material material) {
        this.material = material;
    }

    public Material getForge() {
        return material;
    }
}
