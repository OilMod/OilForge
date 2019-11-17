package org.oilmod.oilforge.crafting;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import org.oilmod.api.items.crafting.DataHolder;
import org.oilmod.api.items.crafting.OilCraftingIngredient;

import javax.annotation.Nullable;
import java.util.Arrays;

import static org.oilmod.oilforge.Util.toOil;

public class OilIngredient extends Ingredient {
    private  OilCraftingIngredient oil;
    private DataHolder currentDataHolder;

    protected OilIngredient(OilCraftingIngredient oil) {
        super(Arrays.stream(new IItemList[0]));

        this.oil = oil;
    }

    @Override
    public ItemStack[] getMatchingStacks() {
        return super.getMatchingStacks();
    }

    @Override
    public boolean isSimple() {
        return false;
    }

    @Override
    protected void invalidate() {
        super.invalidate();
        currentDataHolder = null;
        oil = null;
    }

    public void setCurrentDataHolder(DataHolder currentDataHolder) {
        this.currentDataHolder = currentDataHolder;
    }

    public DataHolder getCurrentDataHolder() {
        return currentDataHolder;
    }

    @Override
    public boolean test(@Nullable ItemStack stack) {
        return oil.match(toOil(stack),currentDataHolder);
    }
}
