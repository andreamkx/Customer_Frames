//package CatalogAndLogin;
//
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.nio.file.StandardOpenOption;
//import java.util.Random;
//import javax.swing.*;
//import javax.swing.border.EmptyBorder;
//import java.io.*;
//import java.io.File;
//import java.util.Scanner;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//
//// Display Make Order Frame
//public class makeorder extends JFrame {
//    private JLabel mailOrderLabel = new JLabel("Mail Order");
//    private JTextField textField1;
//    private JLabel pickupLabel = new JLabel("Or select a pickup location");
//    private JRadioButton location1RadioButton;
//    private JRadioButton location2RadioButton;
//    private JRadioButton location3RadioButton;
//    private JButton submitButton;
//    private JPanel mainPanel;
//
//    double orderTotal;
//
//    String userCCNum;
//
//    public makeorder(double subtotal) {
//        userCCNum = "";
//        setTitle("Place Order");
//        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        setBounds(0,0,500,500);
//        mainPanel = new JPanel();
//        mainPanel.setBorder(new EmptyBorder(5,5,5,5));
//        setContentPane(mainPanel);
//        mainPanel.setLayout(null);
//
//        orderTotal = subtotal;
//
//        mailOrderLabel.setBounds(10, 5, 125,25);
//        mainPanel.add(mailOrderLabel);
//
//        pickupLabel.setBounds(10,50,200,25);
//        mainPanel.add(pickupLabel);
//
//        textField1 = new JTextField();
//        textField1.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                // String inputaddress = textField1.getText();
//                // choosemail();
//                orderTotal += 3;
//                location1RadioButton.removeNotify();
//                location2RadioButton.removeNotify();
//                location3RadioButton.removeNotify();
//            }
//        });
//        textField1.setBounds(10, 25, 300,25);
//        mainPanel.add(textField1);
//
//        location1RadioButton = new JRadioButton("Lubbock Downtown Post Office");
//        location1RadioButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                textField1.removeNotify();
//                location2RadioButton.removeNotify();
//                location3RadioButton.removeNotify();
//            }
//        });
//        location1RadioButton.setBounds(10,115,200,25);
//        mainPanel.add(location1RadioButton);
//
//        boolean chosen = false;
//        location2RadioButton = new JRadioButton("Lubbock Amazon Post Office");
//        location2RadioButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                textField1.removeNotify();
//                location1RadioButton.removeNotify();
//                location3RadioButton.removeNotify();
//            }
//        });
//        location2RadioButton.setBounds(10, 135, 200, 25);
//        mainPanel.add(location2RadioButton);
//
//        location3RadioButton = new JRadioButton("Texas Tech University Post Office");
//        location3RadioButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                textField1.removeNotify();
//                location1RadioButton.removeNotify();
//                location2RadioButton.removeNotify();
//            }
//        });
//        location3RadioButton.setBounds(10, 155, 200, 25);
//        mainPanel.add(location3RadioButton);
//
//        submitButton = new JButton("Submit");
//        submitButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                Scanner input = null;
//
//                // find premium status and ccnum
//                try {
//                    input = new Scanner(new File("DataStuff/LoginData.txt"));
//                } catch (FileNotFoundException fileNotFoundException) {
//                    fileNotFoundException.printStackTrace();
//                }
//                String userInfo = "";
//
//                // find user premium status to use appropriate price
//                while( input.hasNextLine() ){ // find user in file
//                    userInfo = input.nextLine();
//                    if (userInfo.contains(LoginScreen.usernameLogged)){
//                        break;
//                    }
//                }
//                while( input.hasNextLine() ){ // find user premium status
//                    userInfo = input.nextLine();
//                    if (userInfo.contains("CC#")){
//                        String[] arr = userInfo.split(":",2);
//                        userCCNum = arr[1];
//                    }
//                    if (userInfo.contains("Premium")) break;
//                }
//                if (userInfo.contains("true")) {
//                    // opens option pane prompting for premium membership continuation
//                    premiumFirstOrder();
//                }
//
//                System.out.println(userCCNum);
//                MessageBufferResponse q = new MessageBufferResponse();
//                //String userCCNUm;
//                new orderprocess(q, userCCNum);
//                new bankprocess(q);
//                System.out.println(q.response);
//                if (q.response.equals("-1")){
//                    storeOrder(q.response);
//                }
//            }
//        });
//        submitButton.setBounds(250, 400, 125, 25 );
//        mainPanel.add(submitButton);
//    } // end make order constructor
//
//    public void storeOrder(String response) {
//        // use supplier as reference to store order info
//        String orderInfo = "";
//        // username authnum status date total creditcardnum
//        //{
//        //items
//        //}
//        orderInfo += LoginScreen.usernameLogged + " " + response + " " + "ordered" + "04/30/2021"  + orderTotal + " " + userCCNum "\n{\n";
//
//        for (CartItem item: array) {
//
//        }
//        try {
//            Files.write(Paths.get("orders.txt"), orderInfo.getBytes(), StandardOpenOption.APPEND);
//        }catch (IOException e) {
//            System.out.println("File not Found");
//        }
//
//
//    }
//
//    void premiumFirstOrder(){
//        if (JOptionPane.showConfirmDialog(mainPanel, "Would you like to continue your premium membership? This will add $40 to your subtotal", "Premium customer", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_NO_OPTION) {
//            orderTotal+=40;
//            JOptionPane.showMessageDialog(mainPanel, "Your total is now $" +  orderTotal);
//        }
//        else {
//            System.out.println("Changing premium status to false");
//        }
//    }
//}
//
//// Threading classes
//class MessageBufferResponse {
//    String response; // Authoriztion num
//    String message; // CCNum
//
//    private boolean messageBufferFull = false;
//    private boolean responseBufferFull = false;
//
//    // customer calls this to send ccnum, then returns authnum
//    synchronized String send(String creditCardNum) {
//        this.message = creditCardNum;
//        messageBufferFull = true;
//        notify();
//
//        while (!responseBufferFull)
//            try {
//                wait(); // starts banking system thread
//            } catch (InterruptedException e) {
//                System.out.println("InterruptedException caught");
//            }
//
//        System.out.println("Response of this operation is : " + response);
//        responseBufferFull = false;
//        notify();
//        return (response);
//    }
//
//    // bankingSystem waits to get ccNum
//    synchronized String receive() {
//        while (!messageBufferFull) {
//            try {
//                wait(); // wait until ccnum is sent to buffer
//            } catch (InterruptedException e) {
//                System.out.println("InterruptedException caught");
//            }
//        }
//        messageBufferFull = false;
//        return (message);
//    }
//
//    synchronized void reply(String authorizationNum) {
//        response = authorizationNum;
//        responseBufferFull = true;
//        notify();
//    }
//}
//
//
//class orderprocess implements Runnable {
//    MessageBufferResponse q;
//    String CCNum;
//
//    public orderprocess(MessageBufferResponse q, String userCCNum) {
//        this.q = q;
//        this.CCNum = userCCNum;
//        new Thread(this, "Customer").start();
//    }
//
//    @Override
//    public void run() {
//        String authorizationNum = q.send(CCNum);
//        if (authorizationNum.equals("-1")) {
//
////          **DOUBLE CHECK
//            String newCreditCardNum = JOptionPane.showInputDialog("Enter a new credit card number: ");
//            //int newCCNum = Integer.parseInt((newCreditCardNum));
//            String newauthorizationNum = q.send(newCreditCardNum);
//        }
//        if (authorizationNum.equals("-1")){
//            //account.creditCardNum = newCreditCardNum; ** ask will
//            System.out.println("new creditcard num given");
//        }
//        else {
//            System.out.println("too many invalid attempts.");
//        }
//    }
//}
//
//class bankprocess implements Runnable {
//    MessageBufferResponse q;
//    public bankprocess(MessageBufferResponse q) {
//        this.q = q;
//        new Thread(this, "Supplier").start();
//    }
//    @Override
//    public void run() {
//        String authNum = bankApprove(q.receive());
//        System.out.println("Auth num generated: " + authNum);
//        q.reply(authNum);
//    }
//
//    String bankApprove(String testCC) {
//        int authNum = -1;
//        Scanner input = null;
//        try {
//            input = new Scanner(new File("bankSystem.txt"));
//        } catch (FileNotFoundException fileNotFoundException) {
//            fileNotFoundException.printStackTrace();
//        }
//
//        while (input.hasNextLine()){
//            if (input.nextLine().equals(testCC)){
//                Random rand = new Random();
//                authNum = rand.nextInt(10000);
//                // charge customer bank account
//                ;
//            }
//                    }
//        return String.valueOf(authNum);
//    }
//}
//
//
