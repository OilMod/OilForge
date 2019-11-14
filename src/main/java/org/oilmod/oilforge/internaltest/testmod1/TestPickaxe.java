package org.oilmod.oilforge.internaltest.testmod1;

import net.minecraft.init.Items;
import org.oilmod.api.items.EnchantmentType;
import org.oilmod.api.items.ItemInteractionResult;
import org.oilmod.api.items.OilItem;
import org.oilmod.api.items.OilItemStack;
import org.oilmod.api.items.type.IChestplate;
import org.oilmod.api.items.type.IDurable;
import org.oilmod.api.items.type.IPickaxe;
import org.oilmod.api.items.type.ImplementationProvider;
import org.oilmod.api.rep.block.BlockFaceRep;
import org.oilmod.api.rep.entity.EntityHumanRep;
import org.oilmod.api.rep.entity.EntityLivingRep;
import org.oilmod.api.rep.itemstack.ItemStackRep;
import org.oilmod.api.rep.itemstack.state.Enchantments;
import org.oilmod.api.rep.providers.minecraft.MinecraftItem;
import org.oilmod.api.rep.world.LocationBlockRep;
import org.oilmod.api.rep.world.WorldRep;
import org.oilmod.api.util.InteractionResult;
import org.oilmod.api.util.OilKey;
import org.oilmod.oilforge.OilMain;
import org.oilmod.oilforge.rep.item.ItemFR;

public class TestPickaxe extends OilItem implements IPickaxe, IDurable, IChestplate {
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

    @Override
    public ItemInteractionResult onItemLeftClick(OilItemStack stack, WorldRep world, EntityHumanRep human, boolean offhand) {
        System.out.println("TestPickaxe.onItemLeftClick");
        Enchantments.getAll(stack.asBukkitItemStack()).forEach(o -> System.out.println(o.getKey().toString()));
        return IPickaxe.super.onItemLeftClick(stack, world, human, offhand);
    }

    @Override
    public ItemInteractionResult onItemRightClick(OilItemStack stack, WorldRep world, EntityHumanRep human, boolean offhand) {
        System.out.println("TestPickaxe.onItemRightClick");
        Enchantments.getAll(stack.asBukkitItemStack()).forEach(o -> System.out.println(o.getKey().toString()));
        return IPickaxe.super.onItemRightClick(stack, world, human, offhand);
    }

    @Override
    public InteractionResult onItemLeftClickOnBlock(OilItemStack stack, EntityHumanRep human, LocationBlockRep loc, boolean offhand, BlockFaceRep blockFace, float hitX, float hitY, float hitZ) {
        System.out.println("TestPickaxe.onItemLeftClickOnBlock");
        Enchantments.getAll(stack.asBukkitItemStack()).forEach(o -> System.out.println(o.getKey().toString()));
        return IPickaxe.super.onItemLeftClickOnBlock(stack, human, loc, offhand, blockFace, hitX, hitY, hitZ);
    }

    @Override
    public EnchantmentType getEnchantmentType() {
        return IPickaxe.super.getEnchantmentType();
    }

    @Override
    public ImplementationProvider getImplementationProvider() {
        return IPickaxe.super.getImplementationProvider();
    }
}
