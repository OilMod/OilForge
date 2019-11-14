package org.oilmod.oilforge.rep.item;

import net.minecraft.item.Item;
import org.oilmod.api.rep.item.ItemStateRep;
import org.oilmod.api.rep.providers.ItemStateProvider;

public class ItemStateFR extends ItemStateBase {
    private int data;


    public ItemStateFR(ItemFR item, int data) {
        super(item);
        this.data = data;
    }

    public ItemStateFR(Item i, int data) {
        this(new ItemFR(i), data); //todo mixins store ItemFR
    }

    @Override
    public boolean isReadOnly() {
        return false;
    }

    @Override
    public int getNMS() {
        return data;
    }

    @Override
    public ItemStateFR copy() {
        return new ItemStateFR(getItem(), data);
    }

}