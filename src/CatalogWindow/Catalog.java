package CatalogWindow;

import java.awt.event.*;
import java.awt.Window;
import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Catalog extends JFrame {
    // Define everything seen on the form
    private JPanel rootJPanel; // The starting JPanel
    private JButton cartJButton; // Will open cartInternalFrame
    private JButton viewOrderJButton; // Sachita
    private JButton viewInvoiceJButton; // Sachita
    private JButton logOutJButton; // Will

    public List<ItemQuantity> cartItemsArr;

    //private ItemQuantity[] cartItemsArr;

    // Will read the file and put the information into sArray, to be displayed on the screen
    public void ReadFile() {
        cartItemsArr = new ArrayList<>();

        try {
            Scanner in = new Scanner(new File("Catalog.txt"));

            // Read from file and populate frame with items from the catalog
            while (in.hasNextLine()) {
                String s = in.nextLine();
                String[] sArray = s.split(",", 3);

                JLabel itemJLabel = new JLabel();
                itemJLabel.setText(sArray[0]);

                JLabel regularPriceJLabel = new JLabel();
                regularPriceJLabel.setText("Regular Price: $" + sArray[1]);

                JLabel premiumPriceJLabel = new JLabel();
                premiumPriceJLabel.setText("Premium Price: $" + sArray[2]);

                JTextField quantityUserInput = new JTextField();
                quantityUserInput.setText("0");

                JButton addToCartButton = new JButton("Add to Cart");

                Scanner input = new Scanner(new File("DataStuff/LoginData.txt"));
                System.out.println("Test");
                String userInfo = null;
                String userPrice = null;

                rootJPanel.add(itemJLabel);
                rootJPanel.add(regularPriceJLabel);
                rootJPanel.add(premiumPriceJLabel);
                rootJPanel.add(quantityUserInput);
                rootJPanel.add(addToCartButton);

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
                ItemQuantity item = new ItemQuantity(quantityUserInput, sArray[0], userPrice, addToCartButton);

                // Add item to array for cart items
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
        ReadFile();

        // Cart button opens the cart window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        cartJButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CartFrame cartFrame = new CartFrame(cartItemsArr);
                cartFrame.setSize(400,400);
            }
        });

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
    }

    // Displays the Catalog window
    public static void main(String[] args) {
        JFrame frame = new JFrame("Catalog");
        frame.setContentPane(new Catalog().rootJPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}

class ItemQuantity {
    public String itemNameAndDescription;
    public String itemPrice;
    public double quantity;

    JTextField quantityInputField;
    JButton addToCart;

    ItemQuantity(JTextField field, String nameAndDesc, String price, JButton button) {
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
}

class CartFrame extends JFrame {
    private static double subtotal;
    private JPanel cartPanel;
    private JButton placeOrderButton;

    CartFrame(List<ItemQuantity> array) {
        subtotal = 0; // new subtotal calculated each time cart is opened
        cartPanel = new JPanel();

        // Set frame attributes
        setContentPane(cartPanel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setVisible(true);

        // for each item in the array, display in the cart and add prices to subtotal
        for (ItemQuantity item: array){
            if (item.quantity > 0){
                subtotal += item.quantity * Double.parseDouble(item.itemPrice);
                JLabel cartItemLabel = new JLabel(item.itemNameAndDescription + " $" + item.itemPrice);
                cartPanel.add(cartItemLabel);
            }
        }
        JLabel subtotalLabel = new JLabel(String.valueOf(subtotal));
        cartPanel.add(subtotalLabel);

        placeOrderButton = new JButton("Place Order");
        cartPanel.add(placeOrderButton);

        placeOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                // place order

            }
        });
    }
}



