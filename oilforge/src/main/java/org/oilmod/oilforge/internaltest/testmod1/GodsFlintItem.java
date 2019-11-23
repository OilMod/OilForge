package org.oilmod.oilforge.internaltest.testmod1;

import org.oilmod.api.items.OilItem;
import org.oilmod.api.items.OilItemStack;
import org.oilmod.api.items.type.ITool;
import org.oilmod.api.rep.block.BlockFaceRep;
import org.oilmod.api.rep.block.BlockStateRep;
import org.oilmod.api.rep.entity.EntityHumanRep;
import org.oilmod.api.rep.providers.minecraft.MinecraftBlock;
import org.oilmod.api.rep.world.LocationBlockRep;
import org.oilmod.api.util.InteractionResult;
import org.oilmod.api.util.OilKey;


public class GodsFlintItem extends OilItem implements ITool { //Base class for all modded Items (Items=/=ItemStacks)

    public GodsFlintItem() {
        super(MinecraftBlock.STONE.get().getBlock().getItem(), "Godly stick flint");
    }

    @Override
    public int getMaxStackSize() {
        return 2;
    }

    @Override
    public InteractionResult onItemUseOnBlock(OilItemStack stack, EntityHumanRep human, LocationBlockRep loc, boolean offhand, BlockFaceRep blockFace, float hitX, float hitY, float hitZ) {
        LocationBlockRep fireLoc = loc.getRelative(blockFace);

        BlockStateRep block = fireLoc.getBlock();
        if (block.isSame(MinecraftBlock.AIR)) {
            fireLoc.setBlock(MinecraftBlock.FIRE); //will set the clicked block face on fire
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.NONE;
    }
}