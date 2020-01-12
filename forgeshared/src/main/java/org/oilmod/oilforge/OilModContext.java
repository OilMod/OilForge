package org.oilmod.oilforge;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;
import org.oilmod.api.OilMod;

public class OilModContext implements OilMod.ModContext {
    public IForgeRegistry<Item> itemRegistry;
    public IForgeRegistry<Block> blockRegistry;
}
