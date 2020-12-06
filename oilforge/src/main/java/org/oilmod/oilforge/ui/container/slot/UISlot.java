package org.oilmod.oilforge.ui.container.slot;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.oilmod.api.UI.IItemElement;
import org.oilmod.api.UI.slot.ISlotType;
import org.oilmod.api.crafting.ICraftingProcessor;
import org.oilmod.api.rep.crafting.IResultCategory;
import org.oilmod.oilforge.inventory.IItemFilter;
import org.oilmod.oilforge.ui.RealItemRef;
import org.oilmod.oilforge.ui.SlotTypeProcessing;

import java.util.function.BooleanSupplier;
import java.util.function.IntSupplier;

public class UISlot extends Slot {
    private final RealItemRef ref;
    private final IItemFilter filter;
    private final IItemElement element;
    private final int elementRow;
    private final int elementColumn;
    private final IntSupplier xPosition;
    private final IntSupplier yPosition;
    private final BooleanSupplier allowPreview;

    public UISlot(IItemElement element, RealItemRef ref, int slotId, IntSupplier xPosition, IntSupplier yPosition, IItemFilter filter, int elementRow, int elementColumn, BooleanSupplier allowPreview) {
        super(null, slotId, xPosition.getAsInt(), yPosition.getAsInt());
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.ref = ref;
        this.filter = filter;
        this.element = element;
        this.elementRow = elementRow;
        this.elementColumn = elementColumn;
        this.allowPreview = allowPreview;
    }

    public void updatePos() {
        this.xPos = xPosition.getAsInt();
        this.yPos = yPosition.getAsInt();
    }

    private RealItemRef rref() {
        return rref(false);
    }
    private RealItemRef rref(boolean preview) {
        ref.reset();
        ref.setPreview(preview && allowPreview.getAsBoolean());
        ref.deferTo(element, elementRow, elementColumn);
        return ref;
    }


    @Override
    public boolean isItemValid(ItemStack stack) {
        return rref().getSlotType().isSettable() && ref.getHandler().isItemValid(ref, stack) && filter.allowed(stack);
    }



    /**
     * if par2 has more items than par1, onCrafting(item,countIncrease) is called
     */
    public void onSlotChange(ItemStack stack1, ItemStack stack2) {
        rref().getHandler().onSlotChange(ref, stack1, stack2);
    }

    /**
     * the itemStack passed in is the output - ie, iron ingots, and pickaxes, not ore and wood. Typically increases an
     * internal count then calls onCrafting(item).
     */
    protected void onCrafting(ItemStack stack, int amount) {
        rref().getHandler().onCrafting(ref, stack, amount);
    }

    protected void onSwapCraft(int p_190900_1_) {
        rref().getHandler().onSwapCraft(ref, p_190900_1_);
    }

    /**
     * the itemStack passed in is the output - ie, iron ingots, and pickaxes, not ore and wood.
     */
    protected void onCrafting(ItemStack stack) {
        rref().getHandler().onCrafting(ref, stack);
    }

    public ItemStack onTake(PlayerEntity thePlayer, ItemStack stack) {
        //this.onSlotChanged(); this seems to be redundant

        return ref.getHandler().onTake(ref, thePlayer, stack);
    }



    /**
     * Helper fnct to get the stack in the slot.
     */
    @Override
    public ItemStack getStack() {
        ItemStack result = rref().getHandler().getStack(ref);
        if (isPreviewing(ref)) {
            //need to reresolve this time as a preview
            result = rref(true).getHandler().getStack(ref);
        }
        return result;
    }

    public boolean isPreviewing() {
        return isPreviewing(rref());
    }

    private boolean isPreviewing(RealItemRef ref) {
        if (!allowPreview.getAsBoolean())return false;
        ISlotType slotType = ref.getSlotType();
        ItemStack result = ref.getHandler().getStack(ref);
        return slotType instanceof SlotTypeProcessing && result.isEmpty();
    }

    public ISlotType getSlotType() {
        return rref().getSlotType();
    }

    /**
     * Returns if this slot contains a stack.
     */
    public boolean getHasStack() {
        rref();
        if (isPreviewing(ref)) {
            return rref(true).getHandler().getHasStack(ref);
        }
        return ref.getHandler().getHasStack(ref);
    }

    /**
     * Helper method to put a stack in the slot.
     */
    public void putStack(ItemStack stack) {
        rref().getHandler().putStack(ref, stack);
        //this.onSlotChanged();  this seems to be redundant
    }

    /**
     * Called when the stack in a Slot changes
     */
    public void onSlotChanged() {
        //rref().getHandler().onSlotChanged(ref);  this seems to be redundant
    }

    /**
     * Returns the maximum stack size for a given slot (usually the same as getInventoryStackLimit(), but 1 in the case
     * of armor slots)
     */
    public int getSlotStackLimit() {
        return rref().getHandler().getSlotStackLimit(ref);
    }

    public int getItemStackLimit(ItemStack stack) {
        return rref().getHandler().getItemStackLimit(ref, stack);
    }


    /**
     * Decrease the size of the stack in slot (first int arg) by the amount of the second int arg. Returns the new stack.
     */
    public ItemStack decrStackSize(int amount) {
        ISlotType slotType = rref().getSlotType();
        if (slotType instanceof SlotTypeProcessing) {

            //we only want to craft when we try taking the preview
            if (!ref.getHandler().getHasStack(ref)) {
                ICraftingProcessor processor = ((SlotTypeProcessing) slotType).getProcessor();
                processor.onSlotTake();
                processor.afterSlotTake();
            }

            amount = Math.max(amount, ref.getHandler().getStack(ref).getCount());
        }
        return ref.getHandler().decrStackSize(ref, amount);
    }

    /**
     * Return whether this slot's stack can be taken from this slot.
     */
    public boolean canTakeStack(PlayerEntity playerIn) {
        if (rref().getSlotType().isTakeable() && ref.getHandler().canTakeStack(ref, playerIn)) {
            return true;
        }
        return false;
    }

    /**
     * Actually only call when we want to render the white square effect over the slots. Return always True, except for
     * the armor slot of the Donkey/Mule (we can't interact with the Undead and Skeleton horses)
     */
    @OnlyIn(Dist.CLIENT)
    public boolean isEnabled() {
        return rref().getHandler().isEnabled(ref);
    }

    public ICraftingProcessor getCraftingProcessor() {
        ISlotType type = rref().getSlotType();
        return type instanceof SlotTypeProcessing?((SlotTypeProcessing) type).getProcessor():null;
    }

    public IResultCategory getResultCategory() {
        ISlotType type = rref().getSlotType();
        return type instanceof SlotTypeProcessing?((SlotTypeProcessing) type).getCategories()[0] :null;
    }

    /**
     * Checks if the other slot is in the same inventory, by comparing the inventory reference.
     * @param other
     * @return true if the other slot is in the same inventory
     */
    public boolean isSameInventory(Slot other) {
        return other instanceof UISlot && this.element == ((UISlot) other).element;
    }
}
