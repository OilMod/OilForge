package org.oilmod.oilforge.ui;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.network.NetworkHooks;
import org.oilmod.api.UI.IItemInteractionHandler;
import org.oilmod.api.UI.IItemRef;
import org.oilmod.api.UI.UI;
import org.oilmod.api.UI.UIMPI;
import org.oilmod.api.UI.slot.ISlotType;
import org.oilmod.api.crafting.ICraftingProcessor;
import org.oilmod.api.rep.crafting.IResultCategory;
import org.oilmod.api.rep.entity.EntityPlayerRep;
import org.oilmod.api.rep.inventory.InventoryRep;
import org.oilmod.api.rep.inventory.InventoryView;
import org.oilmod.oilforge.inventory.container.OilContainerType;
import org.oilmod.oilforge.ui.container.SetUIPacket;
import org.oilmod.oilforge.ui.container.UIContainer;
import org.oilmod.oilforge.ui.container.slot.RealSlotTypeBase;

import javax.annotation.Nullable;

import static org.oilmod.oilforge.Util.toForge;
import static org.oilmod.oilforge.Util.toOil;
import static org.oilmod.oilforge.inventory.container.ContainerUtil.*;

public class UIHelper extends UIMPI.Helper<UIHelper> {
    public final static VanillaItemInteractionHandler nativeHandler = new VanillaItemInteractionHandler();

    @Override
    protected void openUI(EntityPlayerRep playerRep, UI ui) {
        SetUIPacket packet = new SetUIPacket(ui.getFactory(), ui.getContext());
        PlayerEntity player = toForge(playerRep);
        if (player instanceof ServerPlayerEntity) {
            NetworkHooks.openGui((ServerPlayerEntity) player, new INamedContainerProvider() {

                @Nullable
                @Override
                public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity playerEntity) {
                    return new UIContainer(OilContainerType.CUSTOM_UI, id, playerInventory, ui);
                }

                @Override
                public ITextComponent getDisplayName() {
                    return new StringTextComponent("Test");
                }
            }, packet::encode);
        }
        //otherwise we are clientside maybe



    }

    @Override
    protected void handleNative(IItemRef handle, InventoryRep inv, ISlotType type) {
        RealItemRef ref = (RealItemRef) handle;
        if (inv instanceof InventoryView) {
            InventoryView view = (InventoryView) inv;
            ref.fixIndexForView(view);
            inv = view.getRoot();
        }
        ref.setNative(toForge(inv), type);
    }

    @Override
    protected void handleCustom(IItemRef handle, InventoryRep inv, ISlotType type) {
        ((RealItemRef)handle).setCustom(inv, type);

    }

    @Override
    protected void handleCustom(IItemRef handle, InventoryRep inv, IItemInteractionHandler handler) {
        ((RealItemRef)handle).setCustom(inv, handler);

    }

    @Override
    protected int getSizeSlots() {
        return GuiSlotSize;
    }

    @Override
    protected int getSizeBorder() {
        return GuiOffSide;
    }

    @Override
    protected int getSizeText() {
        return GuiOffTop-GuiOffSide;
    }

    @Override
    protected int getSizeItemRender() {
        return 16;
    }

    @Override
    protected ISlotType getNormalSlotType() {
        return new RealSlotTypeBase() {
            @Override
            public boolean isSettable() {
                return true;
            }

            @Override
            public boolean isTakeable() {
                return true;
            }
        };
    }

    @Override
    protected ISlotType getTakeOnlySlotType() {
        return new RealSlotTypeBase() {
            @Override
            public boolean isSettable() {
                return false;
            }

            @Override
            public boolean isTakeable() {
                return true;
            }
        };
    }

    @Override
    protected ISlotType getProcessingSlotType(ICraftingProcessor processor, IResultCategory[] categories) {
        return new SlotTypeProcessing(processor, categories);
    }
}
