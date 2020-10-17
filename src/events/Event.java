package events;

import java.util.HashSet;
import java.util.Set;

/**
 * This class represents an event
 * @param <TArgs> The {@link EventArgs} type that this event handles
 */
public final class Event<TArgs extends EventArgs> {
    private final Object eventSource;
    private final Set<EventSubscriber<TArgs>> subscribers;

    /**
     * @param eventSource A reference to the instance that will raise this event
     */
    public Event(Object eventSource) {
        subscribers = new HashSet<>();
        this.eventSource = eventSource;
    }

    /**
     * Binds an event subscriber to this event
     * @param subscriber The event subscriber to be bound
     */
    public void subscribe(EventSubscriber<TArgs> subscriber) {
        subscribers.add(subscriber);
    }

    /**
     * Unbinds an event subscriber from this event
     * @param subscriber A reference to the event subscriber to be unbound
     */
    public void unsubscribe(EventSubscriber<TArgs> subscriber) {
        subscribers.remove(subscriber);
    }

    /**
     * Raises this event. This method should be called by a parent instance.
     * @param args An {@link EventArgs} instance to be published to all event subscribers
     */
    public void raise(TArgs args) {
        subscribers.forEach(subscriber -> raiseIfNotNull(subscriber, args));
    }

    /**
     * Raises this event asynchronously. This method should be called by a parent instance.
     * @param args An {@link EventArgs} instance to be published to all event subscribers
     */
    public void raiseAsync(TArgs args) {
        subscribers.parallelStream().forEach(subscriber -> raiseIfNotNull(subscriber, args));
    }

    private void raiseIfNotNull(EventSubscriber<TArgs> subscriber, TArgs args) {
        if (subscriber != null)
            subscriber.invoke(eventSource, args);
    }
}
