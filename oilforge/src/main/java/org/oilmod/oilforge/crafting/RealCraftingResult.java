package org.oilmod.oilforge.crafting;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import org.oilmod.api.items.crafting.OilCraftingResult;

import static org.oilmod.oilforge.Util.toForge;
import static org.oilmod.oilforge.Util.toOil;

public class RealCraftingResult {
    private final OilCraftingResult oilCraftingResult;

    public RealCraftingResult(OilCraftingResult oilCraftingResult) {
        this.oilCraftingResult = oilCraftingResult;
    }

    public ItemStack preCraftResult(NonNullList<ItemStack> matrix, boolean shaped, int width, int height) {
        return toForge(oilCraftingResult.preCraftResult(toOil(matrix), shaped, width, height));
    }

    public void craftResult(ItemStack result, NonNullList<ItemStack> matrix, boolean shaped, int width, int height) {
        oilCraftingResult.craftResult(toOil(result), toOil(matrix), shaped, width, height);

    }
}
