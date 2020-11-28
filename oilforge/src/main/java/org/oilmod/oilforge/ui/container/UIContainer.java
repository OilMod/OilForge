package org.oilmod.oilforge.ui.container;

import net.minecraft.client.util.RecipeBookCategories;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.RecipeBookContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.DistExecutor;
import org.apache.commons.lang3.Validate;
import org.oilmod.api.UI.IItemElement;
import org.oilmod.api.UI.UI;
import org.oilmod.api.crafting.ICraftingProcessor;
import org.oilmod.oilforge.inventory.IItemFilter;
import org.oilmod.oilforge.inventory.NoItemFilter;
import org.oilmod.oilforge.inventory.container.ClientContainerHelper;
import org.oilmod.oilforge.inventory.container.IOilContainer;
import org.oilmod.oilforge.inventory.container.OilContainerType;
import org.oilmod.oilforge.ui.RealItemRef;
import org.oilmod.oilforge.ui.container.slot.UISlot;

import java.util.Collections;
import java.util.List;

import static org.oilmod.oilforge.Util.toForge;
import static org.oilmod.oilforge.Util.toOil;
import static org.oilmod.oilforge.inventory.container.ContainerUtil.GuiOffSide;
import static org.oilmod.oilforge.inventory.container.ContainerUtil.GuiSlotSize;

public class UIContainer extends RecipeBookContainer<IInventory> implements IOilContainer {
    private final PlayerInventory playerInventory;
    private IItemFilter itemFilter;
    private final UI<?> ui;
    private int topSlots;
    private final RealItemRef ref = new RealItemRef();
    private int xDiffHalf;
    private boolean topSmaller;
    private boolean bottomSmaller;


    public UIContainer(OilContainerType<UIContainer> type, int id, PlayerInventory player, PacketBuffer buffer) {
        this(type, id, player, new SetUIPacket(toOil(player.player), buffer));
    }
    public UIContainer(OilContainerType<UIContainer> type, int id, PlayerInventory player, SetUIPacket info) {
        this(null, type, id, player, info.createUI());
        //itemFilter = info.itemFilterPacket.itemFilter;
        initSlots();
    }

    public UIContainer(OilContainerType<UIContainer> type, int id, PlayerInventory player, UI<?> ui) {
        this(null, type, id, player, ui);
        initSlots();
    }
    private UIContainer(Void unused, OilContainerType<UIContainer> type, int id, PlayerInventory playerInventory, UI ui) {
        super(type, id);
        //assertInventorySize(inventory, rows * columns);
        //this.top = inventory;
        this.ui = ui;
        this.playerInventory = playerInventory;

        //inventory.openInventory(playerInventory.player); //todo find a way for this

        itemFilter = NoItemFilter.INSTANCE; //for now
        /*if (inventory instanceof OilIInventory) {
            itemFilter = ((OilIInventory) inventory).getItemFilter();
        }*/
    }

    protected void initSlots() {
        Validate.notNull(itemFilter, "no itemfilter set. huh?");
        Validate.notNull(ui, "no UI set. huh?");

        ui.updateSize();




        int playerInvOff = GuiOffSide + ui.getTopHeight() - 4* GuiSlotSize; //so far we always render an player inventory
        xDiffHalf = (ui.getTopWidth()-GuiSlotSize*9)/2;
        topSmaller = xDiffHalf < 0;
        bottomSmaller = xDiffHalf > 0;
        xDiffHalf = Math.abs(xDiffHalf);


        //whichever taken up less space gets moved in the middle
        int xOff = 8 + getTopXDiffHalf();
        for (IItemElement element:ui.getItemElements()) {

            for (int i = 0; i < element.getRows(); i++) {
                for (int j = 0; j < element.getColumns(); j++) {
                    int finalXOff = xOff;
                    int finalJ = j;
                    int finalI = i;
                    this.addSlot(new UISlot(element, ref, topSlots++, () -> finalXOff + finalJ * GuiSlotSize + element.getLeft(), () -> GuiSlotSize + finalI * GuiSlotSize + element.getTop(), itemFilter, i, j));
                }
            }
        }

        xOff = 8 + getBottomXDiffHalf();
        int yOff = -7;
        for(int l = 0; l < 3; ++l) {
            for(int j1 = 0; j1 < 9; ++j1) {
                this.addSlot(new Slot(playerInventory, j1 + l * 9 + 9, xOff + j1 * GuiSlotSize, 103 + yOff + l * GuiSlotSize + playerInvOff));
            }
        }

        for(int i1 = 0; i1 < 9; ++i1) {
            this.addSlot(new Slot(playerInventory, i1, xOff + i1 * GuiSlotSize, 161 + yOff + playerInvOff));
        }
    }


    public void updateSlotPos() {
        for(Slot slot:inventorySlots) {
            if (slot instanceof UISlot)((UISlot) slot).updatePos();
        }
    }

    /**
     * Determines whether supplied player can use this container
     */
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;//this.top.isUsableByPlayer(playerIn);
    }

    /**
     * Handle when the stack in slot {@code index} is shift-clicked. Normally this moves the stack between the player
     * inventory and the other inventory(s).
     */
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            //todo check if we have a processing stack
            if (index < topSlots) {
                UISlot uiSlot = (UISlot) slot;
                ICraftingProcessor processor=uiSlot.getCraftingProcessor();
                if (processor!= null) {
                    ItemStack previewStack = itemstack1.copy();
                    int max;
                    boolean hasTaken = false;
                    try {
                        do {
                            max = findSpace(previewStack,  topSlots, this.inventorySlots.size());
                            if (max != 0) max = processor.tryCrafting(max, (stack, multiplier, testRun) -> false, true); //bug 1 returns 1 instead of 0
                            if (max == 0) {
                                return ItemStack.EMPTY;
                            }
                            max = processor.tryCrafting(max, (oilStack, multiplier, testRun) -> {
                                if (testRun) return false;
                                int stackLimit =oilStack.getMaxStackSize();
                                int total = (oilStack.getAmount()*multiplier);
                                int stacks = total/stackLimit;
                                int rest = total-stacks*stackLimit;
                                ItemStack stack = toForge(oilStack);
                                stack.setCount(stackLimit);
                                for (int i = 0; i < total; i++) {
                                    playerIn.dropItem(stack.copy(), false);
                                }
                                if (rest!=0) {
                                    stack.setCount(rest);
                                    playerIn.dropItem(stack, false);
                                }
                                return true;
                            }, false);
                            hasTaken = true;

                            itemstack1 = slot.getStack();
                            itemstack = itemstack1.copy();
                            if (!this.mergeItemStack(itemstack1, topSlots, this.inventorySlots.size(), true)) {
                                return ItemStack.EMPTY;
                            }
                        } while (max > 0);
                    } finally {
                        if (hasTaken) {
                            processor.afterSlotTake();
                            processor.updateRecipe(true); //we need to update the processor as its possible that we used up an ingredient
                        }
                    }

                }

                if (!this.mergeItemStack(itemstack1, topSlots, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, topSlots, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }

    public int findSpace(ItemStack stackType, int from, int toEx) {
        int total = 0;
        int stackLimit = stackType.getMaxStackSize();
        for (int i = from; i < toEx; i++) {
            Slot slot = this.inventorySlots.get(i);
            ItemStack stack = slot.getStack();

            if (stack.isEmpty()) {
                total += Math.min(stackLimit, slot.getSlotStackLimit());
            } else if (areItemsAndTagsEqual(stackType, stack)) {
                total += Math.max(0, Math.min(stackLimit, slot.getSlotStackLimit())-stack.getCount());
            }
        }

        return total / stackType.getCount();
    }

    /**
     * Called when the container is closed.
     */
    public void onContainerClosed(PlayerEntity playerIn) {
        super.onContainerClosed(playerIn);
        //this.top.closeInventory(playerIn); //todo
    }


    public int getTopHeight() {
        return ui.getTopHeight();
    }

    public UI<?> getUi() {
        return ui;
    }

    public boolean isTopSmaller() {
        return topSmaller;
    }
    public boolean isBottomSmaller() {
        return bottomSmaller;
    }

    public int getXDiffHalf() {
        return xDiffHalf;
    }

    public int getTopXDiffHalf() {
        return (topSmaller?xDiffHalf:0);
    }

    public int getBottomXDiffHalf() {
        return (topSmaller?0:xDiffHalf);
    }

    @Override
    public void setItemFilter(IItemFilter filter) {
        UIContainer.this.itemFilter = filter;
    }

    public int getGuiWidth() {
        return GuiOffSide*2+ui.getTopWidth();
    }

    public int getGuiHeight() {
        return getTopHeight() +126+5*18; //todo change when we allow changing the player inv
    }

    //<editor-fold desc="RecipeBookContainer" defaultstate="collapsed">

    @Override
    public void func_201771_a(RecipeItemHelper p_201771_1_) {

    }

    public void clear() {

    }

    @Override
    public boolean matches(IRecipe recipeIn) {
        return false;
    }

    @Override
    public int getOutputSlot() {
        return 16;
    }

    //todo crafting height needs to be set
    @Override
    public int getWidth() {
        return 4;
    }

    @Override
    public int getHeight() {
        return 4;
    }

    @Override
    public int getSize() {
        return 16;
    }

    @Override
    public List<RecipeBookCategories> getRecipeBookCategories() {
        return DistExecutor.runForDist(()->()-> ClientContainerHelper.getRecipeBookCategories((OilContainerType) getType()), ()-> Collections::emptyList);
    }

    //</editor-fold>
}
