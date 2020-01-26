package org.oilmod.oilforge.internaltest.testmod1.items;

import org.oilmod.api.items.*;
import org.oilmod.api.items.type.*;
import org.oilmod.api.rep.block.BlockStateRep;
import org.oilmod.api.rep.entity.EntityLivingRep;
import org.oilmod.api.rep.providers.minecraft.MinecraftItem;
import org.oilmod.api.rep.world.LocationBlockRep;

import static org.oilmod.api.rep.providers.minecraft.MinecraftBlock.GOLD_BLOCK;
import static org.oilmod.oilforge.Util.toOil;

public class UpgradablePickaxe extends OilItem implements IPickaxe, IDurable {
    public UpgradablePickaxe() {
        super(MinecraftItem.GOLDEN_PICKAXE, "I am so fast it hurts");
    }

    @Override
    public int getToolStrength(OilItemStack stack, TBBType tooltype) {
        return 3;
    }

    @Override
    public float getDestroySpeed(OilItemStack itemStack) {
        if (itemStack == null)return 10;
        return  ((UpgradablePickaxeState)itemStack).intData.getData()*10+10;
    }

    @Override
    protected OilItemStack createOilItemStackInstance(NMSItemStack nmsItemStack) {
        return new UpgradablePickaxeState(nmsItemStack, this);
    }

    @Override
    public int getMaxDurability() {
        return 170;
    }

    @Override
    public int getItemEnchantability() {
        return 22;
    }

    @Override
    public boolean onBlockDestroyed(OilItemStack stack, BlockStateRep blockState, LocationBlockRep pos, EntityLivingRep entityLiving) {
        System.out.println("onBlockDestroyed called");
        if (blockState.getBlock().equals(GOLD_BLOCK.get().getBlock())) {

            UpgradablePickaxeState stack1 = ((UpgradablePickaxeState)stack);
            stack1.intData.setData(1+stack1.intData.getData());
        }
        return IPickaxe.super.onBlockDestroyed(stack, blockState, pos, entityLiving);
    }

}
