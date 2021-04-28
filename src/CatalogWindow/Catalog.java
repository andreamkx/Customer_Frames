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
    int xCoord = 10;
    int yCoord = 0;

    CartFrame(List<ItemQuantity> array) {
        subtotal = 0; // new subtotal calculated each time cart is opened
        cartPanel = new JPanel();

        // Set frame attributes
        setContentPane(cartPanel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        placeOrderButton = new JButton("Place Order");
        placeOrderButton.setBounds(xCoord, yCoord, 80, 25);
        cartPanel.add(placeOrderButton);

        // for each item in the array, display in the cart and add prices to subtotal
        for (ItemQuantity item: array){
            if (item.quantity > 0){
                subtotal += item.quantity * Double.parseDouble(item.itemPrice);
                JLabel cartItemLabel = new JLabel(item.itemNameAndDescription + " $" + item.itemPrice);
                cartItemLabel.setBounds(xCoord,yCoord+30,400, 25);
                cartPanel.add(cartItemLabel);
                yCoord+=30;
            }
        }

        JLabel subtotalLabel = new JLabel("Subtotal: $" + String.valueOf(subtotal));
        subtotalLabel.setBounds(xCoord+100, 0,100,25);
        cartPanel.add(subtotalLabel);


        placeOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                // place order

            }
        });
    }
}

public class Catalog extends JFrame {
    // Define everything seen on the form
    private JPanel rootJPanel; // The starting JPanel
    private JButton cartJButton; // Will open cartInternalFrame
    private JButton viewOrderJButton; // Sachita
    private JButton viewInvoiceJButton; // Sachita
    private JButton logOutJButton;

    // component coordinates
    int xCoord = 10;
    int yCoord = 50;

    // Items chosen from catalog to populate cart
    public List<ItemQuantity> cartItemsArr;

    // Read the file and put the information into sArray, to be displayed on the screen
    public void ReadFile() {
        cartItemsArr = new ArrayList<>();

        try {
            // Read from file and populate frame with items
            Scanner in = new Scanner(new File("Catalog.txt"));

            while (in.hasNextLine()) {
                String s = in.nextLine();
                String[] sArray = s.split(",", 3);

                JLabel itemJLabel = new JLabel(sArray[0]);
                itemJLabel.setBounds(xCoord, yCoord, 300,25);
                rootJPanel.add(itemJLabel);

                JLabel regularPriceJLabel = new JLabel("Regular Price: $" + sArray[1]);
                regularPriceJLabel.setBounds(xCoord, yCoord+30, 50, 25);
                rootJPanel.add(regularPriceJLabel);

                JLabel premiumPriceJLabel = new JLabel("Premium Price: $" + sArray[2]);
                premiumPriceJLabel.setBounds(xCoord, yCoord+60, 50, 25);
                rootJPanel.add(premiumPriceJLabel);

                JTextField quantityUserInput = new JTextField("0");
                quantityUserInput.setBounds(xCoord+310, yCoord, 25,25);
                rootJPanel.add(quantityUserInput);

                JButton addToCartButton = new JButton("Add to Cart");
                addToCartButton.setBounds(xCoord+340, yCoord, 120,25);
                rootJPanel.add(addToCartButton);

                yCoord+=70;

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
                ItemQuantity item = new ItemQuantity(quantityUserInput, sArray[0], userPrice, addToCartButton);

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
        setTitle("Catalog");
        setDefaultCloseOperation((JFrame.EXIT_ON_CLOSE));
        setBounds(0,0,500,500);
        rootJPanel = new JPanel();
        rootJPanel.setBorder(new EmptyBorder(5,5,5,5));
        setContentPane(rootJPanel);
        rootJPanel.setLayout(null);

        // populate window with catalog items
        ReadFile();

        // Cart button opens the cart window
        cartJButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CartFrame cartFrame = new CartFrame(cartItemsArr);
                cartFrame.setSize(400,400);
            }
        });

        // Logs user out of OSS
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
        frame.setVisible(true);
    }
}




