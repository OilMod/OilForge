package org.oilmod.oilforge.rep.itemstack.state;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.registries.IRegistryDelegate;
import org.oilmod.api.rep.itemstack.state.ItemStackStateRep;
import org.oilmod.api.rep.states.implapi.StateCollector;

import java.lang.reflect.Field;

public class ItemStackStateCollector extends StateCollector<ItemStackStateRep> {

    private static final Field dField;
    static {
        try {
            dField = ItemStack.class.getDeclaredField("delegate"); //todo just make it public in forge lol
            dField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new IllegalStateException(e);
        }
    }

    private void setItemDelegate(ItemStack stack, IRegistryDelegate<Item> delegate) {
        try {
            dField.set(stack, delegate);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void apply(ItemStackStateRep from, ItemStackStateRep to, boolean additive, boolean force) {
        if (!additive && force) {
            ItemStack toF =((ItemStackStateFR)to).getForgeState();
            ItemStack fromF =((ItemStackStateFR)from).getForgeState();
            setItemDelegate(toF, fromF.getItem().delegate);
            toF.setTag(fromF.getTag());

        }
        super.apply(from, to, additive, force);
    }
}
