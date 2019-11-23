package org.oilmod.oilforge.internaltest.testmod1;

import org.oilmod.api.items.OilItem;
import org.oilmod.api.items.OilItemStack;
import org.oilmod.api.items.type.IDurable;
import org.oilmod.api.items.type.IShovel;
import org.oilmod.api.rep.providers.minecraft.MinecraftItem;
import org.oilmod.api.util.OilKey;

public class TestShovel extends OilItem implements IShovel, IDurable {
    public TestShovel() {
        super(MinecraftItem.CARROT, "I am so fast it hurts");
    }

    @Override
    public float getDestroySpeed(OilItemStack itemStack) {
        return 30;
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
