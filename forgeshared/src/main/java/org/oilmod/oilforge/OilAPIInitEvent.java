package org.oilmod.oilforge;

import net.minecraftforge.fml.ModList;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.ForkJoinWorkerThread;

public class OilAPIInitEvent{
    private static Logger LOGGER = LogManager.getLogger();
    static {
        noOp();
        LOGGER.info("Loaded OilAPIInitEvent with classloader {}", OilAPIInitEvent.class::getClassLoader);
    }

    private static Set<Runnable> eventSubs = new HashSet<>();

    private static synchronized void noOp(){}

    public static synchronized void addListener(Runnable r) {
        if (eventSubs == null) {
            r.run();
            return;
        }
        eventSubs.add(r);
    }

    public static synchronized void fire(){
        Validate.notNull(eventSubs, "Cannot call fire twice");
        LOGGER.debug("Sending OilAPIInitEvent to {} subscribers", eventSubs::size);

        try {
            Runnable r = ()->eventSubs.parallelStream().forEach(Runnable::run);
            try {
                ForkJoinTask task = null;
                //lets use forge threat pool, so no one can tell us we made stuff slow
                if (Thread.currentThread() instanceof ForkJoinWorkerThread) {
                    task= ((ForkJoinWorkerThread)Thread.currentThread()).getPool().submit(r);
                } else if (ModList.get() != null) {
                    Field f = ModList.class.getDeclaredField("modLoadingThreadPool");
                    f.setAccessible(true);
                    ForkJoinPool pool= (ForkJoinPool) f.get(ModList.get());
                    task =pool.submit(r) ;
                } else {
                    r.run();
                }
                if (task != null) {
                    task.join();
                }
            } catch (IllegalAccessException | NoSuchFieldException e) {
                LOGGER.error("Could not retrieve forge's mod-loading threat pool: {}", e.toString());
                r.run();
            }
        } catch (RuntimeException e) {
            LOGGER.error("Could not fire OilAPIInitEvent:", e);
            throw e;
        }
        eventSubs = null;
    }


}
