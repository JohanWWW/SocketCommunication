package gui.view;

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
                case "senderPortNumber" -> SwingUtilities.invokeLater(() -> setSenderPortNumberText(_mainWindowViewModel.getSenderPortNumber()));
                case "receiverPortNumber" -> SwingUtilities.invokeLater(() -> setReceiverPortNumber(_mainWindowViewModel.getReceiverPortNumber()));
                case "ipAddress" -> SwingUtilities.invokeLater(() -> setIpAddressText(_mainWindowViewModel.getIpAddress()));
                case "message" -> SwingUtilities.invokeLater(() -> setMessageText(_mainWindowViewModel.getMessage()));
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
        _senderPortNumberTextField.getDocument().addDocumentListener(TextFieldListener.on(_senderPortNumberTextField, args -> _mainWindowViewModel.setSenderPortNumber(args.getValue())));
        _receiverPortNumberTextField.getDocument().addDocumentListener(TextFieldListener.on(_receiverPortNumberTextField, args -> _mainWindowViewModel.setReceiverPortNumber(args.getValue())));
        _ipAddressTextField.getDocument().addDocumentListener(TextFieldListener.on(_ipAddressTextField, args -> _mainWindowViewModel.setIpAddress(args.getValue())));
        _messageTextField.getDocument().addDocumentListener(TextFieldListener.on(_messageTextField, args -> _mainWindowViewModel.setMessage(args.getValue())));
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
    private final Consumer<TextFieldEventArgs> _subscriber;

    private TextFieldListener(JTextField sender, Consumer<TextFieldEventArgs> subscriber) {
        _sender = sender;
        _subscriber = subscriber;
    }

    public static TextFieldListener on(JTextField source, Consumer<TextFieldEventArgs> subscriber) {
        return new TextFieldListener(source, subscriber);
    }

    public void insertUpdate(DocumentEvent e) {
        publishEvent(new TextFieldEventArgs(_sender.getText()));
    }

    public void removeUpdate(DocumentEvent e) {
        publishEvent(new TextFieldEventArgs(_sender.getText()));
    }

    public void changedUpdate(DocumentEvent e) {
        publishEvent(new TextFieldEventArgs(_sender.getText()));
    }

    private void publishEvent(TextFieldEventArgs args) {
        if (_subscriber != null)
            _subscriber.accept(args);
    }
}

class TextFieldEventArgs {
    private final String _value;

    public TextFieldEventArgs(String value) {
        _value = value;
    }

    public String getValue() {
        return _value;
    }
}
