package org.oilmod.oilforge.internaltest.testmod1;

import net.minecraft.init.Items;
import org.oilmod.api.items.OilItem;
import org.oilmod.api.items.OilItemStack;
import org.oilmod.api.items.type.IUnique;
import org.oilmod.api.rep.block.BlockFaceRep;
import org.oilmod.api.rep.entity.EntityHumanRep;
import org.oilmod.api.rep.providers.minecraft.MinecraftItem;
import org.oilmod.api.rep.world.LocationBlockRep;
import org.oilmod.api.util.InteractionResult;
import org.oilmod.api.util.OilKey;

import static org.oilmod.oilforge.Util.toOil;

public class TestItem2 extends OilItem implements IUnique {
    public TestItem2(OilKey key) {
        super(key, MinecraftItem.APPLE, "Test Item 2");
    }

    @Override
    public InteractionResult onItemUseOnBlock(OilItemStack stack, EntityHumanRep human, LocationBlockRep loc, boolean offhand, BlockFaceRep blockFace, float hitX, float hitY, float hitZ) {
        human.setFallDistance(10);
        return InteractionResult.SUCCESS;
    }
}
