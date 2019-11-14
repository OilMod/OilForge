package org.oilmod.oilforge.items.tools;

import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.oilmod.api.items.OilItem;
import org.oilmod.api.items.type.IDurable;
import org.oilmod.api.items.type.IPickaxe;
import org.oilmod.api.items.type.IToolBlockBreaking;
import org.oilmod.oilforge.NMSKeyImpl;
import org.oilmod.oilforge.OilMain;
import org.oilmod.oilforge.items.RealItemImplHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static org.oilmod.oilforge.items.RealItem.createBuilder;

public class RealPickaxe extends ItemPickaxe implements RealItemImplHelper {

    private final OilItem apiItem;



    public RealPickaxe(OilItem oilItem) {
        super(new IItemTier() {
            @Override
            public int getMaxUses() {
                return (oilItem instanceof IDurable)?((IDurable)oilItem).getMaxDurability():0;
            }

            @Override
            public float getEfficiency() {
                return (oilItem instanceof IToolBlockBreaking)?((IToolBlockBreaking)oilItem).getDestroySpeed(null):0;
            }

            @Override
            public float getAttackDamage() {
                return 0;
            }

            @Override
            public int getHarvestLevel() {
                return (oilItem instanceof IPickaxe)?((IPickaxe)oilItem).getPickaxeStrength():0;
            }

            @Override
            public int getEnchantability() {
                return oilItem.getItemEnchantability();
            }

            @Override
            public Ingredient getRepairMaterial() {
                return null;
            }
        }, 0, 1, createBuilder(oilItem).addToolType(ToolType.PICKAXE, (oilItem instanceof IPickaxe)?((IPickaxe)oilItem).getPickaxeStrength():0));
        this.apiItem = oilItem;
        setRegistryName(((NMSKeyImpl) apiItem.getOilKey().getNmsKey()).resourceLocation);
    }

    public OilItem getApiItem() {
        return apiItem;
    }



    //Connect interface



    public boolean canHarvestBlock(ItemStack stack, IBlockState state) {
        return RealItemImplHelper.super.canHarvestBlock(stack, state);
    }

    public int getItemEnchantability(ItemStack stack) {
        return RealItemImplHelper.super.getItemEnchantability(stack);
    }

    public float getDestroySpeed(ItemStack stack, IBlockState state) {
        return RealItemImplHelper.super.getDestroySpeed(stack, state);
    }

    @Nonnull
    public EnumActionResult onItemUse(ItemUseContext context) {
        return RealItemImplHelper.super.onItemUse(context);
    }

    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
        return RealItemImplHelper.super.onBlockDestroyed(stack,worldIn,state,pos,entityLiving);
    }

    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, @Nonnull EnumHand handIn) {
        return RealItemImplHelper.super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Nullable
    public NBTTagCompound getShareTag(ItemStack stack) {
        return RealItemImplHelper.super.getShareTag(stack);
    }

    public void readShareTag(ItemStack stack, @Nullable NBTTagCompound nbt) {
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
    public boolean canEquip(ItemStack stack, EntityEquipmentSlot armorType, Entity entity) {
        return RealItemImplHelper.super.canEquip(stack, armorType, entity);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
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
}
