package org.oilmod.oilforge.inventory;



// CraftBukkit start

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IRecipeHelperPopulator;
import net.minecraft.inventory.IRecipeHolder;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.SlotFurnaceFuel;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import org.oilmod.api.config.Compound;
import org.oilmod.api.inventory.ModFurnaceInventoryObject;
import org.oilmod.api.rep.inventory.InventoryHolderRep;
import org.oilmod.api.util.ITicker;
import org.oilmod.oilforge.inventory.container.OilContainerFurnace;
import org.oilmod.oilforge.rep.location.WorldFR;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

// CraftBukkit end
//OilSpigot start
//OilSpigot end

/**
 * Created by sirati97 on 13.02.2016.
 */
public class OilInventoryFurnace extends OilInventoryBase<ModFurnaceInventoryObject> implements ISidedInventory, IRecipeHolder, IRecipeHelperPopulator {
    //OilSpigot start

    private boolean wasBurning = false;
    private World world;

    //new TextComponentTranslation("container.furnace", new Object[0])
    public OilInventoryFurnace(InventoryHolderRep owner, String title, ITicker ticker, IItemFilter itemFilter) {
        super(owner, title, 3, ticker, itemFilter, true);
        world = ((WorldFR)ticker.getMainWorld()).getForge();
    }
    //OilSpigot end

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        ItemStack itemstack = getStackInSlot(index);
        boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
        super.setInventorySlotContents(index, stack);
        if (stack.getCount() > this.getInventoryStackLimit()) {
            stack.setCount(this.getInventoryStackLimit());
        }

        if (index == 0 && !flag) {
            this.totalCookTime = this.getCookTime();
            this.cookTime = 0;
            this.markDirty();
        }

    }

    //OilSpigot start
    public String getContainerLangKey() {
        return "container.furnace";
    }


    @Override
    public void load(Compound compound) {
        super.load(compound); //This loads items
        this.furnaceBurnTime = compound.getInt("BurnTime");
        this.cookTime = compound.getInt("CookTime");
        this.totalCookTime = compound.getInt("CookTimeTotal");
        this.currentItemBurnTime = getItemBurnTime(getStackInSlot(1));
        int i = compound.getShort("RecipesUsedSize");

        for(int j = 0; j < i; ++j) {
            ResourceLocation resourcelocation = new ResourceLocation(compound.getString("RecipeLocation" + j));
            int k = compound.getInt("RecipeAmount" + j);
            this.recipeUseCounts.put(resourcelocation, k);
        }
    }

    @Override
    public void save(Compound compound) {
        super.save(compound); //This saves items

        compound.setInt("BurnTime", this.furnaceBurnTime);
        compound.setInt("CookTime", this.cookTime);
        compound.setInt("CookTimeTotal", this.totalCookTime);
        compound.setShort("RecipesUsedSize", (short)this.recipeUseCounts.size());
        int i = 0;

        for(Map.Entry<ResourceLocation, Integer> entry : this.recipeUseCounts.entrySet()) {
            compound.setString("RecipeLocation" + i, ((ResourceLocation)entry.getKey()).toString());
            compound.setInt("RecipeAmount" + i, entry.getValue());
            ++i;
        }
        //TODO: check if furnances now support custom names
    }
    //OilSpigot end


    /**
     * Furnace isBurning
     */
    public boolean isBurning() {
        return this.furnaceBurnTime > 0;
    }

    //OilForge
    private IRecipe obtainRecipe() {
        return this.world.getRecipeManager().getRecipe(this, this.world, net.minecraftforge.common.crafting.VanillaRecipeTypes.SMELTING);
    }

    private boolean replenishFire() {
        boolean returnValue = false;
        ItemStack itemstack = getStackInSlot(1);
        this.furnaceBurnTime = getItemBurnTime(itemstack);
        this.currentItemBurnTime = this.furnaceBurnTime;
        if (this.isBurning()) {
            returnValue = true;
            if (itemstack.hasContainerItem()) {
                setInventorySlotContents(1, itemstack.getContainerItem());
            }
            else
            if (!itemstack.isEmpty()) {
                Item item = itemstack.getItem();
                itemstack.shrink(1);
                if (itemstack.isEmpty()) {
                    Item item1 = item.getContainerItem();
                    setInventorySlotContents(1, item1 == null ? ItemStack.EMPTY : new ItemStack(item1));
                }
            }
        }
        return returnValue;
    }

    private void resetCooking() {
        cookTime = 0;
    }

    private boolean processTicks(int elapsedTicks, IRecipe irecipe) {
        boolean returnValue = false;
        //OilForge start
        while (elapsedTicks > 0) {
            //burn enough fuel to smelt item or stop
            int ticksToCook = Math.min(totalCookTime - cookTime, elapsedTicks);
            if (furnaceBurnTime >= ticksToCook) {
                furnaceBurnTime -= ticksToCook;
            } else {
                int missingFuel = ticksToCook - furnaceBurnTime;
                furnaceBurnTime = 0;
                while (missingFuel > 0) {
                    if (replenishFire()) {
                        returnValue = true;
                    } else {
                        resetCooking();
                        return returnValue;
                    }
                    if (missingFuel > furnaceBurnTime) {
                        missingFuel -= furnaceBurnTime;
                        furnaceBurnTime = 0;
                    }
                }
                furnaceBurnTime -= missingFuel;
            }
            cookTime += ticksToCook;
            elapsedTicks -= ticksToCook;

            if (cookTime == totalCookTime) { //only not the case in the last execution of loop
                this.cookTime = 0;
                irecipe = obtainRecipe();
                this.smeltItem(irecipe);
                irecipe = obtainRecipe();
                this.totalCookTime = this.getCookTime();
                returnValue = true;
            }
        }
        return returnValue;
    }

    public void smelt(int elapsedTicks) {
        boolean wasBurning = this.isBurning();
        boolean flag1 = false;


        if (this.isBurning()) {
            --this.furnaceBurnTime;
        }

        if (!this.world.isRemote) {
            ItemStack itemstack = getStackInSlot(1);
            if (this.isBurning() || !itemstack.isEmpty() && !((ItemStack)getStackInSlot(0)).isEmpty()) {
                IRecipe irecipe = obtainRecipe(); //OilForge
                if (!this.isBurning() && this.canSmelt(irecipe)) {
                    flag1 |= replenishFire();
                }

                if (this.isBurning() && this.canSmelt(irecipe)) {
                    processTicks(elapsedTicks, irecipe);
                } else {
                    this.cookTime = 0;
                }
            } else if (!this.isBurning() && this.cookTime > 0) {
                this.cookTime = MathHelper.clamp(this.cookTime - 2, 0, this.totalCookTime);
            }

            if (wasBurning != this.isBurning()) {
                flag1 = true;
                if (wasBurning) {
                    onGoneOut();
                } else {
                    onStartFire();
                }
            }
        }

        if (flag1) {
            this.markDirty();
        }

    }



    //OilSpigot start
    protected void onStartFire(){}
    protected void onGoneOut(){}
    //OilSpigot end

    //OilSpigot start

    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
        return new OilContainerFurnace(playerInventory, this);
    }

    @Override
    public void tick(int i) {
        smelt(i);
    }

    @Override
    public boolean isTickable() {
        return true;
    }

    //OilSpigot end





















    
    /** The number of ticks that the furnace will keep burning */
    private int furnaceBurnTime;
    /** The number of ticks that a fresh copy of the currently-burning item would keep the furnace burning for */
    private int currentItemBurnTime;
    private int cookTime;
    private int totalCookTime;
    private ITextComponent furnaceCustomName;
    private final Map<ResourceLocation, Integer> recipeUseCounts = Maps.<ResourceLocation, Integer>newHashMap();






    private int getCookTime() {
        FurnaceRecipe furnacerecipe = this.world.getRecipeManager().getRecipe(this, this.world, net.minecraftforge.common.crafting.VanillaRecipeTypes.SMELTING);
        return furnacerecipe != null ? furnacerecipe.getCookingTime() : 200;
    }

    private boolean canSmelt(@Nullable IRecipe recipe) {
        if (!((ItemStack)getStackInSlot(0)).isEmpty() && recipe != null) {
            ItemStack itemstack = recipe.getRecipeOutput();
            if (itemstack.isEmpty()) {
                return false;
            } else {
                ItemStack itemstack1 = getStackInSlot(2);
                if (itemstack1.isEmpty()) {
                    return true;
                } else if (!itemstack1.isItemEqual(itemstack)) {
                    return false;
                } else if (itemstack1.getCount() + itemstack.getCount() <= this.getInventoryStackLimit() && itemstack1.getCount() < itemstack1.getMaxStackSize()) { // Forge fix: make furnace respect stack sizes in furnace recipes
                    return true;
                } else {
                    return itemstack1.getCount() + itemstack.getCount() <= itemstack.getMaxStackSize(); // Forge fix: make furnace respect stack sizes in furnace recipes
                }
            }
        } else {
            return false;
        }
    }

    private void smeltItem(@Nullable IRecipe recipe) {
        if (recipe != null && this.canSmelt(recipe)) {
            ItemStack itemstack = getStackInSlot(0);
            ItemStack itemstack1 = recipe.getRecipeOutput();
            ItemStack itemstack2 = getStackInSlot(2);
            if (itemstack2.isEmpty()) {
                setInventorySlotContents(2, itemstack1.copy());
            } else if (itemstack2.getItem() == itemstack1.getItem()) {
                itemstack2.grow(itemstack1.getCount());
            }

            if (!this.world.isRemote) {
                this.canUseRecipe(this.world, (EntityPlayerMP)null, recipe);
            }

            if (itemstack.getItem() == Blocks.WET_SPONGE.asItem() && !((ItemStack)getStackInSlot(1)).isEmpty() && ((ItemStack)getStackInSlot(1)).getItem() == Items.BUCKET) {
                setInventorySlotContents(1, new ItemStack(Items.WATER_BUCKET));
            }

            itemstack.shrink(1);
        }
    }


    private static final Method getItemBurnTimeMethod;
    static {
        try {
            getItemBurnTimeMethod = TileEntityFurnace.class.getDeclaredMethod("getItemBurnTime", ItemStack.class);
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Returns the number of ticks that the supplied fuel item will keep the furnace burning, or 0 if the item isn't fuel
     */
    private static int getItemBurnTime(ItemStack stack) { //todo use mixin instead
        try {
            return (int) getItemBurnTimeMethod.invoke(null, stack);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException(e);
        }
    }

    public static boolean isItemFuel(ItemStack stack) {
        return getItemBurnTime(stack) > 0;
    }

    /**
     * Don't rename this method to canInteractWith due to conflicts with Container
     */
    public boolean isUsableByPlayer(EntityPlayer player) {
        return true;
    }

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot. For
     * guis use Slot.isItemValid
     */
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        if (index == 2) {
            return false;
        } else if (index != 1) {
            return true;
        } else {
            ItemStack itemstack = getStackInSlot(1);
            return isItemFuel(stack) || SlotFurnaceFuel.isBucket(stack) && itemstack.getItem() != Items.BUCKET;
        }
    }

    private static final int[] SLOTS_TOP = new int[]{0};
    private static final int[] SLOTS_BOTTOM = new int[]{2, 1};
    private static final int[] SLOTS_SIDES = new int[]{1};
    public int[] getSlotsForFace(EnumFacing side) {
        if (side == EnumFacing.DOWN) {
            return SLOTS_BOTTOM;
        } else {
            return side == EnumFacing.UP ? SLOTS_TOP : SLOTS_SIDES;
        }
    }

    /**
     * Returns true if automation can insert the given item in the given slot from the given side.
     */
    public boolean canInsertItem(int index, ItemStack itemStackIn, @Nullable EnumFacing direction) {
        return this.isItemValidForSlot(index, itemStackIn);
    }

    /**
     * Returns true if automation can extract the given item in the given slot from the given side.
     */
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        if (direction == EnumFacing.DOWN && index == 1) {
            Item item = stack.getItem();
            if (item != Items.WATER_BUCKET && item != Items.BUCKET) {
                return false;
            }
        }

        return true;
    }

    public String getGuiID() {
        return "minecraft:furnace";
    }

    public int getField(int id) {
        switch(id) {
            case 0:
                return this.furnaceBurnTime;
            case 1:
                return this.currentItemBurnTime;
            case 2:
                return this.cookTime;
            case 3:
                return this.totalCookTime;
            default:
                return 0;
        }
    }

    public void setField(int id, int value) {
        switch(id) {
            case 0:
                this.furnaceBurnTime = value;
                break;
            case 1:
                this.currentItemBurnTime = value;
                break;
            case 2:
                this.cookTime = value;
                break;
            case 3:
                this.totalCookTime = value;
        }

    }

    public int getFieldCount() {
        return 4;
    }

    public void fillStackedContents(RecipeItemHelper helper) {
        for(ItemStack itemstack : this.getItems()) {
            helper.accountStack(itemstack);
        }

    }

    public void setRecipeUsed(IRecipe recipe) {
        if (this.recipeUseCounts.containsKey(recipe.getId())) {
            this.recipeUseCounts.put(recipe.getId(), this.recipeUseCounts.get(recipe.getId()) + 1);
        } else {
            this.recipeUseCounts.put(recipe.getId(), 1);
        }

    }

    @Nullable
    public IRecipe getRecipeUsed() {
        return null;
    }

    public Map<ResourceLocation, Integer> getRecipeUseCounts() {
        return this.recipeUseCounts;
    }

    public boolean canUseRecipe(World worldIn, EntityPlayerMP player, @Nullable IRecipe recipe) {
        if (recipe != null) {
            this.setRecipeUsed(recipe);
            return true;
        } else {
            return false;
        }
    }

    public void onCrafting(EntityPlayer player) {
        if (!this.world.getGameRules().getBoolean("doLimitedCrafting")) {
            List<IRecipe> list = Lists.<IRecipe>newArrayList();

            for(ResourceLocation resourcelocation : this.recipeUseCounts.keySet()) {
                IRecipe irecipe = player.world.getRecipeManager().getRecipe(resourcelocation);
                if (irecipe != null) {
                    list.add(irecipe);
                }
            }

            player.unlockRecipes(list);
        }

        this.recipeUseCounts.clear();
    }

    net.minecraftforge.common.util.LazyOptional<? extends net.minecraftforge.items.IItemHandler>[] handlers =
            net.minecraftforge.items.wrapper.SidedInvWrapper.create(this, EnumFacing.UP, EnumFacing.DOWN, EnumFacing.NORTH);

}
