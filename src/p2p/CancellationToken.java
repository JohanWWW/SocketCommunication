package p2p;

import events.Event;
import events.EventArgs;
import events.EventSubscriber;

public class CancellationToken {
    private boolean _isCancellationRequested = false;

    private final Event<EventArgs> _onCancellationRequested;

    public CancellationToken() {
        _onCancellationRequested = new Event<>(this);
    }

    /**
     * Creates an event subscription
     */
    void addCancellationRequestedSubscriber(EventSubscriber<EventArgs> subscriber) {
        _onCancellationRequested.subscribe(subscriber);
    }

    /**
     * Sets the state to cancelled
     *
     * @return false if already cancelled otherwise true
     */
    public boolean cancel() {
        if (_isCancellationRequested)
            return false;
        _isCancellationRequested = true;
        _onCancellationRequested.raise(new EventArgs());
        return true;
    }

    public boolean isCancellationRequested() {
        return _isCancellationRequested;
    }
}
