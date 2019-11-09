package org.oilmod.oilforge.config;

import org.oilmod.api.config.Compound;
import org.oilmod.api.config.CompoundCreator;
import org.oilmod.api.config.DataIndexedEntry;
import org.oilmod.api.config.DataKeyedEntry;
import org.oilmod.api.config.DataList;

/**
 * Created by sirati97 on 25.06.2016 for spigot.
 */
public final class ConfigUtil {
    private ConfigUtil() {throw new UnsupportedOperationException();}


    public static void copyTo(Compound from, Compound to) {
        copyTo(from, to, to);
    }

    public static void copyTo(Compound from, Compound to, CompoundCreator creator) {
        for (DataKeyedEntry<?> entry:from) {
            switch (entry.getType()) {
                case List:
                    DataList<?> oldList = (DataList) entry.getValue();
                    DataList<?> newList = creator.createList(oldList.getType());
                    copyTo(oldList, newList, creator);
                    to.setList(entry.getKey(), newList);
                    break;
                case Subsection:
                    Compound newSubsection = creator.createCompound();
                    copyTo((Compound) entry.getValue(), newSubsection, creator);
                    to.set(entry.getKey(), newSubsection);
                    break;
                default:
                    to.set(entry);
            }
        }
    }


    public static void copyTo(DataList<?> from, DataList<?> to, CompoundCreator creator) {
        for (DataIndexedEntry<?> entry:from) {
            switch (entry.getType()) {
                case List:
                    DataList<?> oldList = (DataList) entry.getValue();
                    DataList<?> newList = creator.createList(oldList.getType());
                    copyTo(oldList, newList, creator);
                    //noinspection unchecked
                    ((DataList)to).append(newList);
                    break;
                case Subsection:
                    Compound newSubsection = creator.createCompound();
                    copyTo((Compound) entry.getValue(), newSubsection, creator);
                    //noinspection unchecked
                    ((DataList)to).append(newSubsection);
                    break;
                default:
                    //noinspection unchecked
                    ((DataList)to).append(entry.getValue());
            }
        }
    }
}
