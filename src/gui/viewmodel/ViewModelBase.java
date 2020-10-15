package gui.viewmodel;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public abstract class ViewModelBase {
    private final Set<Consumer<ProperyChangedEventArgs>> _subscribers;

    public ViewModelBase() {
        _subscribers = new HashSet<>();
    }

    public void addPropertyChangedEventSubscriber(Consumer<ProperyChangedEventArgs> subscriber) {
        _subscribers.add(subscriber);
    }

    public void releaseSubscribers() {
        _subscribers.clear();
    }

    protected <TProp> boolean setProperty(TProp newValue, TProp oldValue, Runnable setter, String propertyName) {
        if (!newValue.equals(oldValue)) {
            setter.run();
            raiseOnPropertyChanged(new ProperyChangedEventArgs<>(propertyName, newValue, oldValue));
            return true;
        }
        return false;
    }

    protected <TProp> void raiseOnPropertyChanged(ProperyChangedEventArgs<TProp> args) {
        _subscribers.forEach(subscriber -> {
            if (subscriber != null)
                subscriber.accept(args);
        });
    }
}
