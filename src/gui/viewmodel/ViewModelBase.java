package gui.viewmodel;

import events.Event;
import events.EventSubscriber;

public abstract class ViewModelBase {
    private final Event<MemberValueChangedEventArgs> _onMemberValueChanged;

    public ViewModelBase() {
        _onMemberValueChanged = new Event<>(this);
    }

    public void addMemberValueChangedEventSubscriber(EventSubscriber<MemberValueChangedEventArgs> subscriber) {
        _onMemberValueChanged.subscribe(subscriber);
    }

    protected <TMem> boolean setMemberValue(TMem newValue, TMem oldValue, Runnable setter, String memberName) {
        if (!newValue.equals(oldValue)) {
            setter.run();
            _onMemberValueChanged.raiseAsync(new MemberValueChangedEventArgs(memberName));
            return true;
        }
        return false;
    }
}
