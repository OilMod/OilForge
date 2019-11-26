package org.oilmod.oilforge.rep.itemstack;

import net.minecraft.item.ItemStack;
import org.oilmod.api.items.OilBukkitItemStack;
import org.oilmod.api.items.OilItemStack;
import org.oilmod.oilforge.items.RealItemStack;

public class OilModItemStackFR extends ItemStackFR implements OilBukkitItemStack {
    private final RealItemStack forgeReal;

    public OilModItemStackFR(RealItemStack forge) {
        super(forge.getForgeItemStack());
        this.forgeReal = forge;
    }

    @Override
    public ItemStack getForge() {
        return forgeReal.getForgeItemStack();
    }


    public RealItemStack getForgeReal() {
        return forgeReal;
    }

    @Override
    public OilItemStack getOilItemStack() {
        return forgeReal.getOilItemStack();
    }
}
