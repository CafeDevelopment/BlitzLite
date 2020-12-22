package club.cafedevelopment.blitzlite.example;

import club.cafedevelopment.blitzlite.BlitzBus;
import club.cafedevelopment.blitzlite.BlitzEntry;

import java.util.function.Consumer;

@SuppressWarnings("unused")
public class Example {
    public static final BlitzBus BLITZ_BUS = new BlitzBus();

    public static void main(String[] args) {
        Example instance = new Example();
        BLITZ_BUS.register(instance);
        BLITZ_BUS.dispatch(new Event1());
        BLITZ_BUS.dispatch(new Event2(10, 10.5, "11"));
        BLITZ_BUS.unregister(instance);
        BLITZ_BUS.dispatch(new Event1()); // Won't output anything because of instance being unregistered.
    }

    @BlitzEntry(type = Event1.class, priority = -1)
    public Consumer<Event1> onEvent1_2 = event -> System.out.println("This will always happen AFTER the consumer below.");

    @BlitzEntry(type = Event1.class)
    public Consumer<Event1> onEvent1 = event -> System.out.println("A new Event1 was called!");

    @BlitzEntry(type = Event2.class)
    public Consumer<Event2> onEvent2 = event -> {
        System.out.println(event.getI());
        System.out.println(event.getD());
        System.out.println(event.getS());
    };

    public static class Event1 {}
    public static class Event2 {
        private final int i;
        private final double d;
        private final String s;

        public Event2(int i, double d, String s) {
            this.i = i;
            this.d = d;
            this.s = s;
        }

        public int getI() { return i; }
        public double getD() { return d; }
        public String getS() { return s; }
    }
}
