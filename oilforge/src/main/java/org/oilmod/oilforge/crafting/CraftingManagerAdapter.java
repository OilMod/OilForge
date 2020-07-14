package org.oilmod.oilforge.crafting;

import org.oilmod.api.crafting.custom.CustomCraftingManager;
import org.oilmod.api.rep.crafting.IIngredientCategory;
import org.oilmod.api.rep.crafting.IResultCategory;

public class CraftingManagerAdapter extends CustomCraftingManager {
    public CraftingManagerAdapter(IIngredientCategory[] ingredientCategories, IResultCategory[] resultCategories) {
        super(ingredientCategories, resultCategories);
    }
}
