package gui.view.components;

import events.Event;
import events.EventArgs;
import events.EventSubscriber;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class XButton extends JButton {
    private Event<ButtonClickedEventArgs> onClicked;

    public XButton() {
        super();
        initialize();
    }

    public XButton(String text) {
        super(text);
        initialize();
    }

    private void initialize() {
        initializeEventHandlers();
        addActionListener(new EventRouter(onClicked));
    }

    private void initializeEventHandlers() {
        onClicked = new Event<>(this);
    }

    public void addOnClickedSubscriber(EventSubscriber<ButtonClickedEventArgs> eventSubscriber) {
        onClicked.subscribe(eventSubscriber);
    }

    public static class ButtonClickedEventArgs extends EventArgs {

    }

    private static class EventRouter implements ActionListener {
        private final Event<ButtonClickedEventArgs> onClickedEventHandlerReference;

        public EventRouter(Event<ButtonClickedEventArgs> onClickedEventHandlerReference) {
            this.onClickedEventHandlerReference = onClickedEventHandlerReference;
        }

        public void actionPerformed(ActionEvent e) {
            onClickedEventHandlerReference.raise(new ButtonClickedEventArgs());
        }
    }
}
