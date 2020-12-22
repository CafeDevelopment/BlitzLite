package club.cafedevelopment.blitzlite;

import java.util.function.Consumer;

import java.util.*;

/**
 * @author Reap
 *
 * This is the event bus. You should (almost) always only have 1 instance of it.
 */
@SuppressWarnings({"unused", "unchecked"})
public class BlitzBus {
    /**
     * All the registered listeners, separated by listening objects and event types.
     */
    private final HashMap<Object, HashMap<Class<?>, List<Consumer<?>>>> listenerMap = new HashMap<>();

    /**
     * Registers an object to the {@link #listenerMap}.
     * @param obj the object to scan, filter and put in the {@link #listenerMap}
     */
    public void register(Object obj) {
        if (listenerMap.containsKey(obj)) return;

        HashMap<Class<?>, List<Consumer<?>>> filteredMap = new HashMap<>();

        Arrays.stream(obj.getClass().getDeclaredFields())
                .filter(it -> it.getType().isAssignableFrom(Consumer.class))
                .filter(it -> it.isAnnotationPresent(BlitzEntry.class))
                .sorted(Comparator.comparing(it -> -it.getDeclaredAnnotation(BlitzEntry.class).priority()))
                .forEach(it -> {
                    try {
                        Consumer<?> consumer = (Consumer<?>) it.get(obj);

                        if (consumer != null && it.isAnnotationPresent(BlitzEntry.class)) {
                            Class<?> clazz = it.getDeclaredAnnotation(BlitzEntry.class).type();
                            List<Consumer<?>> list = filteredMap.containsKey(clazz) ? filteredMap.get(clazz) : new ArrayList<>();

                            list.add(consumer);
                            filteredMap.put(clazz, list);
                        }
                    } catch (IllegalAccessException e) { e.printStackTrace(); }
                });
        listenerMap.put(obj, filteredMap);
    }

    /**
     * Attempts to remove an object from the {@link #listenerMap}.
     * @param obj the object to attempt removing.
     */
    public void unregister(Object obj) { listenerMap.remove(obj); }

    /**
     * Dispatches an event.
     * @param event the event to dispatch.
     * @param <T> the type for the event.
     */
    public <T> void dispatch(T event) {
        for (HashMap<Class<?>, List<Consumer<?>>> map : listenerMap.values())
            for (Consumer<?> consumer : map.get(event.getClass()))
                ((Consumer<T>) consumer).accept(event);
    }
}
