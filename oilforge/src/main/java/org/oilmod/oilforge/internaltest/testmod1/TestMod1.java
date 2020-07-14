package org.oilmod.oilforge.internaltest.testmod1;

import org.oilmod.api.OilMod;
import org.oilmod.api.UI.UIRegistry;
import org.oilmod.api.blocks.BlockRegistry;
import org.oilmod.api.crafting.custom.CustomCraftingManager;
import org.oilmod.api.crafting.custom.RecipeBuilder;
import org.oilmod.api.crafting.custom.Transformation;
import org.oilmod.api.inventory.ItemFilterRegistry;
import org.oilmod.api.items.ItemRegistry;
import org.oilmod.api.items.crafting.VanillaMaterialIngredient;
import org.oilmod.api.registry.DeferredObject;
import org.oilmod.api.rep.crafting.*;
import org.oilmod.api.rep.itemstack.ItemStackFactory;
import org.oilmod.api.stateable.complex.ComplexStateTypeRegistry;
import org.oilmod.oilforge.internaltest.testmod1.blocks.TestBlock;
import org.oilmod.oilforge.internaltest.testmod1.blocks.TestBlock2;
import org.oilmod.oilforge.internaltest.testmod1.blocks.TestBlockInventoryType;
import org.oilmod.oilforge.internaltest.testmod1.items.*;
import org.oilmod.oilforge.internaltest.testmod1.ui.UITest;
import org.oilmod.oilforge.internaltest.testmod1.ui.UITestItem;

import static org.oilmod.api.rep.providers.minecraft.MinecraftItem.*;

public class TestMod1 extends OilMod {
    public static final IIngredientCategory TestIngredientCategory = new IIngredientCategory() {};
    public static final IResultCategory TestResultCategory = new IResultCategory() {};
    public static final CustomCraftingManager TestCraftingManager = new CustomCraftingManager(new IIngredientCategory[]{TestIngredientCategory}, new IResultCategory[]{TestResultCategory});
    public static DeferredObject<TestBlockInventoryType> TestBlockInventoryType = DeferredObject.empty();

    @Override
    public void onRegisterItems(ItemRegistry itemRegistry) {
        CustomRecipe recipe = new RecipeBuilder()
                .shaped(TestIngredientCategory, Transformation.Rotation90)
                    .row(new VanillaMaterialIngredient(ARROW.getItem()))
                    .row(new VanillaMaterialIngredient(BEEF.getItem()))
                .ok()
                .results(TestResultCategory, (state, checkState) -> ItemStackFactory.INSTANCE.create(PORKCHOP.getItem(), 2)).build();

        TestCraftingManager.add(recipe);



        itemRegistry.register("testitem1", TestItem1::new);
        itemRegistry.register("testitem2", TestItem2::new);
        itemRegistry.register("testbackpack", TestBackpackItem::new);
        itemRegistry.register("kaban", TestKabanItem::new);
        itemRegistry.register("testportablefurnance", TestPortableFurnaceItem::new);
        itemRegistry.register("testpickaxe", TestPickaxe::new);
        itemRegistry.register("testshovel", TestShovel::new);
        itemRegistry.register("gods_flint", GodsFlintItem::new);
        itemRegistry.register("ui_test", UITestItem::new);
        itemRegistry.register("upgradable_pickaxe", UpgradablePickaxe::new);



    }

    @Override
    protected void onRegisterBlocks(BlockRegistry registry) {
        registry.register("testblock", TestBlock::new);
        registry.register("testblock2", TestBlock2::new);
    }

    @Override
    protected void onRegisterItemFilter(ItemFilterRegistry registry) {
        registry.register("portable_item_filter", ()-> PortableInventoryFilter.INSTANCE);
    }

    @Override
    protected void onRegisterUI(UIRegistry registry) {
        registry.register("ui_test", ()->UITest.INSTANCE);
    }

    @Override
    protected void onRegisterComplexStateType(ComplexStateTypeRegistry registry) {
        TestBlockInventoryType = registry.register("testblockcomplexstate", TestBlockInventoryType::new);
    }
}
