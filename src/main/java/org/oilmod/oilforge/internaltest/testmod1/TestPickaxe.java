package org.oilmod.oilforge.internaltest.testmod1;

import net.minecraft.init.Items;
import org.oilmod.api.items.OilItem;
import org.oilmod.api.items.OilItemStack;
import org.oilmod.api.items.type.IDurable;
import org.oilmod.api.items.type.IPickaxe;
import org.oilmod.api.util.OilKey;
import org.oilmod.oilforge.rep.item.ItemFR;

public class TestPickaxe extends OilItem implements IPickaxe, IDurable {
    public TestPickaxe(OilKey key) {
        super(key, new ItemFR(Items.DIAMOND_PICKAXE), "I am so fast it hurts");
    }

    @Override
    public int getPickaxeStrength() {
        return 3;
    }

    @Override
    public float getDestroySpeed(OilItemStack itemStack) {
        return 1;
    }

    @Override
    public int getMaxDurability() {
        return 10;
    }

    @Override
    public int getItemEnchantability() {
        return 22;
    }
}
