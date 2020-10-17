package gui.viewmodel;

import events.Event;
import events.EventSubscriber;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public abstract class ViewModelBase {
    private final Event<ProperyChangedEventArgs> _onPropertyChanged;

    public ViewModelBase() {
        _onPropertyChanged = new Event<>(this);
    }

    public void addPropertyChangedEventSubscriber(EventSubscriber<ProperyChangedEventArgs> subscriber) {
        _onPropertyChanged.subscribe(subscriber);
    }

    protected <TProp> boolean setProperty(TProp newValue, TProp oldValue, Runnable setter, String propertyName) {
        if (!newValue.equals(oldValue)) {
            setter.run();
            _onPropertyChanged.raiseAsync(new ProperyChangedEventArgs(propertyName));
            return true;
        }
        return false;
    }
}
