package org.oilmod.oilforge.rep.item;

import net.minecraft.item.Item;
import org.oilmod.api.rep.item.ItemStateRep;

public class ItemStateFR implements ItemStateRep {
    private ItemFR item;
    private short data;


    public ItemStateFR(ItemFR item, short data) {
        this.item = item;
        this.data = data;
    }

    public ItemStateFR(Item i, short data) {
        this(new ItemFR(i), data); //todo mixins store ItemFR
    }

    @Override
    public boolean isReadOnly() {
        return false;
    }

    @Override
    public ItemStateFR copy() {
        return new ItemStateFR(item, data);
    }

    @Override
    public ItemFR getItem() {
        return item;
    }
}
