package org.oilmod.oilforge.block;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import org.apache.commons.lang3.Validate;
import org.oilmod.api.OilMod;
import org.oilmod.api.blocks.BlockRegistry;
import org.oilmod.api.blocks.OilBlock;
import org.oilmod.oilforge.OilModContext;
import org.oilmod.oilforge.rep.item.BlockItemStateFR;

import static org.oilmod.oilforge.Util.toOil;

public class RealBlockRegistry extends BlockRegistry implements IBlockItemRegistry {
    /**
     * Creates new instance of Registry
     *
     * @param mod            associated mod with this item registry
     * @param registryHelper
     */
    protected RealBlockRegistry(OilMod mod, RealBlockRegistryHelper registryHelper) {
        super(mod, registryHelper);
    }

    @Override
    public void registerBlockItems() {
        OilModContext context = (OilModContext) getMod().getContext();
        Validate.notNull(context.itemRegistry, "ItemRegistry not set for modcontext, out of order registration?");

        for(OilBlock oilBlock:getRegistered()) {
            if (!oilBlock.hasCustomAssociatedItem()) {
                Block realBlock = (Block) oilBlock.getNmsBlock();
                BlockItem item = new BlockItem(realBlock, new Item.Properties().group(ItemGroup.DECORATIONS));
                item.setRegistryName(realBlock.getRegistryName());
                context.itemRegistry.register(item);
                oilBlock.setItemFactory(state -> new BlockItemStateFR(toOil(item), toOil(realBlock.getDefaultState()))); //todo state
            }
        }
    }
}
