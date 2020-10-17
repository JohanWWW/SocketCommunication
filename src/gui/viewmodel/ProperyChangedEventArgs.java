package gui.viewmodel;

import events.EventArgs;

public class ProperyChangedEventArgs extends EventArgs {
    private final String _propertyName;

    public ProperyChangedEventArgs(String propertyName) {
        _propertyName = propertyName;
    }

    public String getPropertyName() {
        return _propertyName;
    }
}
