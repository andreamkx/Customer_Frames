package CatalogAndLogin;
// TEST COMMENT

import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

// Associates specific actions with each view invoice button
class viewInvoiceButton {
    private JPanel viewOrdersPanel;
    private JButton viewInvoiceB;
    private String orderRead;

    viewInvoiceButton(String order, JButton button, JPanel panel) {
        this.orderRead = order;
        this.viewInvoiceB = button;
        this.viewOrdersPanel = panel;

        viewInvoiceB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Scanner ordersInput = null;
                try {
                    ordersInput = new Scanner(new File("orders.txt"));
                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                }

                // get order date, items, total, payment info and show in dialog
                String orderLine = null;
                while (ordersInput.hasNextLine()){
                    orderLine = ordersInput.nextLine();
                    if (orderLine.equals(orderRead)){
                        String[] arr = orderLine.split(" ", 6);

                        // populate string with order info
                        String orderInfo = "Date: " + arr[3] +
                                "\nTotal: $" + arr[4] +
                                "\nPayment Info: Credit Card used - " + arr[5] +
                                "\nITEMS: ";
                        orderLine = ordersInput.nextLine(); // toss {

                        // populate string with ordered items
                        while (!ordersInput.hasNext("}")){
                            orderLine = ordersInput.nextLine();
                            orderInfo += "\n" + orderLine;
                        }

                        JOptionPane.showMessageDialog(viewOrdersPanel, orderInfo);
                        break;
                    }
                }
            }
        });
    } // end viewInvoiceButton constructor
}

// Displays View Order Frame
class viewOrder extends JFrame {
    private JPanel mainPanel;
    int xCoord = 5;
    int yCoord = 10;

    public viewOrder() {
        setTitle("Your Orders");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(0,0,450,450);
        mainPanel = new JPanel();
        mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(mainPanel);
        mainPanel.setLayout(null);

        File custFile = new File("orders.txt");
        Scanner ordersInput = null;
        try {
            ordersInput = new Scanner(custFile);
        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }

        String read = null;
        while (ordersInput.hasNextLine()) {
            read = ordersInput.nextLine();

            if (read.contains(LoginScreen.usernameLogged)){
                String[] arr = read.split(" ", 4);
                JLabel orderInfoLabel = new JLabel("Order #: " + arr[1] + " Order Status: " + arr[2]);
                orderInfoLabel.setBounds(xCoord, yCoord, 250, 25);
                mainPanel.add(orderInfoLabel);

                JButton viewInvoiceB = new JButton("View Invoice");
                viewInvoiceB.setBounds(xCoord + 300, yCoord, 125, 25);
                mainPanel.add(viewInvoiceB);

                viewInvoiceButton button = new viewInvoiceButton(read, viewInvoiceB, mainPanel);

                yCoord += 75;
            }
        }

    }
}

// Display Make Order Frame
class makeorder extends JFrame {
    private JLabel mailOrderLabel = new JLabel("Mail Order");
    private JTextField textField1;
    private JLabel pickupLabel = new JLabel("Or select a pickup location");
    private JRadioButton location1RadioButton;
    private JRadioButton location2RadioButton;
    private JRadioButton location3RadioButton;
    private JButton submitButton;
    private JPanel mainPanel;

    double orderTotal;

    String userCCNum;

    public makeorder(double subtotal, List<CartItem> array) {
        userCCNum = "";
        setTitle("Place Order");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(0,0,500,500);
        mainPanel = new JPanel();
        mainPanel.setBorder(new EmptyBorder(5,5,5,5));
        setContentPane(mainPanel);
        mainPanel.setLayout(null);

        orderTotal = subtotal;

        mailOrderLabel.setBounds(10, 5, 125,25);
        mainPanel.add(mailOrderLabel);

        pickupLabel.setBounds(10,50,200,25);
        mainPanel.add(pickupLabel);

        textField1 = new JTextField();
        textField1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // String inputaddress = textField1.getText();
                // choosemail();
                orderTotal += 3;
                location1RadioButton.removeNotify();
                location2RadioButton.removeNotify();
                location3RadioButton.removeNotify();
            }
        });
        textField1.setBounds(10, 25, 300,25);
        mainPanel.add(textField1);

        location1RadioButton = new JRadioButton("Lubbock Downtown Post Office");
        location1RadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textField1.removeNotify();
                location2RadioButton.removeNotify();
                location3RadioButton.removeNotify();
            }
        });
        location1RadioButton.setBounds(10,115,200,25);
        mainPanel.add(location1RadioButton);

        boolean chosen = false;
        location2RadioButton = new JRadioButton("Lubbock Amazon Post Office");
        location2RadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textField1.removeNotify();
                location1RadioButton.removeNotify();
                location3RadioButton.removeNotify();
            }
        });
        location2RadioButton.setBounds(10, 135, 200, 25);
        mainPanel.add(location2RadioButton);

        location3RadioButton = new JRadioButton("Texas Tech University Post Office");
        location3RadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textField1.removeNotify();
                location1RadioButton.removeNotify();
                location2RadioButton.removeNotify();
            }
        });
        location3RadioButton.setBounds(10, 155, 200, 25);
        mainPanel.add(location3RadioButton);

        submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Scanner input = null;

                // find premium status and ccnum
                try {
                    input = new Scanner(new File("DataStuff/LoginData.txt"));
                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                }
                String userInfo = "";

                // find user premium status to use appropriate price
                while( input.hasNextLine() ){ // find user in file
                    userInfo = input.nextLine();
                    if (userInfo.contains(LoginScreen.usernameLogged)){
                        break;
                    }
                }
                while( input.hasNextLine() ){ // find user premium status
                    userInfo = input.nextLine();
                    if (userInfo.contains("CC#")){
                        String[] arr = userInfo.split(":",2);
                        userCCNum = arr[1];
                    }
                    if (userInfo.contains("Premium")) break;
                }
                if (userInfo.contains("true")) {
                    // opens option pane prompting for premium membership continuation
                    premiumFirstOrder();
                }

                System.out.println(userCCNum);
                MessageBufferResponse q = new MessageBufferResponse();
                //String userCCNUm;
                orderprocess orderP = new orderprocess(q, userCCNum);
                bankprocess bankP = new bankprocess(q);
                System.out.println("PRINTING: " + orderP.q.response);
                if ( !(orderP.q.response).equals("-1")){
                    storeOrder(orderP.q.response, array);
                }
            }
        });
        submitButton.setBounds(250, 400, 125, 25 );
        mainPanel.add(submitButton);
    } // end make order constructor

    public void storeOrder(String response, List<CartItem> array) {
        // use supplier as reference to store order info
        String orderInfo = "";
        // username authnum status date total creditcardnum
        //{
        //items
        //}
        orderInfo += LoginScreen.usernameLogged + " " + response + " " + "ordered" + "04/30/2021"  + orderTotal + " " + userCCNum + "\n{\n";

        for (CartItem item: array) {
            String[] sArray = item.getItemNameAndDescription().split(":", 2);
            orderInfo += sArray[0] + " " + item.getItemPrice() + " " + item.getQuantity()+"\n";
        }
        orderInfo += "}\n";
        try {
            Files.write(Paths.get("orders.txt"), orderInfo.getBytes(), StandardOpenOption.APPEND);
        }catch (IOException e) {
            System.out.println("File not Found");
        }


    }

    void premiumFirstOrder(){
        if (JOptionPane.showConfirmDialog(mainPanel, "Would you like to continue your premium membership? This will add $40 to your subtotal", "Premium customer", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_NO_OPTION) {
            orderTotal+=40;
            JOptionPane.showMessageDialog(mainPanel, "Your total is now $" +  orderTotal);
        }
        else {
            System.out.println("Changing premium status to false");
        }
    }
}

// Threading classes
class MessageBufferResponse {
    String response; // Authoriztion num
    String message; // CCNum

    private boolean messageBufferFull = false;
    private boolean responseBufferFull = false;

    // customer calls this to send ccnum, then returns authnum
    synchronized String send(String creditCardNum) {
        message = creditCardNum;
        messageBufferFull = true;
        notify();

        while (!responseBufferFull)
            try {
                wait(); // starts banking system thread
            } catch (InterruptedException e) {
                System.out.println("InterruptedException caught");
            }

        System.out.println("Response of this operation is : " + response);
        responseBufferFull = false;
        notify();
        return response;
    }

    // bankingSystem waits to get ccNum
    synchronized String receive() {
        while (!messageBufferFull) {
            try {
                wait(); // wait until ccnum is sent to buffer
            } catch (InterruptedException e) {
                System.out.println("InterruptedException caught");
            }
        }
        messageBufferFull = false;
        return (message);
    }

    synchronized void reply(String authorizationNum) {
        response = authorizationNum;
        responseBufferFull = true;
        notify();
    }
}


class orderprocess implements Runnable {
    MessageBufferResponse q;
    String CCNum;

    public orderprocess(MessageBufferResponse buffer, String userCCNum) {
        this.q = buffer;
        this.CCNum = userCCNum;
        new Thread(this, "Customer").start();
    }

    @Override
    public void run() {
        String authorizationNum = q.send(CCNum);
        System.out.println("THREAD RESPONSE: " + authorizationNum);
        if (authorizationNum.equals("-1")) { // if invalid ccnum, ask for new
//          **DOUBLE CHECK
            String newCreditCardNum = JOptionPane.showInputDialog("Enter a new credit card number: ");
            //int newCCNum = Integer.parseInt((newCreditCardNum));
            System.out.println("new creditcard num given: " + newCreditCardNum);
            authorizationNum = q.send(newCreditCardNum);

            if (authorizationNum.equals("-1")){
                System.out.println("too many invalid attempts.");
            }
            else {
                System.out.println("Adding new credit card to customer info");
            }
        }
        System.out.println("FINAL RESPONSE: " + q.response);
        q.response = authorizationNum;
    }
}

class bankprocess implements Runnable {
    MessageBufferResponse q;

    public bankprocess(MessageBufferResponse buffer) {
        this.q = buffer;
        new Thread(this, "Supplier").start();
    }
    @Override
    public void run() {
        String authNum = bankApprove(q.receive());
        System.out.println("Auth num generated: " + authNum);
        q.reply(authNum);
    }
    String bankApprove(String testCC) {
        int authNum = -1;
        Scanner input = null;
        try {
            input = new Scanner(new File("bankSystem.txt"));
        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }

        while (input.hasNextLine()){
            if (input.nextLine().equals(testCC)){
                Random rand = new Random();
                authNum = rand.nextInt(10000);
                // charge customer bank account
                ;
            }
        }
        return String.valueOf(authNum);
    }
}



class CartItem {
    private String itemNameAndDescription;
    private String itemPrice;
    private double quantity;

    private JTextField quantityInputField;
    private JButton addToCart;

    CartItem(JTextField field, String nameAndDesc, String price, JButton button) {
        itemNameAndDescription = nameAndDesc;
        itemPrice = price;
        quantityInputField = field;
        addToCart = button;

        addToCart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                quantity = Double.parseDouble(quantityInputField.getText()); // get value when button pressed
            }
        });
    }

    String getItemNameAndDescription(){
        return itemNameAndDescription;
    }

    String getItemPrice(){
        return itemPrice;
    }

    double getQuantity(){
        return quantity;
    }
}

// Displays customer's cart
class CartFrame extends JFrame {
    private static double subtotal;
    private JPanel cartPanel;
    private JButton placeOrderButton;
    int xCoord = 10;
    int yCoord = 0;

    // calculate subtotal and display order information. Provide place order button
    CartFrame(List<CartItem> array) {
        subtotal = 0; // new subtotal calculated each time cart is opened
        setTitle("Shopping Cart");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(0,0,450,450);
        cartPanel = new JPanel();
        setContentPane(cartPanel);
        cartPanel.setLayout(null);

        placeOrderButton = new JButton("Place Order");
        placeOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
               makeorder frame = new makeorder(subtotal, array);
               frame.setBounds(0,0,450,450);
               frame.setVisible(true);
            }
        });
        placeOrderButton.setBounds(xCoord, yCoord, 125, 25);
        cartPanel.add(placeOrderButton);

        // for each item in the array, display in the cart and add prices to subtotal
        for (CartItem item: array){
            if (item.getQuantity() > 0){
                subtotal += item.getQuantity() * Double.parseDouble(item.getItemPrice());
                JLabel cartItemLabel = new JLabel("$" + item.getItemPrice() + " x" + String.format("%,.0f", item.getQuantity()) + ": " + item.getItemNameAndDescription());
                cartItemLabel.setBounds(xCoord,yCoord+30,400, 25);
                cartPanel.add(cartItemLabel);
                yCoord+=30;
            }
        }

        JLabel subtotalLabel = new JLabel("Subtotal: $" + String.valueOf(subtotal));
        subtotalLabel.setBounds(xCoord+150, 0,100,25);
        cartPanel.add(subtotalLabel);
    }

}

// Displays catalog to customer
public class Catalog extends JFrame {
    // Define everything seen on the form
    private JPanel rootJPanel; // The starting JPanel
    private JButton cartJButton; // Will open cartInternalFrame
    private JButton viewOrderJButton; // Sachita
    private JButton viewInvoiceJButton; // Sachita
    private JButton logOutJButton;

    // ReadFile() component coordinates
    int xCoord = 10;
    int yCoord = 50;

    // Items chosen from catalog to populate cart
    public List<CartItem> cartItemsArr;

    // Read the file and display information on the screen
    public void ReadFile() {
        cartItemsArr = new ArrayList<>();

        try {
            // Read from file and populate frame with items
            Scanner in = new Scanner(new File("Catalog.txt"));

            while (in.hasNextLine()) {
                String sTEST = in.nextLine();
                System.out.println(sTEST);
                String[] sArray = sTEST.split(",", 3);
                JLabel itemJLabel = new JLabel(sArray[0]);
                itemJLabel.setBounds(xCoord, yCoord, 375,25);
                rootJPanel.add(itemJLabel);

                JLabel premiumPriceJLabel = new JLabel("Premium Price: $" + sArray[1]);
                premiumPriceJLabel.setBounds(xCoord, yCoord+30, 300, 10);
                rootJPanel.add(premiumPriceJLabel);

                JLabel regularPriceJLabel = new JLabel("Regular Price:    $" + sArray[2]);
                regularPriceJLabel.setBounds(xCoord, yCoord+45, 300, 10);
                rootJPanel.add(regularPriceJLabel);

                JTextField quantityUserInput = new JTextField("0");
                quantityUserInput.setBounds(xCoord+400, yCoord, 25,25);
                rootJPanel.add(quantityUserInput);

                JButton addToCartButton = new JButton("Add to Cart");
                addToCartButton.setBounds(xCoord+430, yCoord, 100,25);
                rootJPanel.add(addToCartButton);

                yCoord+=75;

                System.out.println("TEST");

                Scanner input = new Scanner(new File("DataStuff/LoginData.txt"));
                //System.out.println("Test");
                String userInfo = "";
                String userPrice = "";

                // Assign specific actions to addToCartButton depending on quantityUserInput
                    // find user premium status to use appropriate price
                while( input.hasNextLine() ){ // find user in file
                    userInfo = input.nextLine();
                    System.out.println("IN LOGIN READING " + userInfo);
                    if (userInfo.contains(LoginScreen.usernameLogged)){
                        System.out.println("TESTING USERNAME READ: " + LoginScreen.usernameLogged);
                        break;
                    }
                }

                while( input.hasNextLine() ){ // find user premium status
                    System.out.println("TEST");
                    userInfo = input.nextLine();
                    if (userInfo.contains("Premium")) break;
                }
                if (userInfo.contains("true")) { // if premium, take premium price
                    userPrice = sArray[1];
                    System.out.println("TEST3");
                } else { // else, take regular price
                    userPrice = sArray[2];
                }

                // Store text field value, item information, price, and button to assign specific actions to it
                CartItem item = new CartItem(quantityUserInput, sArray[0], userPrice, addToCartButton);

                // Add item to array to populate cart items in CartFrame
                cartItemsArr.add(item);
            }
            in.close();

        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "File not found.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "File cannot be read.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } // End ReadFile()

    public Catalog() {
        // set Frame attributes
        setTitle("Catalog");
        setDefaultCloseOperation((JFrame.EXIT_ON_CLOSE));
        setBounds(0,0,600,600);
        rootJPanel = new JPanel();
        rootJPanel.setBorder(new EmptyBorder(5,5,5,5));
        setContentPane(rootJPanel);
        rootJPanel.setLayout(null);

        // Cart button opens the cart window
        cartJButton = new JButton("Cart");
        cartJButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CartFrame cartFrame = new CartFrame(cartItemsArr);
                cartFrame.setSize(450,450);
                cartFrame.setVisible(true);
            }
        });
        cartJButton.setBounds(xCoord, 0, 100, 25);
        rootJPanel.add(cartJButton);

        // View Order displays customer order history
        viewOrderJButton = new JButton("View Orders");
        viewOrderJButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewOrder frame = new viewOrder();
                frame.setSize(450,450);
                frame.setVisible(true);
            }
        });
        viewOrderJButton.setBounds(xCoord+115, 0, 100, 25);
        rootJPanel.add(viewOrderJButton);

        // Logs user out of OSS
        logOutJButton = new JButton("Log Out");
        logOutJButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame loggedIn = new JFrame("Logged In Screen");
                if (JOptionPane.showConfirmDialog(loggedIn, "You have Logged out, would you like to log in again?", "Login Systems", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_NO_OPTION) {
                    LoginScreen.main(null);
                    //Catalog.dispatchEvent(new WindowEvent(Catalog, WindowEvent.WINDOW_CLOSING));
                    dispose();
                } else {
                    System.exit(0);
                }
            }
        });
        logOutJButton.setBounds(xCoord+430,0,100,25);
        rootJPanel.add(logOutJButton);

        // populate window with catalog items
        ReadFile();
    }

    // Displays the Catalog window
    public static void main(String[] args) {
        Catalog frame = new Catalog();
        frame.setVisible(true);
    }
}