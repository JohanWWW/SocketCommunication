package gui.viewmodel;

public class ProperyChangedEventArgs<TProp> {
    private final String _propertyName;
    private final TProp _newValue;
    private final TProp _oldValue;

    public ProperyChangedEventArgs(String propertyName, TProp newValue, TProp oldValue) {
        _propertyName = propertyName;
        _oldValue = oldValue;
        _newValue = newValue;
    }

    public String getPropertyName() {
        return _propertyName;
    }

    public TProp getNewValue() {
        return _newValue;
    }

    public TProp getOldValue() {
        return _oldValue;
    }
}
