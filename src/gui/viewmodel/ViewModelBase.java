package gui.viewmodel;

import events.Event;
import events.EventSubscriber;

public abstract class ViewModelBase {
    private final Event<MemberValueChangedEventArgs> onMemberValueChanged;

    public ViewModelBase() {
        onMemberValueChanged = new Event<>(this);
    }

    public void addMemberValueChangedEventSubscriber(EventSubscriber<MemberValueChangedEventArgs> subscriber) {
        onMemberValueChanged.subscribe(subscriber);
    }

    protected <TMem> boolean setMemberValue(TMem newValue, TMem oldValue, Runnable setter, String memberName) {
        if (!newValue.equals(oldValue)) {
            setter.run();
            onMemberValueChanged.raiseAsync(new MemberValueChangedEventArgs(memberName));
            return true;
        }
        return false;
    }
}
