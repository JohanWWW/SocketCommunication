package p2p;

import events.Event;
import events.EventArgs;
import events.EventSubscriber;

public class CancellationToken {
    private boolean isCancellationRequested = false;

    private final Event<EventArgs> onCancellationRequested;

    public CancellationToken() {
        onCancellationRequested = new Event<>(this);
    }

    /**
     * Creates an event subscription
     */
    void addCancellationRequestedSubscriber(EventSubscriber<EventArgs> subscriber) {
        onCancellationRequested.subscribe(subscriber);
    }

    /**
     * Sets the state to cancelled
     *
     * @return false if already cancelled otherwise true
     */
    public boolean cancel() {
        if (isCancellationRequested)
            return false;
        isCancellationRequested = true;
        onCancellationRequested.raise(new EventArgs());
        return true;
    }

    public boolean isCancellationRequested() {
        return isCancellationRequested;
    }
}
