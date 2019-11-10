package org.oilmod.oilforge.rep.item;

import org.oilmod.api.rep.item.ItemRep;
import org.oilmod.api.rep.item.ItemStateRep;

public abstract class ItemStateBase implements ItemStateRep {
    protected ItemStateBase(ItemFR item) {
        this.item = item;
    }

    public abstract short getNMS();

    private final ItemFR item;

    @Override
    public ItemStateRep copy() {
        return new ItemStateFR(item, getNMS());
    }

    @Override
    public ItemRep getItem() {
        return item;
    }
}
