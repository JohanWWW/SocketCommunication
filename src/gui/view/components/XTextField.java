package gui.view.components;

import events.Event;
import events.EventArgs;
import events.EventSubscriber;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class XTextField extends JTextField {
    private Event<TextFieldEventArgs> onInserted;
    private Event<TextFieldEventArgs> onRemoved;
    private Event<TextFieldEventArgs> onChanged;
    private Event<TextFieldEventArgs> onUpdated;

    public XTextField() {
        super();
        initialize();
    }

    public XTextField(String text) {
        super(text);
        initialize();
    }

    private void initialize() {
        initializeEventHandlers();
        getDocument().addDocumentListener(new EventRouter(this, onInserted, onRemoved, onChanged, onUpdated));
    }

    private void initializeEventHandlers() {
        onInserted = new Event<>(this);
        onRemoved = new Event<>(this);
        onChanged = new Event<>(this);
        onUpdated = new Event<>(this);
    }

    public void addOnInsertSubscriber(EventSubscriber<TextFieldEventArgs> eventSubscriber) {
        onInserted.subscribe(eventSubscriber);
    }

    public void addOnRemoveSubscriber(EventSubscriber<TextFieldEventArgs> eventSubscriber) {
        onRemoved.subscribe(eventSubscriber);
    }

    public void addOnChangeSubscriber(EventSubscriber<TextFieldEventArgs> eventSubscriber) {
        onChanged.subscribe(eventSubscriber);
    }

    public void addOnUpdateSubscriber(EventSubscriber<TextFieldEventArgs> eventSubscriber) {
        onUpdated.subscribe(eventSubscriber);
    }

    public static class TextFieldEventArgs extends EventArgs {
    }

    // This class internally listens for changes and routes them to the event handlers
    private static class EventRouter implements DocumentListener {
        private final Event<TextFieldEventArgs> onInsertedReference;
        private final Event<TextFieldEventArgs> onRemovedReference;
        private final Event<TextFieldEventArgs> onChangedReference;
        private final Event<TextFieldEventArgs> onUpdatedReference;

        private final JTextField source;

        public EventRouter(JTextField source,
                           Event<TextFieldEventArgs> onInsertedReference,
                           Event<TextFieldEventArgs> onRemovedReference,
                           Event<TextFieldEventArgs> onChangedReference,
                           Event<TextFieldEventArgs> onUpdatedReference) {
            this.source = source;
            this.onInsertedReference = onInsertedReference;
            this.onRemovedReference = onRemovedReference;
            this.onChangedReference = onChangedReference;
            this.onUpdatedReference = onUpdatedReference;
        }

        public void insertUpdate(DocumentEvent e) {
            var args = new TextFieldEventArgs();
            this.onInsertedReference.raise(args);
            this.onUpdatedReference.raise(args);
        }

        public void removeUpdate(DocumentEvent e) {
            var args = new TextFieldEventArgs();
            this.onRemovedReference.raise(args);
            this.onUpdatedReference.raise(args);
        }

        public void changedUpdate(DocumentEvent e) {
            var args = new TextFieldEventArgs();
            this.onChangedReference.raise(args);
            this.onUpdatedReference.raise(args);
        }
    }
}
