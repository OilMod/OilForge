package org.oilmod.oilforge.internaltest.testmod1;

import org.oilmod.api.items.ItemInteractionResult;
import org.oilmod.api.items.NMSItemStack;
import org.oilmod.api.items.OilItem;
import org.oilmod.api.items.OilItemStack;
import org.oilmod.api.items.type.IUnique;
import org.oilmod.api.rep.entity.EntityHumanRep;
import org.oilmod.api.rep.providers.minecraft.MinecraftItem;
import org.oilmod.api.rep.world.WorldRep;
import org.oilmod.api.util.InteractionResult;
import org.oilmod.api.util.OilKey;

import static org.oilmod.oilforge.Util.toForge;

public class TestPortableFurnaceItem extends OilItem implements IUnique {
    public TestPortableFurnaceItem() {
        super(MinecraftItem.BLAZE_POWDER, "Portable Furnace");
    }

    @Override
    protected OilItemStack createOilItemStackInstance(NMSItemStack nmsItemStack) {
        return new TestPortableFurnaceItemStack(nmsItemStack, this);
    }

    @Override
    public ItemInteractionResult onItemRightClick(OilItemStack stack, WorldRep world, EntityHumanRep human, boolean offhand) {
        human.openInventory(stack.getInventory());


        return new ItemInteractionResult(InteractionResult.SUCCESS, stack);
    }
}
