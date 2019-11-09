package org.oilmod.oilforge.items;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.oilmod.api.items.ItemInteractionResult;
import org.oilmod.api.items.NMSItem;
import org.oilmod.api.items.OilItem;
import org.oilmod.api.items.type.IToolBlockBreaking;
import org.oilmod.oilforge.config.nbttag.NBTCompound;
import org.oilmod.oilforge.rep.item.ItemFR;
import org.oilmod.oilforge.rep.itemstack.ItemStackFR;

import javax.annotation.Nullable;

import static org.oilmod.oilforge.Util.toForge;
import static org.oilmod.oilforge.Util.toOil;
import static org.oilmod.oilforge.dirtyhacks.RealItemStackHelper.toReal;

public interface RealItemImplHelper extends NMSItem {


    //Oil


    default Item getVanillaFakeItem(RealItemStack stack) {
        return ((ItemFR)getApiItem().getVanillaItem(stack.getOilItemStack())).getForge();
    }

    OilItem getApiItem();

    //NMS
    default boolean canHarvestBlock(ItemStack stack, IBlockState state) { //todo generify to consider itemstack
        if (getApiItem() instanceof IToolBlockBreaking) {
            return ((IToolBlockBreaking)getApiItem()).canHarvestBlock(toOil(state), null); //TODO FIX -> state.getMaterial().getOilBlockType());
        }
        return false; //return super
    }
    default int getItemEnchantability() {
        return getApiItem().getItemEnchantability();
    }

    default float getDestroySpeed(ItemStack stack, IBlockState state) {
        if (getApiItem() instanceof IToolBlockBreaking) {
            return ((IToolBlockBreaking)getApiItem()).getDestroySpeed(toReal(stack).getOilItemStack(), toOil(state), null); //TODO FIX -> state.getMaterial().getOilBlockType());
        }
        return 1.0F;//return super
    }

    default EnumActionResult onItemUse(ItemUseContext context) {
        World world = context.getWorld();
        BlockPos blockpos = context.getPos();
        EntityPlayer player = context.getPlayer();

        RealItemStack itemstack = toReal(context.getItem());
        return toForge(getApiItem().onItemUseOnBlock(itemstack.getOilItemStack(), toOil(player), toOil(blockpos, world), player.getHeldItemOffhand()==context.getItem(), toOil(context.getFace()), 0.5f, 0.5f, 0.5f)); //todo: calculate hit point, or change api to to is lazily
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
    default NBTTagCompound getShareTag(ItemStack stack) {
        NBTTagCompound result = new NBTTagCompound();
        toReal(stack).saveModData(new NBTCompound(result));
        return result;
    }

    default void readShareTag(ItemStack stack, @Nullable NBTTagCompound nbt) {
        toReal(stack).loadModData(new NBTCompound(nbt));
    }
}
