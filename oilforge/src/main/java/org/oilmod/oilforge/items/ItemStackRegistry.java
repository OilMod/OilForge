package org.oilmod.oilforge.items;

import gnu.trove.map.hash.THashMap;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ItemStackRegistry {
    public final static Pattern NAME_CHECKER = Pattern.compile("^[a-z0-9_]*$");
    private static ItemStackRegistry instance= new ItemStackRegistry();
    private ItemStackRegistry() {}

    public static ItemStackRegistry getInstance() {
        return instance;
    }

    private Map<String, ItemStackCreator> register = new THashMap<String, ItemStackCreator>();

    public void register(ItemStackCreator itemStackCreator) {
        Matcher matcher = NAME_CHECKER.matcher(itemStackCreator.getInternalName());
        if (matcher.matches()) {
            register.put(itemStackCreator.getInternalName(), itemStackCreator);
        } else {
            throw new IllegalStateException("'"+itemStackCreator.getInternalName()  + "' must only contain letters, numbers and underscores");
        }
    }


    public void unregister(ItemStackCreator itemStackCreator) {
        register.remove(itemStackCreator.getInternalName());
    }

    public ItemStackCreator get(String internalName) {
        return register.get(internalName);
    }
    public boolean exists(String internalName) {
        return register.containsKey(internalName);
    }


}