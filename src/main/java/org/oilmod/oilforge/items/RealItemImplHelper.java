package org.oilmod.oilforge.items;

import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.extensions.IForgeItem;
import org.oilmod.api.items.*;
import org.oilmod.api.items.type.*;
import org.oilmod.api.rep.enchant.EnchantmentRep;
import org.oilmod.api.rep.itemstack.ItemStackRep;
import org.oilmod.oilforge.config.nbttag.NBTCompound;
import org.oilmod.oilforge.items.capability.OilItemStackProvider;
import org.oilmod.oilforge.rep.item.ItemFR;
import org.oilmod.oilforge.rep.itemstack.ItemStackFR;

import javax.annotation.Nullable;

import static org.oilmod.oilforge.Util.*;

public interface RealItemImplHelper extends NMSItem, IForgeItem {


    //Oil


    default Item getVanillaFakeItem(RealItemStack stack) {
        return ((ItemFR)getApiItem().getVanillaItem(stack.getOilItemStack())).getForge();
    }

    OilItem getApiItem();



    //NMS
    @Override
    default boolean canHarvestBlock(ItemStack stack, IBlockState state) { //todo generify to consider itemstack
        if (getApiItem() instanceof IToolBlockBreaking) {
            return ((IToolBlockBreaking)getApiItem()).canHarvestBlock(toReal(stack).getOilItemStack(), toOil(state), toOil(state.getMaterial()));
        }
        return false; //return super
    }
    @Override
    default int getItemEnchantability(ItemStack stack) {
        return getApiItem().getItemEnchantability();
    }

    default float getDestroySpeed(ItemStack stack, IBlockState state) {
        if (getApiItem() instanceof IToolBlockBreaking) {
            return ((IToolBlockBreaking)getApiItem()).getDestroySpeed(toReal(stack).getOilItemStack(), toOil(state), toOil(state.getMaterial()));
        }
        return 1.0F;//return super
    }

    default EnumActionResult onItemUse(ItemUseContext context) {
        World world = context.getWorld();
        BlockPos blockpos = context.getPos();
        EntityPlayer player = context.getPlayer();

        RealItemStack itemstack = toReal(context.getItem());
        return toForge(getApiItem().onItemUseOnBlock(itemstack.getOilItemStack(), toOil(player), toOil(blockpos, world), player.getHeldItemOffhand()==context.getItem(), toOil(context.getFace()), context.getHitX(), context.getHitY(), context.getHitZ()));
    }

    /*
    public EnumInteractionResult onItemUseOnBlock(EntityHuman human, World w, BlockPosition pos, EnumHand hand, EnumDirection facing, float hitX, float hitY, float hitZ) {
        RealItemStack itemstack = (RealItemStack) human.b(hand);
        return OilSpigotUtil.toNMS(getApiItem().onItemUseOnBlock(itemstack.getOilItemStack(), human.getBukkitEntity(), OilSpigotUtil.toBukkit(w, pos), hand==EnumHand.OFF_HAND, OilSpigotUtil.toBukkit(facing), hitX, hitY, hitZ));
    }

    public boolean onEntityHit(ItemStack stack, EntityLiving target, EntityLiving attacker) {
        return getApiItem().onEntityHit(((RealItemStack)stack).getOilItemStack(), (LivingEntity)target.getBukkitEntity(), (LivingEntity)attacker.getBukkitEntity());
    }*/ //TODO

    default boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
        return getApiItem().onBlockDestroyed(toReal(stack).getOilItemStack(), toOil(state), toOil(pos, worldIn), entityLiving instanceof EntityPlayer?toOil((EntityPlayer)entityLiving):toOil((EntityLiving)entityLiving));
    }

    default ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        RealItemStack itemstack = toReal(playerIn.getHeldItem(handIn));
        ItemInteractionResult result = getApiItem().onItemRightClick(itemstack.getOilItemStack(), toOil(worldIn), toOil(playerIn), handIn==EnumHand.OFF_HAND);

        return new ActionResult<>(toForge(result.getInteractionResult()), ((ItemStackFR)result.getItemStack()).getForge());
    }

    @Nullable
    @Override
    default NBTTagCompound getShareTag(ItemStack stack) {
        NBTTagCompound result = new NBTTagCompound();
        toReal(stack).saveModData(new NBTCompound(result));
        return result;
    }

    @Override
    default void readShareTag(ItemStack stack, @Nullable NBTTagCompound nbt) {
        //todo this is only for the client receiving stuff wrong method
        toReal(stack).loadModData(new NBTCompound(nbt));
    }

    default boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        OilItemStack oil = toReal(toRepair).getOilItemStack();
        return oil.getItem().isRepairable(oil, toOil(repair));
    }

    @Nullable
    @Override
    default String getCreatorModId(ItemStack itemStack) {
        return getApiItem().getOilKey().getMod().getInternalName();
    }

    @Override
    default double getDurabilityForDisplay(ItemStack stack) {
        return getApiItem() instanceof IDurable?((IDurable) getApiItem()).getDurabilityForDisplay(toReal(stack).getOilItemStack()):IForgeItem.super.getDurabilityForDisplay(stack);
    }

    @Override
    default String getHighlightTip(ItemStack item, String displayName) {
        System.out.println("Highlight tip is: " + displayName);
        return displayName + " this was added";
    }

    @Override
    default boolean canEquip(ItemStack stack, EntityEquipmentSlot armorType, Entity entity) {
        switch (armorType) {
            case FEET:
                if (getApiItem() instanceof IShoes) return true;
                break;
            case LEGS:
                if (getApiItem() instanceof ILeggings) return true;
                break;
            case CHEST:
                if (getApiItem() instanceof IChestplate) return true;
                break;
            case HEAD:
                if (getApiItem() instanceof IHelmet) return true;
                break;
        }
        return IForgeItem.super.canEquip(stack, armorType, entity);
    }

    @Nullable
    @Override
    default ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
        RealItemStack realItemStack = new RealItemStack(stack, getApiItem());

        return new OilItemStackProvider(realItemStack);
    }

    @Override
    default boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment ench) {
        EnchantmentRep enchRep = toOil(ench);
        return getApiItem().getEnchantmentType().containsEnchantment(enchRep) || getApiItem().canEnchantSpecial(enchRep, false);
    }

    boolean isInGroup(ItemGroup group);

    default void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if (isInGroup(group)) {
            for (OilItemStackFactory factory:getApiItem().getCreativeItems()) {
                items.add(toForge(factory.create()));
            }
        }
    }

    @Override
    default boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        if (slotChanged) return IForgeItem.super.shouldCauseReequipAnimation(oldStack, newStack, slotChanged);
        else return isModStack(oldStack) && isModStack(newStack) && toReal(oldStack).getItem().getApiItem().equals( toReal(newStack).getItem().getApiItem());
    }


}
