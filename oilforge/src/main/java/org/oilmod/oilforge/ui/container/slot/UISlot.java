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
import org.oilmod.oilforge.rep.inventory.InventoryFR;
import org.oilmod.oilforge.ui.RealItemRef;
import org.oilmod.oilforge.ui.SlotTypeProcessing;

public class UISlot extends Slot {
    private final RealItemRef ref;
    private final IItemFilter filter;
    private final IItemElement element;
    private final int elementRow;
    private final int elementColumn;

    public UISlot(IItemElement element, RealItemRef ref, int slotId, int xPosition, int yPosition, IItemFilter filter, int elementRow, int elementColumn) {
        super(null, slotId, xPosition, yPosition);
        this.ref = ref;
        this.filter = filter;
        this.element = element;
        this.elementRow = elementRow;
        this.elementColumn = elementColumn;
    }

    private RealItemRef rref() {
        ref.reset();
        ref.next(element, elementRow, elementColumn);
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
        this.onSlotChanged();

        ISlotType slotType = rref().getSlotType();
        if (slotType instanceof SlotTypeProcessing) {
            ICraftingProcessor processor = ((SlotTypeProcessing) slotType).getProcessor();
            processor.onSlotTake();
            processor.afterSlotTake();
        }

        return ref.getHandler().onTake(ref, thePlayer, stack);
        //todo add crafting logic
    }

    /**
     * Helper fnct to get the stack in the slot.
     */
    @Override
    public ItemStack getStack() {
        ISlotType slotType = rref().getSlotType();
        ItemStack result = ref.getHandler().getStack(ref);
        /*if (result.isEmpty() && slotType instanceof SlotTypeProcessing) {
            SlotTypeProcessing slotTypeProcessing = ((SlotTypeProcessing) slotType);
            ICraftingProcessor processor = slotTypeProcessing.getProcessor();
            for (IResultCategory category:slotTypeProcessing.getCategories()) {
                //to/do: this will result is wrong behaviour if the local slotview does not match the processors result invview. this is a bug, to fix it we need to enforce they are the same or obtain the local slot id other ways
                ItemStack preview = ((InventoryFR)processor.getPreviewInventory(category)).getForge().getStackInSlot(elementRow * element.getColumns()+ elementColumn);
                //System.out.printf("preview: %s, real:%s, slotid:%d\n", preview, result, elementRow * element.getColumns()+ elementColumn);
                if (!preview.isEmpty()) return preview;
            }
        }*/
        return result;
    }

    /**
     * Returns if this slot contains a stack.
     */
    public boolean getHasStack() {
        return rref().getHandler().getHasStack(ref);
    }

    /**
     * Helper method to put a stack in the slot.
     */
    public void putStack(ItemStack stack) {
        rref().getHandler().putStack(ref, stack);
        this.onSlotChanged();
    }

    /**
     * Called when the stack in a Slot changes
     */
    public void onSlotChanged() {
        rref().getHandler().onSlotChanged(ref);
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
        return rref().getHandler().decrStackSize(ref, amount);
    }

    /**
     * Return whether this slot's stack can be taken from this slot.
     */
    public boolean canTakeStack(PlayerEntity playerIn) {


        if ( rref().getSlotType().isTakeable() && ref.getHandler().canTakeStack(ref, playerIn)) {

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

    /**
     * Checks if the other slot is in the same inventory, by comparing the inventory reference.
     * @param other
     * @return true if the other slot is in the same inventory
     */
    public boolean isSameInventory(Slot other) {
        return other instanceof UISlot && this.element == ((UISlot) other).element;
    }
}
