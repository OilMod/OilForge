package org.oilmod.oilforge.items.tools;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
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

public class RealShovel extends ItemSpade implements RealItemImplHelper {

    private final OilItem apiItem;



    public RealShovel(OilItem oilItem) {
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
        }, 0, 1, createBuilder(oilItem).addToolType(ToolType.SHOVEL, 0));
        //forge knows harvest levels for all kinda of tools! consider
        this.apiItem = oilItem;
        setRegistryName(((NMSKeyImpl) apiItem.getOilKey().getNmsKey()).resourceLocation);
    }

    public OilItem getApiItem() {
        return apiItem;
    }



    //Connect interface


    @Override
    public boolean canHarvestBlock(IBlockState blockIn) {
        OilMain.printTrace("canHarvestBlock(IBlockState blockIn)");
        return super.canHarvestBlock(blockIn);
    }

    @Override
    public boolean canHarvestBlock(ItemStack stack, IBlockState state) {
        OilMain.printTrace("canHarvestBlock(ItemStack stack, IBlockState blockIn)");
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

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return RealItemImplHelper.super.getIsRepairable(toRepair, repair);
    }
}
