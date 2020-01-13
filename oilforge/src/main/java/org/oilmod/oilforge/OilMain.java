package org.oilmod.oilforge;


import org.apache.commons.lang3.Validate;
import org.oilmod.api.OilMod;
import org.oilmod.api.inventory.InventoryFactory;
import org.oilmod.api.items.EnchantmentType;
import org.oilmod.api.items.internal.ItemClassMap;
import org.oilmod.api.items.internal.ItemFactory;
import org.oilmod.api.items.type.ItemImplementationProvider;
import org.oilmod.api.items.type.ItemTypeHelper;
import org.oilmod.api.items.type.TBBType;
import org.oilmod.api.rep.RepAPI;
import org.oilmod.oilforge.enchantments.RealEnchantmentTypeHelper;
import org.oilmod.oilforge.inventory.RealInventoryFactory;
import org.oilmod.oilforge.inventory.container.ContainerPackageHandler;
import org.oilmod.oilforge.items.*;
import org.oilmod.oilforge.items.tools.RealTBBHelper;
import org.oilmod.oilforge.rep.RepAPIImpl;
import org.oilmod.spi.MPILoader;

public class OilMain {
    public static OilMod ModMinecraft;
    public static OilMod ModOilMod;


    public static void init() {
        ContainerPackageHandler.registerPackets();
        MPILoader.init(); //this gonna get more complicated in the future but its okay for now
        RepAPI.installImplementation(new RepAPIImpl());


        RealItemClassMap itemClassMap = new RealItemClassMap();
        MPILoader.commitDependency(itemClassMap); //for RealItemRegistryHelper
        Validate.notNull(RealItemRegistryHelper.INSTANCE);



        ItemFactory.setInstance(new RealItemFactory());
        ItemClassMap.setInstance(itemClassMap);
        InventoryFactory.setInstance(new RealInventoryFactory());

        EnchantmentType.EnchantmentTypeHelper.setInstance(new RealEnchantmentTypeHelper());
        ItemTypeHelper.setInstance(new RealItemTypeHelper());
        TBBType.TBBHelper.setInstance(new RealTBBHelper());
        initReflection();
        //YBase.registerYAMLClasses();

        OilAPIInitEvent.fire();
        //todo by here API should be completely initialised, i.e. no hanging dependencies MPILoader.xXX()
    }

    public static void printTrace(String text) {
        System.out.println("Printing stack trace for " + text + ":");
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        for (int i = 2; i < elements.length; i++) {
            StackTraceElement s = elements[i];
            System.out.println("\tat " + s.getClassName() + "." + s.getMethodName()
                    + "(" + s.getFileName() + ":" + s.getLineNumber() + ")");
        }
    }


    public static void initReflection() {
        //should i ever use reflection - check it here to fire missing fields exceptions early
    }
}
