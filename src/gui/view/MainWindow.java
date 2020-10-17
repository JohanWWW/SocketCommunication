package gui.view;

import events.Event;
import events.EventArgs;
import events.EventSubscriber;
import gui.viewmodel.MainWindowViewModel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainWindow extends Frame {
    private MainWindowViewModel mainWindowViewModel;

    private JTextField senderPortNumberTextField;
    private JTextField receiverPortNumberTextField;
    private JTextField ipAddressTextField;
    private JTextField messageTextField;
    private JButton beginListenButton;
    private JButton transmitButton;

    public MainWindow() {
        setTitle("Transmit Message");
        setSize(250, 400);
        setLayout(new FlowLayout());

        initializeViewModels();
        initializeComponents();

        var mainPanel = new Panel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));

        var senderPortNumberPanel = createLabelledTextFieldArea("Sender Port", senderPortNumberTextField);
        var receiverPortNumberPanel = createLabelledTextFieldArea("Receiver Port", receiverPortNumberTextField);
        var ipAddressPanel = createLabelledTextFieldArea("IP Address", ipAddressTextField);
        var messagePanel = createLabelledTextFieldArea("Message", messageTextField);

        mainPanel.add(senderPortNumberPanel);
        mainPanel.add(beginListenButton);
        mainPanel.add(receiverPortNumberPanel);
        mainPanel.add(ipAddressPanel);
        mainPanel.add(messagePanel);
        mainPanel.add(transmitButton);

        add(mainPanel);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
        setVisible(true);
    }

    private void initializeViewModels() {
        mainWindowViewModel = new MainWindowViewModel();
        mainWindowViewModel.addMemberValueChangedEventSubscriber((eventSource, args) -> {
            switch (args.getMemberName()) {
                case MainWindowViewModel.Member.senderPortNumber ->
                        SwingUtilities.invokeLater(() -> setSenderPortNumberText(mainWindowViewModel.getSenderPortNumber()));

                case MainWindowViewModel.Member.receiverPortNumber ->
                        SwingUtilities.invokeLater(() -> setReceiverPortNumber(mainWindowViewModel.getReceiverPortNumber()));

                case MainWindowViewModel.Member.ipAddress ->
                        SwingUtilities.invokeLater(() -> setIpAddressText(mainWindowViewModel.getIpAddress()));

                case MainWindowViewModel.Member.message ->
                        SwingUtilities.invokeLater(() -> setMessageText(mainWindowViewModel.getMessage()));
            }
        });
    }

    private void initializeComponents() {
        // Initialize private variables
        senderPortNumberTextField = new JTextField("3000");
        receiverPortNumberTextField = new JTextField("3000");
        ipAddressTextField = new JTextField("localhost");
        messageTextField = new JTextField("");
        beginListenButton = new JButton("Begin");
        transmitButton = new JButton("Transmit");

        // Subscribe on listeners
        senderPortNumberTextField.getDocument().addDocumentListener(TextFieldListener.on(senderPortNumberTextField, (s, e) -> mainWindowViewModel.setSenderPortNumber(e.getValue())));
        receiverPortNumberTextField.getDocument().addDocumentListener(TextFieldListener.on(receiverPortNumberTextField, (s, e) -> mainWindowViewModel.setReceiverPortNumber(e.getValue())));
        ipAddressTextField.getDocument().addDocumentListener(TextFieldListener.on(ipAddressTextField, (s, e) -> mainWindowViewModel.setIpAddress(e.getValue())));
        messageTextField.getDocument().addDocumentListener(TextFieldListener.on(messageTextField, (s, e) -> mainWindowViewModel.setMessage(e.getValue())));
        beginListenButton.addActionListener(a -> mainWindowViewModel.createPeer());
        transmitButton.addActionListener(a -> mainWindowViewModel.transmitMessage());
    }

    private Panel createLabelledTextFieldArea(String labelValue, JTextField textField) {
        var area = new Panel();
        area.setLayout(new BoxLayout(area, BoxLayout.Y_AXIS));
        area.add(new Label(labelValue));
        area.add(textField);
        return area;
    }

    private void setSenderPortNumberText(String value) {
        if (!senderPortNumberTextField.getText().equals(value))
            senderPortNumberTextField.setText(value);
    }

    private void setReceiverPortNumber(String value) {
        if (!receiverPortNumberTextField.getText().equals(value))
            receiverPortNumberTextField.setText(value);
    }

    private void setIpAddressText(String value) {
        if (!ipAddressTextField.getText().equals(value))
            ipAddressTextField.setText(value);
    }

    private void setMessageText(String value) {
        if (!messageTextField.getText().equals(value))
            messageTextField.setText(value);
    }
}

class TextFieldListener implements DocumentListener {
    private final JTextField sender;

    private final Event<TextFieldEventArgs> onInserted;
    private final Event<TextFieldEventArgs> onRemoved;
    private final Event<TextFieldEventArgs> onChanged;

    private TextFieldListener(JTextField sender, EventSubscriber<TextFieldEventArgs> subscriber) {
        onInserted = new Event<>(this);
        onRemoved = new Event<>(this);
        onChanged = new Event<>(this);
        onInserted.subscribe(subscriber);
        onRemoved.subscribe(subscriber);
        onChanged.subscribe(subscriber);
        this.sender = sender;
    }

    public static TextFieldListener on(JTextField source, EventSubscriber<TextFieldEventArgs> subscriber) {
        return new TextFieldListener(source, subscriber);
    }

    public TextFieldListener andAdd(EventSubscriber<TextFieldEventArgs> subscriber) {
        onInserted.subscribe(subscriber);
        onRemoved.subscribe(subscriber);
        onChanged.subscribe(subscriber);
        return this;
    }

    public void insertUpdate(DocumentEvent e) {
        onInserted.raise(new TextFieldEventArgs(sender.getText()));
    }

    public void removeUpdate(DocumentEvent e) {
        onRemoved.raise(new TextFieldEventArgs(sender.getText()));
    }

    public void changedUpdate(DocumentEvent e) {
        onChanged.raise(new TextFieldEventArgs(sender.getText()));
    }
}

class TextFieldEventArgs extends EventArgs {
    private final String value;

    public TextFieldEventArgs(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
