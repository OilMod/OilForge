package org.oilmod.oilforge.internaltest.testmod1;

import org.oilmod.api.UI.UIMPI;
import org.oilmod.api.crafting.ResultSlotCraftingProcessor;
import org.oilmod.api.inventory.InventoryFactory;
import org.oilmod.api.inventory.ModInventoryObject;
import org.oilmod.api.items.ItemInteractionResult;
import org.oilmod.api.items.NMSItemStack;
import org.oilmod.api.items.OilItem;
import org.oilmod.api.items.OilItemStack;
import org.oilmod.api.items.type.IUnique;
import org.oilmod.api.rep.crafting.IngredientSupplierImpl;
import org.oilmod.api.rep.entity.EntityHumanRep;
import org.oilmod.api.rep.entity.EntityPlayerRep;
import org.oilmod.api.rep.providers.minecraft.MinecraftItem;
import org.oilmod.api.rep.world.WorldRep;
import org.oilmod.api.util.InteractionResult;

import java.util.Collections;

import static org.oilmod.oilforge.Util.toForge;
import static org.oilmod.oilforge.internaltest.testmod1.TestMod1.*;

public class UITestItem extends OilItem implements IUnique {
    public static UITestItem INSTANCE;
    private final InventoryFactory.Builder<ModInventoryObject> invBuilder;

    public UITestItem() {
        super(MinecraftItem.LEATHER, "UI Test Item");
        INSTANCE = this;

        invBuilder = InventoryFactory
                .builder("items")
                .standardTitle("UI Test Item")
                .size(4*4+1)
                .filter(PortableInventoryFilter.INSTANCE)
                .mainInventory()
                .processors((inv)-> new ResultSlotCraftingProcessor(Collections.singletonMap(TestIngredientCategory, inv.createView2d(0, 4, 4)), Collections.singletonMap(TestResultCategory, inv.createView(16, 1)), CraftingManager))
                .basic();
    }

    @Override
    protected OilItemStack createOilItemStackInstance(NMSItemStack nmsItemStack) {
        return new TestBackpackItemStack(nmsItemStack, this, invBuilder);
    }

    @Override
    public ItemInteractionResult onItemRightClick(OilItemStack stack, WorldRep world, EntityHumanRep human, boolean offhand) {
        UIMPI.openUI((EntityPlayerRep) human, UITest.INSTANCE.create(stack));

        return new ItemInteractionResult(InteractionResult.SUCCESS, stack);
    }
}
