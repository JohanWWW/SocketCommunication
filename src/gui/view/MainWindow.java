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
import java.util.function.Consumer;

public class MainWindow extends Frame {
    private MainWindowViewModel _mainWindowViewModel;

    private JTextField _senderPortNumberTextField;
    private JTextField _receiverPortNumberTextField;
    private JTextField _ipAddressTextField;
    private JTextField _messageTextField;
    private JButton _beginListenButton;
    private JButton _transmitButton;

    public MainWindow() {
        setTitle("Transmit Message");
        setSize(250, 400);
        setLayout(new FlowLayout());

        initializeViewModels();
        initializeComponents();

        var mainPanel = new Panel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));

        var senderPortNumberPanel = createLabelledTextFieldArea("Sender Port", _senderPortNumberTextField);
        var receiverPortNumberPanel = createLabelledTextFieldArea("Receiver Port", _receiverPortNumberTextField);
        var ipAddressPanel = createLabelledTextFieldArea("IP Address", _ipAddressTextField);
        var messagePanel = createLabelledTextFieldArea("Message", _messageTextField);

        mainPanel.add(senderPortNumberPanel);
        mainPanel.add(_beginListenButton);
        mainPanel.add(receiverPortNumberPanel);
        mainPanel.add(ipAddressPanel);
        mainPanel.add(messagePanel);
        mainPanel.add(_transmitButton);

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
        _mainWindowViewModel = new MainWindowViewModel();
        _mainWindowViewModel.addMemberValueChangedEventSubscriber((eventSource, args) -> {
            switch (args.getMemberName()) {
                case MainWindowViewModel.Member.senderPortNumber ->
                        SwingUtilities.invokeLater(() -> setSenderPortNumberText(_mainWindowViewModel.getSenderPortNumber()));

                case MainWindowViewModel.Member.receiverPortNumber ->
                        SwingUtilities.invokeLater(() -> setReceiverPortNumber(_mainWindowViewModel.getReceiverPortNumber()));

                case MainWindowViewModel.Member.ipAddress ->
                        SwingUtilities.invokeLater(() -> setIpAddressText(_mainWindowViewModel.getIpAddress()));

                case MainWindowViewModel.Member.message ->
                        SwingUtilities.invokeLater(() -> setMessageText(_mainWindowViewModel.getMessage()));
            }
        });
    }

    private void initializeComponents() {
        // Initialize private variables
        _senderPortNumberTextField = new JTextField("3000");
        _receiverPortNumberTextField = new JTextField("3000");
        _ipAddressTextField = new JTextField("localhost");
        _messageTextField = new JTextField("");
        _beginListenButton = new JButton("Begin");
        _transmitButton = new JButton("Transmit");

        // Subscribe on listeners
        _senderPortNumberTextField.getDocument().addDocumentListener(TextFieldListener.on(_senderPortNumberTextField, (s, e) -> _mainWindowViewModel.setSenderPortNumber(e.getValue())));
        _receiverPortNumberTextField.getDocument().addDocumentListener(TextFieldListener.on(_receiverPortNumberTextField, (s, e) -> _mainWindowViewModel.setReceiverPortNumber(e.getValue())));
        _ipAddressTextField.getDocument().addDocumentListener(TextFieldListener.on(_ipAddressTextField, (s, e) -> _mainWindowViewModel.setIpAddress(e.getValue())));
        _messageTextField.getDocument().addDocumentListener(TextFieldListener.on(_messageTextField, (s, e) -> _mainWindowViewModel.setMessage(e.getValue())));
        _beginListenButton.addActionListener(a -> _mainWindowViewModel.createPeer());
        _transmitButton.addActionListener(a -> _mainWindowViewModel.transmitMessage());
    }

    private Panel createLabelledTextFieldArea(String labelValue, JTextField textField) {
        var area = new Panel();
        area.setLayout(new BoxLayout(area, BoxLayout.Y_AXIS));
        area.add(new Label(labelValue));
        area.add(textField);
        return area;
    }

    private void setSenderPortNumberText(String value) {
        if (!_senderPortNumberTextField.getText().equals(value))
            _senderPortNumberTextField.setText(value);
    }

    private void setReceiverPortNumber(String value) {
        if (!_receiverPortNumberTextField.getText().equals(value))
            _receiverPortNumberTextField.setText(value);
    }

    private void setIpAddressText(String value) {
        if (!_ipAddressTextField.getText().equals(value))
            _ipAddressTextField.setText(value);
    }

    private void setMessageText(String value) {
        if (!_messageTextField.getText().equals(value))
            _messageTextField.setText(value);
    }
}

class TextFieldListener implements DocumentListener {
    private final JTextField _sender;

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
        _sender = sender;
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
        onInserted.raise(new TextFieldEventArgs(_sender.getText()));
    }

    public void removeUpdate(DocumentEvent e) {
        onRemoved.raise(new TextFieldEventArgs(_sender.getText()));
    }

    public void changedUpdate(DocumentEvent e) {
        onChanged.raise(new TextFieldEventArgs(_sender.getText()));
    }
}

class TextFieldEventArgs extends EventArgs {
    private final String _value;

    public TextFieldEventArgs(String value) {
        _value = value;
    }

    public String getValue() {
        return _value;
    }
}
