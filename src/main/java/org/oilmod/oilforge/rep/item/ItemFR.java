package org.oilmod.oilforge.rep.item;

import net.minecraft.item.Item;
import org.oilmod.api.rep.item.ItemRep;

public class ItemFR implements ItemRep {
    private final Item forge;

    public ItemFR(Item forge) {
        this.forge = forge;
    }

    public Item getForge() {
        return forge;
    }

    @Override
    public int getMaxDurability() {
        return forge.getMaxDamage();
    }
}
