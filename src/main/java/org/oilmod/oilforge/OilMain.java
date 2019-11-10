package org.oilmod.oilforge;


import org.oilmod.api.OilMod;
import org.oilmod.api.inventory.InventoryFactory;
import org.oilmod.api.items.ItemRegistry;
import org.oilmod.api.items.internal.ItemClassMap;
import org.oilmod.api.items.internal.ItemFactory;
import org.oilmod.api.items.type.ImplementationProvider;
import org.oilmod.api.items.type.ItemTypeHelper;
import org.oilmod.api.items.type.TBBType;
import org.oilmod.api.rep.RepAPI;
import org.oilmod.api.util.OilUtil;
import org.oilmod.oilforge.inventory.RealInventoryFactory;
import org.oilmod.oilforge.items.*;
import org.oilmod.oilforge.items.tools.RealTBBHelper;
import org.oilmod.oilforge.modloader.RealModHelper;
import org.oilmod.oilforge.rep.RepAPIImpl;

public class OilMain {
    public static OilMod ModMinecraft;
    public static OilMod ModOilMod;
    public static RealItemRegistryHelper realItemRegistryHelper;

    public static void init() {
        RepAPI.installImplementation(new RepAPIImpl());

        OilMod.ModHelper.setInstance(new RealModHelper());
        ModMinecraft  =  new OilMod("minecraft", "Minecraft");
        ModOilMod  =  new OilMod("oilmod", "OilMod");
        ItemFactory.setInstance(new RealItemFactory());
        RealItemClassMap itemClassMap = new RealItemClassMap();
        ItemClassMap.setInstance(itemClassMap);
        ItemRegistry.ItemRegistryHelper.setInstance(realItemRegistryHelper = new RealItemRegistryHelper(itemClassMap));
        OilUtil.UtilImpl.setInstance(new RealOilUtil());
        InventoryFactory.setInstance(new RealInventoryFactory());
        ImplementationProvider.IPHelper.setInstance(new RealIPHelper());
        /*UIHelper.setInstance(new RealUIHelper());
        ItemCraftingFactory.setInstance(new RealItemCraftingFactory());
        BlockType.BlockTypeHelper.setInstance(new RealBlockTypeHelper());
        EnchantmentType.EnchantmentTypeHelper.setInstance(new RealEnchantmentTypeHelper());*/
        ItemTypeHelper.setInstance(new RealItemTypeHelper());
        TBBType.TBBHelper.setInstance(new RealTBBHelper());
        initReflection();
        //YBase.registerYAMLClasses();
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
