package org.oilmod.oilforge.internaltest.testmod1;

import net.minecraft.init.Items;
import org.oilmod.api.items.OilItem;
import org.oilmod.api.items.OilItemStack;
import org.oilmod.api.items.type.IDurable;
import org.oilmod.api.items.type.IPickaxe;
import org.oilmod.api.rep.entity.EntityLivingRep;
import org.oilmod.api.rep.itemstack.ItemStackRep;
import org.oilmod.api.rep.providers.minecraft.MinecraftItem;
import org.oilmod.api.util.OilKey;
import org.oilmod.oilforge.OilMain;
import org.oilmod.oilforge.rep.item.ItemFR;

public class TestPickaxe extends OilItem implements IPickaxe, IDurable {
    public TestPickaxe(OilKey key) {
        super(key, MinecraftItem.DIAMOND_PICKAXE, "I am so fast it hurts");
    }

    @Override
    public int getPickaxeStrength() {
        return 3;
    }

    @Override
    public float getDestroySpeed(OilItemStack itemStack) {
        return 10;
    }

    @Override
    public int getMaxDurability() {
        return 17;
    }

    @Override
    public int getItemEnchantability() {
        return 22;
    }

    @Override
    public void damageItem(OilItemStack stack, int damage, EntityLivingRep entity) {
        IDurable.super.damageItem(stack, damage*3, entity);
    }

    @Override
    public boolean handleItemDamage(OilItemStack stack, int damage, EntityLivingRep entity) {
        return IDurable.super.handleItemDamage(stack, damage*4, entity);
    }

    @Override
    public boolean isRepairable(OilItemStack toRepair, ItemStackRep repair) {
        return repair.getItemState().isSimilar(MinecraftItem.BEEF);
    }
}
