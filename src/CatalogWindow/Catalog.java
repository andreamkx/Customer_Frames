package CatalogWindow;

import java.awt.event.*;
import java.awt.Window;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Associates specific 'Add to Cart' actions with each item
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
                // place order

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
                String s = in.nextLine();
                String[] sArray = s.split(",", 3);

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

                Scanner input = new Scanner(new File("DataStuff/LoginData.txt"));
                System.out.println("Test");
                String userInfo = "";
                String userPrice = "";

                // Assign specific actions to addToCartButton depending on quantityUserInput
                    // find user premium status to use appropriate price
                while( input.hasNextLine() ){ // find user in file
                    userInfo = input.nextLine();
                    if (userInfo.contains(LoginScreen.username)){
                        break;
                    }
                }

                while( input.hasNextLine() ){ // find user premium status
                    userInfo = input.nextLine();
                    if (userInfo.contains("Premium")) break;
                }

                if (userInfo.contains("true")) { // if premium, take premium price
                    userPrice = sArray[1];
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
                cartFrame.setSize(400,400);
                cartFrame.setVisible(true);
            }
        });
        cartJButton.setBounds(xCoord, 0, 100, 25);
        rootJPanel.add(cartJButton);

        // View Order displays customer order
        viewOrderJButton = new JButton("View Order");
        viewOrderJButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("View Order pressed");
            }
        });
        viewOrderJButton.setBounds(xCoord+115, 0, 100, 25);
        rootJPanel.add(viewOrderJButton);

        // view Invoice displays customer Invoice
        viewInvoiceJButton = new JButton("View Invoice");
        viewInvoiceJButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("View Invoice pressed");
            }
        });
        viewInvoiceJButton.setBounds(xCoord+230, 0, 125, 25);
        rootJPanel.add(viewInvoiceJButton);

        // Logs user out of OSS
        logOutJButton = new JButton("Log Out");
        logOutJButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame loggedIn = new JFrame("Logged In Screen");
                if (JOptionPane.showConfirmDialog(loggedIn, "You have Logged out, would you like to log in again?", "Login Systems", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_NO_OPTION) {
                    LoginScreen.main(null);
                    //Catalog.dispatchEvent(new WindowEvent(Catalog, WindowEvent.WINDOW_CLOSING));
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