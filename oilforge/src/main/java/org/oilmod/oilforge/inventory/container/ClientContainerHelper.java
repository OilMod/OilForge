package org.oilmod.oilforge.inventory.container;

import com.google.common.collect.Lists;
import net.minecraft.client.util.RecipeBookCategories;
import net.minecraft.inventory.container.*;

import java.util.List;

public class ClientContainerHelper {


    public static List<RecipeBookCategories> getRecipeBookCategories(OilContainerType containerType) {
        if (containerType == OilContainerType.FURNACE) {
            return Lists.newArrayList(RecipeBookCategories.FURNACE_SEARCH, RecipeBookCategories.FURNACE_FOOD, RecipeBookCategories.FURNACE_BLOCKS, RecipeBookCategories.FURNACE_MISC);
        } else if (containerType == OilContainerType.BLAST_FURNACE) {
            return Lists.newArrayList(RecipeBookCategories.BLAST_FURNACE_SEARCH, RecipeBookCategories.BLAST_FURNACE_BLOCKS, RecipeBookCategories.BLAST_FURNACE_MISC);
        } else if (containerType == OilContainerType.SMOKER) {
            return Lists.newArrayList(RecipeBookCategories.SMOKER_SEARCH, RecipeBookCategories.SMOKER_FOOD);
        } else {
            return Lists.newArrayList(RecipeBookCategories.FURNACE_BLOCKS, RecipeBookCategories.FURNACE_FOOD, RecipeBookCategories.FURNACE_BLOCKS, RecipeBookCategories.FURNACE_MISC, RecipeBookCategories.CRAFTING_EQUIPMENT); //so we dont crash again
        }
    }
}
