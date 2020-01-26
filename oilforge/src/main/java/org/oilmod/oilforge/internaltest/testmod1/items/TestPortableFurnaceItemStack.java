package org.oilmod.oilforge.internaltest.testmod1.items;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.thread.SidedThreadGroups;
import org.oilmod.api.OilMod;
import org.oilmod.api.inventory.InventoryFactory;
import org.oilmod.api.inventory.ModFurnaceInventoryObject;
import org.oilmod.api.items.NMSItemStack;
import org.oilmod.api.items.OilItem;
import org.oilmod.api.items.OilItemStack;
import org.oilmod.api.rep.world.WorldRep;
import org.oilmod.api.util.ITicker;
import org.oilmod.api.util.Tickable;
import org.oilmod.oilforge.rep.location.WorldFR;

import static org.oilmod.oilforge.modloader.OilModLoaderMod.serverWorldDimOverworld;

public class TestPortableFurnaceItemStack extends OilItemStack {
    private static ITicker ticker = new ITicker() {
        @Override
        public boolean isRemote() {
            return Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER;
        }

        @Override
        public WorldRep getMainWorld() {

            return new WorldFR(isRemote() ? serverWorldDimOverworld : Minecraft.getInstance().world);
        }

        @Override
        public void add(Tickable tickable) {

        }

        @Override
        public void remove(Tickable tickable) {

        }

        @Override
        public void resume() {

        }

        @Override
        public void pause() {

        }

        @Override
        public void stopAndDispose() {

        }

        @Override
        public int getTickRate() {
            return 1;
        }

        @Override
        public int getSimulationSpeed() {
            return 1;
        }

        @Override
        public OilMod getMod() {
            return null;
        }
    }; //for now so we dont get a null pointer
    private ModFurnaceInventoryObject inventory;


    private final static InventoryFactory.Builder<ModFurnaceInventoryObject> invBuilder = InventoryFactory
            .builder("items")
            .standardTitle("Portable Furnace")
            .ticker(ticker)
            .filter(PortableInventoryFilter.INSTANCE)
            .mainInventory()
            .furnace();

    public TestPortableFurnaceItemStack(NMSItemStack nmsItemStack, OilItem item) {
        super(nmsItemStack, item);
        inventory = invBuilder.create(this);
    }


}
