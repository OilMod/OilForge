package org.oilmod.oilforge.rep.item;

import net.minecraft.item.Item;

public class ItemStateFR extends ItemStateBase {
    private int data;


    public ItemStateFR(ItemFR item, int data) { //todo get rid of data
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
