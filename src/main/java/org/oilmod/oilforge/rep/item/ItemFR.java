package org.oilmod.oilforge.rep.item;

import net.minecraft.item.Item;
import org.oilmod.api.rep.item.ItemRep;
import org.oilmod.api.rep.item.ItemStateRep;

public class ItemFR implements ItemRep {
    private final Item forge;

    public ItemFR(Item forge) {
        this.forge = forge;
    }

    public Item getForge() {
        return forge;
    }

    @Override
    public ItemStateRep getStandardState() {
        return new StandardItemStateFR(this);
    }

    @Override
    public int getMaxDurability() {
        return getForge().getMaxDamage(); //todo fix reprecated (requires itemstack, oilrep api fix)
    }
}
