package org.oilmod.oilforge.internaltest.testmod1;

import org.oilmod.api.OilMod;
import org.oilmod.api.UI.UIRegistry;
import org.oilmod.api.crafting.custom.CustomCraftingManager;
import org.oilmod.api.crafting.custom.RecipeBuilder;
import org.oilmod.api.crafting.custom.Transformation;
import org.oilmod.api.inventory.ItemFilterRegistry;
import org.oilmod.api.items.ItemRegistry;
import org.oilmod.api.items.crafting.VanillaMaterialIngredient;
import org.oilmod.api.rep.crafting.*;
import org.oilmod.api.rep.itemstack.ItemStackFactory;

import static org.oilmod.api.rep.providers.minecraft.MinecraftItem.*;

public class TestMod1 extends OilMod {
    public static final IIngredientCategory TestIngredientCategory = new IIngredientCategory() {};
    public static final IResultCategory TestResultCategory = new IResultCategory() {};
    public static final CustomCraftingManager CraftingManager = new CustomCraftingManager(new IIngredientCategory[]{TestIngredientCategory}, new IResultCategory[]{TestResultCategory});


    @Override
    public void onRegisterItems(ItemRegistry itemRegistry) {
        CustomRecipe recipe = new RecipeBuilder()
                .shaped(TestIngredientCategory, Transformation.Rotation90)
                    .row(new VanillaMaterialIngredient(ARROW.getItem()))
                    .row(new VanillaMaterialIngredient(BEEF.getItem()))
                .ok()
                .results(TestResultCategory, (state, checkState) -> ItemStackFactory.INSTANCE.create(PORKCHOP.getItem(), 10)).build();

        CraftingManager.add(recipe);



        itemRegistry.register("testitem1", new TestItem1());
        itemRegistry.register("testitem2", new TestItem2());
        itemRegistry.register("testbackpack", new TestBackpackItem());
        itemRegistry.register("kaban", new TestKabanItem());
        itemRegistry.register("testportablefurnance", new TestPortableFurnaceItem());
        itemRegistry.register("testpickaxe", new TestPickaxe());
        itemRegistry.register("testshovel", new TestShovel());
        itemRegistry.register("gods_flint", new GodsFlintItem());
        itemRegistry.register("ui_test", new UITestItem());
        itemRegistry.register("upgradable_pickaxe", new UpgradablePickaxe());



    }

    @Override
    protected void onRegisterItemFilter(ItemFilterRegistry registry) {
        registry.register("portable_item_filter", PortableInventoryFilter.INSTANCE);
    }

    @Override
    protected void onRegisterUI(UIRegistry registry) {
        registry.register("ui_test", UITest.INSTANCE);
    }

}
