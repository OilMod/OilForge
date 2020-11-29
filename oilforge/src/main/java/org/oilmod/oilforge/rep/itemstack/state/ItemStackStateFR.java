package org.oilmod.oilforge.rep.itemstack.state;

import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.NotImplementedException;
import org.oilmod.api.rep.item.ItemStateRep;
import org.oilmod.api.rep.itemstack.state.ItemStackStateRep;
import org.oilmod.api.rep.providers.ItemStackStateProvider;
import org.oilmod.oilforge.Util;
import org.oilmod.oilforge.rep.item.ItemStateFR;
import org.oilmod.oilforge.rep.itemstack.ItemStackFR;

import java.util.Objects;

public class ItemStackStateFR implements ItemStackStateRep {
    private final ItemStack forgeState;

    public ItemStackStateFR(ItemStack forgeState) {
        this.forgeState = forgeState;
    }

    public ItemStack getForgeState() {
        return forgeState;
    }

    @Override
    public ItemStateRep getItemState() {
        return new ItemStateFR(forgeState.getItem(), forgeState.getDamage());
    }

    @Override
    public void applyItemState(ItemStateRep state) {
        throw new NotImplementedException("todo"); //todo
    }

    @Override
    public boolean isAttached() {
        return true;
    }

    @Override
    public void setItemDamage(int itemDamage) {
        forgeState.setDamage(itemDamage);
    }

    public int getItemDamage() {
        return forgeState.getDamage();
    }

    @Override
    public int getMaxStackSize() {
        return forgeState.getMaxStackSize();
    }


    @Override
    public boolean isSimilar(ItemStackStateProvider state) {
        ItemStackStateFR stateFR = (ItemStackStateFR)state.getProvidedItemStackState();
        ItemStack forge2 = stateFR.getForgeState();
        return Util.areItemTagEqual(forgeState, forge2);
    }

    @Override
    public boolean equals(ItemStackStateRep other) {
        return equals((Object)other);
    }

    @Override
    public int getHashCode() {
        return hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemStackStateFR)) return false;
        ItemStackStateFR that = (ItemStackStateFR) o;
        return Util.areItemTagEqual(getForgeState(), that.getForgeState());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getForgeState().getItem(), getForgeState().getTag());
    }
}
