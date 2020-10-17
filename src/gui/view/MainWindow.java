package gui.view;

import gui.view.components.XButton;
import gui.view.components.XTextField;
import gui.viewmodel.MainWindowViewModel;

import javax.swing.BoxLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainWindow extends Frame {
    private MainWindowViewModel mainWindowViewModel;

    private XTextField senderPortNumberTextField;
    private XTextField receiverPortNumberTextField;
    private XTextField ipAddressTextField;
    private XTextField messageTextField;
    private XButton beginListenButton;
    private XButton transmitButton;

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
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
        setVisible(true);
    }

    private void initializeViewModels() {
        mainWindowViewModel = new MainWindowViewModel();
    }

    private void initializeComponents() {
        // Initialize private variables
        senderPortNumberTextField = new XTextField("3000");
        receiverPortNumberTextField = new XTextField("3000");
        ipAddressTextField = new XTextField("localhost");
        messageTextField = new XTextField("");
        beginListenButton = new XButton("Begin");
        transmitButton = new XButton("Transmit");

        // Subscribe on events
        senderPortNumberTextField.addOnUpdateSubscriber((s, args) -> mainWindowViewModel.setSenderPortNumber(args.getText()));
        receiverPortNumberTextField.addOnUpdateSubscriber((s, args) -> mainWindowViewModel.setReceiverPortNumber(args.getText()));
        ipAddressTextField.addOnUpdateSubscriber((s, args) -> mainWindowViewModel.setIpAddress(args.getText()));
        messageTextField.addOnUpdateSubscriber((s, args) -> mainWindowViewModel.setMessage(args.getText()));

        beginListenButton.addOnClickedSubscriber((s, args) -> mainWindowViewModel.createPeer());
        transmitButton.addOnClickedSubscriber((s, args) -> mainWindowViewModel.transmitMessage());
    }

    private Panel createLabelledTextFieldArea(String labelValue, XTextField textField) {
        var area = new Panel();
        area.setLayout(new BoxLayout(area, BoxLayout.Y_AXIS));
        area.add(new Label(labelValue));
        area.add(textField);
        return area;
    }
}
