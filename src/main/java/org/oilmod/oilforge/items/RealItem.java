package org.oilmod.oilforge.items;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.oilmod.api.items.ItemInteractionResult;
import org.oilmod.api.items.NMSItem;
import org.oilmod.api.items.OilItem;
import org.oilmod.api.items.type.IDurable;
import org.oilmod.api.items.type.IToolBlockBreaking;
import org.oilmod.api.items.type.IVDAdapter;
import org.oilmod.oilforge.NMSKeyImpl;
import org.oilmod.oilforge.block.RealOilBlockState;
import org.oilmod.oilforge.config.nbttag.NBTCompound;
import org.oilmod.oilforge.rep.item.ItemFR;
import org.oilmod.oilforge.rep.itemstack.ItemStackFR;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;

import static org.oilmod.oilforge.Util.toForge;
import static org.oilmod.oilforge.Util.toOil;
import static org.oilmod.oilforge.dirtyhacks.RealItemStackHelper.toReal;

public class RealItem extends Item implements NMSItem, RealItemImplHelper {
    private final OilItem apiItem;

    public static Item.Properties createBuilder(OilItem oilItem) {
        Item.Properties b = new Item.Properties().maxStackSize(oilItem.getMaxStackSize());
        if (oilItem instanceof IDurable) {
            b.defaultMaxDamage(((IDurable) oilItem).getMaxDurability());
        }
        b.group(ItemGroup.FOOD);
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



    public boolean canHarvestBlock(ItemStack stack, IBlockState state) {
        return RealItemImplHelper.super.canHarvestBlock(stack, state);
    }

    public int getItemEnchantability() {
        return RealItemImplHelper.super.getItemEnchantability();
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
}
