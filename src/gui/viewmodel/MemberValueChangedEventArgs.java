package gui.viewmodel;

import events.EventArgs;

public class MemberValueChangedEventArgs extends EventArgs {
    private final String _memberName;

    public MemberValueChangedEventArgs(String memberName) {
        _memberName = memberName;
    }

    public String getMemberName() {
        return _memberName;
    }
}
