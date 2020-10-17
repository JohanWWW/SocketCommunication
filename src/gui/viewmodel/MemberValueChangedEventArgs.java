package gui.viewmodel;

import events.EventArgs;

public class MemberValueChangedEventArgs extends EventArgs {
    private final String memberName;

    public MemberValueChangedEventArgs(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberName() {
        return memberName;
    }
}
