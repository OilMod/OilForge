package org.oilmod.oilforge.dirtyhacks;

import net.minecraft.item.ItemStack;
import org.oilmod.api.items.OilItemStack;
import org.oilmod.oilforge.items.RealItem;
import org.oilmod.oilforge.items.RealItemStack;

import java.util.WeakHashMap;

//THIS IS A HORRIBLE HACK, BUT APPARENTLY MIXINS DO NOT WORK YET RIP
public final class RealItemStackHelper {
    private static WeakHashMap<ItemStack, RealItemStack> map = new WeakHashMap<>();

    public static void set(ItemStack stack, RealItemStack real) {
        map.put(stack, real);

    }

    public static RealItemStack toReal(ItemStack stack) {
        if (!map.containsKey(stack)) {
            RealItemStack result = new RealItemStack(stack);
            set(stack, result);
            return result;
        }
        return map.get(stack);
    }
}
