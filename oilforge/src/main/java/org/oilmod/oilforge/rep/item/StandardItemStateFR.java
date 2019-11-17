package org.oilmod.oilforge.rep.item;

public class StandardItemStateFR extends ItemStateBase {
    public StandardItemStateFR(ItemFR item) {
        super(item);
    }

    @Override
    public int getNMS() {
        return 0;
    }

    @Override
    public boolean isReadOnly() {
        return true;
    }
}
