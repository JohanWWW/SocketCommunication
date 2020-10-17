package p2p.events;

/**
 * The event subscriber method that will be invoked when an event is raised
 * @param <TArgs> The {@link EventArgs} argument type that this event subscriber handles
 */
@FunctionalInterface
public interface EventSubscriber<TArgs extends EventArgs> {
    /**
     * This method is invoked by {@link Event}
     * @param eventSource A reference to the event owner instance that invoked the event
     * @param eventArgs An {@link EventArgs} instance that contains event data
     */
    void invoke(Object eventSource, TArgs eventArgs);
}
