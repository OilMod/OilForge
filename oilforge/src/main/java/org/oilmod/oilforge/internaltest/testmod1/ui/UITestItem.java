package org.oilmod.oilforge.internaltest.testmod1.ui;

import org.oilmod.api.UI.UIMPI;
import org.oilmod.api.inventory.InventoryFactory;
import org.oilmod.api.inventory.ModInventoryObject;
import org.oilmod.api.items.ItemInteractionResult;
import org.oilmod.api.items.NMSItemStack;
import org.oilmod.api.items.OilItem;
import org.oilmod.api.items.OilItemStack;
import org.oilmod.api.items.type.IUnique;
import org.oilmod.api.rep.entity.EntityHumanRep;
import org.oilmod.api.rep.entity.EntityPlayerRep;
import org.oilmod.api.rep.inventory.InventoryRep;
import org.oilmod.api.rep.providers.minecraft.MinecraftItem;
import org.oilmod.api.rep.world.WorldRep;
import org.oilmod.api.util.InteractionResult;
import org.oilmod.oilforge.internaltest.testmod1.items.PortableInventoryFilter;
import org.oilmod.oilforge.internaltest.testmod1.items.TestBackpackItemStack;

import static org.oilmod.oilforge.Util.toForge;
import static org.oilmod.oilforge.internaltest.testmod1.TestMod1.*;

public class UITestItem extends OilItem implements IUnique {
    public static UITestItem INSTANCE;
    private final InventoryFactory.Builder<ModInventoryObject> invBuilder = InventoryFactory
            .builder("items")
            .standardTitle("UI Test Item")
            .size(4*4+2+2+18)
            .filter(PortableInventoryFilter.INSTANCE)
            .mainInventory()
            .refHolder(TestRefHolder::new)
            .processor(TestCraftingManager, (b)-> b
                    .buildInv(TestRefHolder::craftingGrid).view2d(4, 4) //using variables within builder
                    .ingre(TestRefHolder::craftingGrid, TestIngredientCategory).raw() //reusing previously created
                    .result(TestResultCategory).view1d(16, 2)
                    .overflow(TestResultCategory).view1d(18, 2).make()
                    .reserve(TestIngredientCategory).view1d(20, 18).make()
                    .resultSlot())
            .basic();

    private static class TestRefHolder {
        public void craftingGrid(InventoryRep craftingGrid) {
            this.craftingGrid = craftingGrid;
        }

        public InventoryRep craftingGrid() {
            return craftingGrid;
        }

        InventoryRep craftingGrid;
    }

    public UITestItem() {
        super(MinecraftItem.LEATHER, "UI Test Item");
        INSTANCE = this;


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
