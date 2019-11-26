package org.oilmod.oilforge.internaltest.testmod1;

import net.minecraft.item.ItemStack;
import org.oilmod.api.UI.UIMPI;
import org.oilmod.api.items.ItemInteractionResult;
import org.oilmod.api.items.NMSItemStack;
import org.oilmod.api.items.OilItem;
import org.oilmod.api.items.OilItemStack;
import org.oilmod.api.items.type.IUnique;
import org.oilmod.api.rep.entity.EntityHumanRep;
import org.oilmod.api.rep.entity.EntityPlayerRep;
import org.oilmod.api.rep.inventory.InventoryRep;
import org.oilmod.api.rep.itemstack.ItemStackRep;
import org.oilmod.api.rep.itemstack.state.Inventory;
import org.oilmod.api.rep.providers.minecraft.MinecraftItem;
import org.oilmod.api.rep.world.WorldRep;
import org.oilmod.api.util.InteractionResult;
import org.oilmod.oilforge.items.RealItemStack;

import static org.oilmod.oilforge.Util.toForge;

public class UITestItem extends OilItem implements IUnique {
    public static UITestItem INSTANCE;
    public UITestItem() {
        super(MinecraftItem.LEATHER, "Backpack");
        INSTANCE = this;
    }

    @Override
    protected OilItemStack createOilItemStackInstance(NMSItemStack nmsItemStack) {
        return new TestBackpackItemStack(nmsItemStack, this, 4*4+1, 1);
    }

    @Override
    public ItemInteractionResult onItemRightClick(OilItemStack stack, WorldRep world, EntityHumanRep human, boolean offhand) {
        UIMPI.openUI((EntityPlayerRep) human, UITest.INSTANCE.create(stack));

        return new ItemInteractionResult(InteractionResult.SUCCESS, stack);
    }
}
