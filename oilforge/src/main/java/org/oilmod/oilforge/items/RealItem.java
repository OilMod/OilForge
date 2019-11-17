package org.oilmod.oilforge.items;

import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.oilmod.api.items.NMSItem;
import org.oilmod.api.items.OilItem;
import org.oilmod.api.items.type.IDurable;
import org.oilmod.oilforge.NMSKeyImpl;
import org.oilmod.oilforge.rep.item.ItemFR;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RealItem extends Item implements NMSItem, RealItemImplHelper {
    private final OilItem apiItem;

    public static Item.Properties createBuilder(OilItem oilItem) {
        Item.Properties b = new Item.Properties().maxStackSize(oilItem.getMaxStackSize());
        if (oilItem instanceof IDurable) {
            b.defaultMaxDamage(((IDurable) oilItem).getMaxDurability());
        }
        b.group(ItemGroup.TRANSPORTATION);
        b.setTEISR(() -> () -> ((ItemFR)oilItem.getVanillaItem(null)).getForge().getTileEntityItemStackRenderer());
        return b;
    }



    public RealItem(OilItem oilItem) {
        super(createBuilder(oilItem));
        this.apiItem = oilItem;
        setRegistryName(((NMSKeyImpl) apiItem.getOilKey().getNmsKey()).resourceLocation);
    }

    public OilItem getApiItem() {
        return apiItem;
    }

    //NMS



    public boolean canHarvestBlock(ItemStack stack, BlockState state) {
        return RealItemImplHelper.super.canHarvestBlock(stack, state);
    }

    public int getItemEnchantability(ItemStack stack) {
        return RealItemImplHelper.super.getItemEnchantability(stack);
    }

    public float getDestroySpeed(ItemStack stack, BlockState state) {
        return RealItemImplHelper.super.getDestroySpeed(stack, state);
    }

    @Nonnull
    public ActionResultType onItemUse(ItemUseContext context) {
        return RealItemImplHelper.super.onItemUse(context);
    }

    public boolean onBlockDestroyed(ItemStack stack, World worldIn, BlockState state, BlockPos pos, LivingEntity livingEntity) {
        return RealItemImplHelper.super.onBlockDestroyed(stack,worldIn,state,pos, livingEntity);
    }

    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, @Nonnull Hand handIn) {
        return RealItemImplHelper.super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Nullable
    public CompoundNBT getShareTag(ItemStack stack) {
        return RealItemImplHelper.super.getShareTag(stack);
    }


    public void readShareTag(ItemStack stack, @Nullable CompoundNBT nbt) {
        RealItemImplHelper.super.readShareTag(stack, nbt);
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return RealItemImplHelper.super.getIsRepairable(toRepair, repair);
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        return RealItemImplHelper.super.getDurabilityForDisplay(stack);
    }

    @Override
    public String getHighlightTip(ItemStack item, String displayName) {
        return RealItemImplHelper.super.getHighlightTip(item, displayName);
    }

    @Override
    public boolean canEquip(ItemStack stack, EquipmentSlotType armorType, Entity entity) {
        return RealItemImplHelper.super.canEquip(stack, armorType, entity);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return RealItemImplHelper.super.initCapabilities(stack, nbt);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return RealItemImplHelper.super.canApplyAtEnchantingTable(stack, enchantment);
    }

    @Override
    public boolean isInGroup(ItemGroup group) {
        return super.isInGroup(group); //correct like this
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        RealItemImplHelper.super.fillItemGroup(group, items);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return RealItemImplHelper.super.shouldCauseReequipAnimation(oldStack, newStack, slotChanged);
    }

    /*@Override
    public boolean isRepairable() {
        return RealItemImplHelper.super.isRepairable();
    }*/


    //test impl

}
