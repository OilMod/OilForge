package org.oilmod.oilforge.inventory;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.block.Blocks;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IRecipeHelperPopulator;
import net.minecraft.inventory.IRecipeHolder;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
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
import java.util.List;
import java.util.Map;

/**
 * Created by sirati97 on 13.02.2016.
 */
public class OilInventoryFurnace extends OilInventoryBase<ModFurnaceInventoryObject> implements ISidedInventory, IRecipeHolder, IRecipeHelperPopulator {
    //OilSpigot start

    private boolean wasBurning = false;

    //new TextComponentTranslation("container.furnace", new Object[0])
    public OilInventoryFurnace(ContainerType<?> containerType, IRecipeType<? extends AbstractCookingRecipe> recipeType, InventoryHolderRep owner, String title, ITicker ticker, IItemFilter itemFilter) {
        super(owner, title, 3, ticker, itemFilter, true);
        this.recipeType = recipeType;
        this.containerType = containerType;
    }
    //OilSpigot end


    //OilSpigot start
    public String getContainerLangKey() {
        return "container.furnace";
    }



    //OilSpigot end

    //OilForge

    private boolean replenishFire() {
        boolean returnValue = false;
        ItemStack itemstack = getStackInSlot(1);
        this.burnTime = getBurnTime(itemstack);
        this.recipesUsed = this.burnTime;
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





    //OilSpigot start
    protected void onStartFire(){}
    protected void onGoneOut(){}
    //OilSpigot end
























    private static final int[] SLOTS_UP = new int[]{0};
    private static final int[] SLOTS_DOWN = new int[]{2, 1};
    private static final int[] SLOTS_HORIZONTAL = new int[]{1};
    private int burnTime;
    private int recipesUsed;
    private int cookTime;
    private int cookTimeTotal;
    protected final IIntArray furnaceData = new IIntArray() {
        public int get(int index) {
            switch(index) {
                case 0:
                    return OilInventoryFurnace.this.burnTime;
                case 1:
                    return OilInventoryFurnace.this.recipesUsed;
                case 2:
                    return OilInventoryFurnace.this.cookTime;
                case 3:
                    return OilInventoryFurnace.this.cookTimeTotal;
                default:
                    return 0;
            }
        }

        public void set(int index, int value) {
            switch(index) {
                case 0:
                    OilInventoryFurnace.this.burnTime = value;
                    break;
                case 1:
                    OilInventoryFurnace.this.recipesUsed = value;
                    break;
                case 2:
                    OilInventoryFurnace.this.cookTime = value;
                    break;
                case 3:
                    OilInventoryFurnace.this.cookTimeTotal = value;
            }

        }

        public int size() {
            return 4;
        }
    };

    protected final IRecipeType<? extends AbstractCookingRecipe> recipeType;
    protected final ContainerType<?> containerType;
    private ITextComponent furnaceCustomName;
    private final Map<ResourceLocation, Integer> recipeUseCounts = Maps.newHashMap();



    private boolean isBurning() {
        return this.burnTime > 0;
    }

    @Override
    public void load(Compound compound) {
        super.load(compound); //This loads items
        this.burnTime = compound.getInt("BurnTime");
        this.cookTime = compound.getInt("CookTime");
        this.cookTimeTotal = compound.getInt("CookTimeTotal");
        this.recipesUsed = getBurnTime(getStackInSlot(1));
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

        compound.setInt("BurnTime", this.burnTime);
        compound.setInt("CookTime", this.cookTime);
        compound.setInt("CookTimeTotal", this.cookTimeTotal);
        compound.setShort("RecipesUsedSize", (short)this.recipeUseCounts.size());
        int i = 0;

        for(Map.Entry<ResourceLocation, Integer> entry : this.recipeUseCounts.entrySet()) {
            compound.setString("RecipeLocation" + i, entry.getKey().toString());
            compound.setInt("RecipeAmount" + i, entry.getValue());
            ++i;
        }
        //TODO: check if furnances now support custom names
    }





    private IRecipe obtainRecipe() {
        return this.getWorld().getRecipeManager().getRecipe((IRecipeType<AbstractCookingRecipe>)this.recipeType, this, this.getWorld()).orElse(null);
    }


    //OilSpigot start

    @Override
    public void tick(int i) {
        smelt(i);
    }

    @Override
    public boolean isTickable() {
        return true;
    }

    //OilSpigot end

    public void smelt(int elapsedTicks) {
        boolean wasBurning = this.isBurning();
        boolean flag1 = false;
        if (this.isBurning()) {
            --this.burnTime;
        }

        if (!this.getWorld().isRemote) {
            ItemStack itemstack = getStackInSlot(1);
            if (this.isBurning() || !itemstack.isEmpty() && !getStackInSlot(0).isEmpty()) {
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
                this.cookTime = MathHelper.clamp(this.cookTime - 2, 0, this.cookTimeTotal);
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


    private boolean processTicks(int elapsedTicks, IRecipe irecipe) {
        boolean returnValue = false;
        //OilForge start
        while (elapsedTicks > 0) {
            //burn enough fuel to smelt item or stop
            int ticksToCook = Math.min(cookTimeTotal - cookTime, elapsedTicks);
            if (burnTime >= ticksToCook) {
                burnTime -= ticksToCook;
            } else {
                int missingFuel = ticksToCook - burnTime;
                burnTime = 0;
                while (missingFuel > 0) {
                    if (replenishFire()) {
                        returnValue = true;
                    } else {
                        resetCooking();
                        return returnValue;
                    }
                    if (missingFuel > burnTime) {
                        missingFuel -= burnTime;
                        burnTime = 0;
                    }
                }
                burnTime -= missingFuel;
            }
            cookTime += ticksToCook;
            elapsedTicks -= ticksToCook;

            if (cookTime == cookTimeTotal) { //only not the case in the last execution of loop
                this.cookTime = 0;
                irecipe = obtainRecipe();
                this.smeltItem(irecipe);
                irecipe = obtainRecipe();
                this.cookTimeTotal = this.getCookTime();
                returnValue = true;
            }
        }
        return returnValue;
    }

    protected boolean canSmelt(@Nullable IRecipe recipe) {
        if (!getStackInSlot(0).isEmpty() && recipe != null) {
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

    private void smeltItem(@Nullable IRecipe<?> recipe) {
        if (recipe != null && this.canSmelt(recipe)) {
            ItemStack itemstack = getStackInSlot(0);
            ItemStack itemstack1 = recipe.getRecipeOutput();
            ItemStack itemstack2 = getStackInSlot(2);
            if (itemstack2.isEmpty()) {
                setInventorySlotContents(2, itemstack1.copy());
            } else if (itemstack2.getItem() == itemstack1.getItem()) {
                itemstack2.grow(itemstack1.getCount());
            }

            if (!this.getWorld().isRemote) {
                this.setRecipeUsed(recipe);
            }

            if (itemstack.getItem() == Blocks.WET_SPONGE.asItem() && !getStackInSlot(1).isEmpty() && getStackInSlot(1).getItem() == Items.BUCKET) {
                setInventorySlotContents(1, new ItemStack(Items.WATER_BUCKET));
            }

            itemstack.shrink(1);
        }
    }



    /**
     * Returns the number of ticks that the supplied fuel item will keep the furnace burning, or 0 if the item isn't fuel
     */
    protected int getBurnTime(ItemStack stack) { 
        if (stack.isEmpty()) {
            return 0;
        } else {
            Item item = stack.getItem();
            return net.minecraftforge.common.ForgeHooks.getBurnTime(stack);
        }
    }

    protected int getCookTime() {
        return this.getWorld().getRecipeManager().getRecipe((IRecipeType<AbstractCookingRecipe>)this.recipeType, this, this.getWorld()).map(AbstractCookingRecipe::getCookTime).orElse(200);
    }

    public static boolean isFuel(ItemStack stack) {
        return net.minecraftforge.common.ForgeHooks.getBurnTime(stack) > 0;
    }

    public int[] getSlotsForFace(Direction side) {
        if (side == Direction.DOWN) {
            return SLOTS_DOWN;
        } else {
            return side == Direction.UP ? SLOTS_UP : SLOTS_HORIZONTAL;
        }
    }

    /**
     * Returns true if automation can insert the given item in the given slot from the given side.
     */
    public boolean canInsertItem(int index, ItemStack itemStackIn, @Nullable Direction direction) {
        return this.isItemValidForSlot(index, itemStackIn);
    }

    /**
     * Returns true if automation can extract the given item in the given slot from the given side.
     */
    public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
        if (direction == Direction.DOWN && index == 1) {
            Item item = stack.getItem();
            if (item != Items.WATER_BUCKET && item != Items.BUCKET) {
                return false;
            }
        }

        return true;
    }


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
            this.cookTimeTotal = this.getCookTime();
            this.cookTime = 0;
            this.markDirty();
        }

    }

    /**
     * Don't rename this method to canInteractWith due to conflicts with Container
     */
    public boolean isUsableByPlayer(PlayerEntity player) {
        return true;
    }


    /**
     * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot. For
     * guis use Slot.isItemValid
     */
    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        if (!super.isItemValidForSlot(index, stack)) return false;

        if (index == 2) {
            return false;
        } else if (index != 1) {
            return true;
        } else {
            ItemStack itemstack = getStackInSlot(1);
            return isFuel(stack) || stack.getItem() == Items.BUCKET && itemstack.getItem() != Items.BUCKET;
        }
    }

    public void setRecipeUsed(IRecipe recipe) {
        if (recipe != null) {
            this.recipeUseCounts.compute(recipe.getId(), (p_214004_0_, p_214004_1_) -> {
                return 1 + (p_214004_1_ == null ? 0 : p_214004_1_);
            });
        }

    }

    @Nullable
    public IRecipe<?> getRecipeUsed() {
        return null;
    }

    public void onCrafting(PlayerEntity player) {
    }

    public void func_213995_d(PlayerEntity p_213995_1_) {
        List<IRecipe<?>> list = Lists.newArrayList();

        for(Map.Entry<ResourceLocation, Integer> entry : this.recipeUseCounts.entrySet()) {
            p_213995_1_.world.getRecipeManager().getRecipe(entry.getKey()).ifPresent((p_213993_3_) -> {
                list.add(p_213993_3_);
                func_214003_a(p_213995_1_, entry.getValue(), ((AbstractCookingRecipe)p_213993_3_).getExperience());
            });
        }

        p_213995_1_.unlockRecipes(list);
        this.recipeUseCounts.clear();
    }

    private static void func_214003_a(PlayerEntity p_214003_0_, int p_214003_1_, float p_214003_2_) {
        if (p_214003_2_ == 0.0F) {
            p_214003_1_ = 0;
        } else if (p_214003_2_ < 1.0F) {
            int i = MathHelper.floor((float)p_214003_1_ * p_214003_2_);
            if (i < MathHelper.ceil((float)p_214003_1_ * p_214003_2_) && Math.random() < (double)((float)p_214003_1_ * p_214003_2_ - (float)i)) {
                ++i;
            }

            p_214003_1_ = i;
        }

        while(p_214003_1_ > 0) {
            int j = ExperienceOrbEntity.getXPSplit(p_214003_1_);
            p_214003_1_ -= j;
            p_214003_0_.world.addEntity(new ExperienceOrbEntity(p_214003_0_.world, p_214003_0_.posX, p_214003_0_.posY + 0.5D, p_214003_0_.posZ + 0.5D, j));
        }

    }

    public Map<ResourceLocation, Integer> getRecipeUseCounts() {
        return this.recipeUseCounts;
    }



    public void fillStackedContents(RecipeItemHelper helper) {
        for(ItemStack itemstack : this.getItems()) {
            helper.accountStack(itemstack);
        }

    }

    public boolean canUseRecipe(World worldIn, ServerPlayerEntity player, @Nullable IRecipe recipe) {
        if (recipe != null) {
            this.setRecipeUsed(recipe);
            return true;
        } else {
            return false;
        }
    }



    //todo consider sided capabilites!

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory player, PlayerEntity p_createMenu_3_) {
        return new OilContainerFurnace(containerType,recipeType, id, player, this, this.furnaceData);
    }
}
