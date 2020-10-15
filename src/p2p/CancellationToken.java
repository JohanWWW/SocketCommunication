package p2p;

public class CancellationToken {
    private Runnable _cancellationRequestSubscription;
    private boolean _isCancellationRequested = false;

    /**
     * Creates an event subscription
     */
    void setCancellationRequestSubscription(Runnable subscriber) {
        _cancellationRequestSubscription = subscriber;
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
        raiseOnCancellationRequestedEvent();
        return true;
    }

    public boolean isCancellationRequested() {
        return _isCancellationRequested;
    }

    private void raiseOnCancellationRequestedEvent() {
        if (_cancellationRequestSubscription != null)
            _cancellationRequestSubscription.run();
    }
}
