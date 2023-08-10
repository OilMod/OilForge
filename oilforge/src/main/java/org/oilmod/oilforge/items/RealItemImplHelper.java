package org.oilmod.oilforge.items;

import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.extensions.IForgeItem;
import org.oilmod.api.items.*;
import org.oilmod.api.items.type.*;
import org.oilmod.api.rep.enchant.EnchantmentRep;
import org.oilmod.oilforge.config.nbttag.OilNBTCompound;
import org.oilmod.oilforge.items.capability.OilItemStackProvider;
import org.oilmod.oilforge.rep.item.ItemFR;
import org.oilmod.oilforge.rep.itemstack.ItemStackFR;

import javax.annotation.Nullable;

import java.util.Objects;

import static org.oilmod.oilforge.Util.*;

public interface RealItemImplHelper extends NMSItem, IForgeItem, IItemProvider {


    //Oil


    default Item getVanillaFakeItem(RealItemStack stack) {
        return ((ItemFR)getApiItem().getVanillaIcon(stack.getOilItemStack()).getProvidedItem()).getForge();
    }

    OilItem getApiItem();



    //NMS
    @Override
    default boolean canHarvestBlock(ItemStack stack, BlockState state) { //todo generify to consider itemstack
        if (getApiItem() instanceof IToolBlockBreaking) {
            return ((IToolBlockBreaking)getApiItem()).canHarvestBlock(toReal(stack).getOilItemStack(), toOil(state), toOil(state.getMaterial()));
        }
        return false; //return super
    }
    @Override
    default int getItemEnchantability(ItemStack stack) {
        return getApiItem().getItemEnchantability();
    }

    default float getDestroySpeed(ItemStack stack, BlockState state) {
        if (getApiItem() instanceof IToolBlockBreaking) {
            return ((IToolBlockBreaking)getApiItem()).getDestroySpeed(toReal(stack).getOilItemStack(), toOil(state), toOil(state.getMaterial()));
        }
        return 1.0F;//return super
    }

    default ActionResultType onItemUse(ItemUseContext context) {
        World world = context.getWorld();
        BlockPos blockpos = context.getPos();
        PlayerEntity player = context.getPlayer();

        RealItemStack itemstack = toReal(context.getItem());
        Vector3d hit = context.getHitVec();
        return toForge(getApiItem().onItemUseOnBlock(itemstack.getOilItemStack(), toOil(player), toOil(blockpos, world), context.getHand() == Hand.OFF_HAND, toOil(context.getFace()), (float)hit.x, (float)hit.y, (float)hit.z));
    }

    /*
    public EnumInteractionResult onItemUseOnBlock(EntityHuman human, World w, BlockPosition pos, Hand hand, EnumDirection facing, float hitX, float hitY, float hitZ) {
        RealItemStack itemstack = (RealItemStack) human.b(hand);
        return OilSpigotUtil.toNMS(getApiItem().onItemUseOnBlock(itemstack.getOilItemStack(), human.getBukkitEntity(), OilSpigotUtil.toBukkit(w, pos), hand==Hand.OFF_HAND, OilSpigotUtil.toBukkit(facing), hitX, hitY, hitZ));
    }

    public boolean onEntityHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        return getApiItem().onEntityHit(((RealItemStack)stack).getOilItemStack(), (LivingEntity)target.getBukkitEntity(), (LivingEntity)attacker.getBukkitEntity());
    }*/ //TODO

    default boolean onBlockDestroyed(ItemStack stack, World worldIn, BlockState state, BlockPos pos, LivingEntity livingEntity) {
        return getApiItem().onBlockDestroyed(toReal(stack).getOilItemStack(), toOil(state), toOil(pos, worldIn), livingEntity instanceof PlayerEntity?toOil((PlayerEntity)livingEntity):toOil((MobEntity) livingEntity));
    }

    default ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        RealItemStack itemstack = toReal(playerIn.getHeldItem(handIn));
        ItemInteractionResult result = getApiItem().onItemRightClick(itemstack.getOilItemStack(), toOil(worldIn), toOil(playerIn), handIn==Hand.OFF_HAND);

        return new ActionResult<>(toForge(result.getInteractionResult()), ((ItemStackFR)result.getItemStack()).getForge());
    }

    @Nullable
    @Override
    default CompoundNBT getShareTag(ItemStack stack) {
        CompoundNBT result = new CompoundNBT();
        toReal(stack).saveModData(new OilNBTCompound(result));
        return result;
    }

    @Override
    default void readShareTag(ItemStack stack, @Nullable CompoundNBT nbt) {
        //todo this is only for the client receiving stuff wrong method
        toReal(stack).loadModData(new OilNBTCompound(nbt));
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
    default boolean canEquip(ItemStack stack, EquipmentSlotType armorType, Entity entity) {
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
    default ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        RealItemStack realItemStack = new RealItemStack(stack, getApiItem());

        return new OilItemStackProvider(realItemStack);
    }

    @Override
    default boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment ench) {
        EnchantmentRep enchRep = toOil(ench);
        return getApiItem().getEnchantmentType().containsEnchantment(enchRep) || getApiItem().canEnchantSpecial(enchRep, false);
    }

    default void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if (Objects.equals(getItem().getGroup(), group)) { //todo use isInGroup (requires access transformer)
            for (OilItemStackFactory factory:getApiItem().getCreativeItems()) {
                items.add(toForge(factory.create()));
            }
        }
    }

    @Override
    default boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        if (slotChanged) return IForgeItem.super.shouldCauseReequipAnimation(oldStack, newStack, slotChanged);
        else return !(isModStack(oldStack) && isModStack(newStack) && toReal(oldStack).getItem().getApiItem().equals( toReal(newStack).getItem().getApiItem()));
    }

    //todo use for crafting
    /*/**
     * Called when item is crafted/smelted. Used only by maps so far.
     */
    /*public void onCreated(ItemStack stack, World worldIn, PlayerEntity playerIn) {
    }*/

}
