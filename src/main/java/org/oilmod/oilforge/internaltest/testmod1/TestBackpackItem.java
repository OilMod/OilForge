package org.oilmod.oilforge.internaltest.testmod1;

import net.minecraft.init.Items;
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

import static org.oilmod.oilforge.Util.toOil;

public class TestBackpackItem extends OilItem implements IUnique {
    public TestBackpackItem(OilKey key) {
        super(key, MinecraftItem.LEATHER, "Backpack");
    }

    @Override
    protected OilItemStack createOilItemStackInstance(NMSItemStack nmsItemStack) {
        return new TestBackpackItemStack(nmsItemStack, this);
    }

    @Override
    public ItemInteractionResult onItemRightClick(OilItemStack stack, WorldRep world, EntityHumanRep human, boolean offhand) {
        human.openInventory(stack.getInventory());
        return new ItemInteractionResult(InteractionResult.SUCCESS, stack);
    }
}
