package org.oilmod.oilforge.rep.item;

import org.oilmod.api.rep.item.ItemStateRep;
import org.oilmod.api.rep.providers.ItemStateProvider;

public abstract class ItemStateBase implements ItemStateRep {
    protected ItemStateBase(ItemFR item) {
        this.item = item;
    }

    public abstract int getNMS();

    private final ItemFR item;

    @Override
    public ItemStateRep copy() {
        return new ItemStateFR(item, getNMS());
    }

    @Override
    public ItemFR getItem() {
        return item;
    }

    @Override
    public boolean isSimilar(ItemStateProvider itemState) {
        ItemStateBase state = (ItemStateBase)itemState.getProvidedItemState();

        return getItem().isSimilar(state) && getNMS() == state.getNMS();
    }
}
