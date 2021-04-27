package CatalogWindow;

import java.awt.event.*;
import java.awt.Window;

import javax.swing.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Catalog extends JFrame {
    // Define everything seen on the form
    private JPanel rootJPanel; // The starting JPanel
    private JButton cartJButton; // Will open cartInternalFrame
    private JButton viewOrderJButton; // Sachita
    private JButton viewInvoiceJButton; // Sachita
    private JButton logOutJButton; // Will
//     private JCheckBox itemNameJCheckBox; // Allows user to select item
//    private JLabel itemNameAndDescriptionJLabel; // Read in from file
//    private JLabel regularPriceJLabel; // Read in from file
//    private JLabel premiumPriceJLabel; // Read in from file
//    private JLabel quantityPrompt; // Displays the quantity prompt
//    private JTextField quantityUserInput; // Allows the user to input their desired quantity
//    private JLabel regularPricePrompt;
//    private JLabel premiumPricePrompt;

    // Displays the window
    public static void main(String[] args) {

        JFrame frame = new JFrame("Catalog");
        frame.setContentPane(new Catalog().rootJPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        // frame.ReadFile();

    }

    // Will read the file and put the information into sArray, to be displayed on the screen
    public void ReadFile() {
        try {
            Scanner in = new Scanner(new File("Catalog.txt"));
            while (in.hasNextLine()) {
                String s = in.nextLine();
                String[] sArray = s.split(",", 3);

                JLabel itemNameAndDescriptionJLabel = new JLabel();
                JLabel regularPriceJLabel = new JLabel();
                JLabel premiumPriceJLabel = new JLabel();
                JTextField quantityUserInput = new JTextField();
                //itemNameJCheckBox.setText(sArray[0]);
                itemNameAndDescriptionJLabel.setText(sArray[0]);
                regularPriceJLabel.setText(sArray[1]);
                premiumPriceJLabel.setText(sArray[2]);
                quantityUserInput.setText("0");

                rootJPanel.add(itemNameAndDescriptionJLabel);
                rootJPanel.add(regularPriceJLabel);
                rootJPanel.add(premiumPriceJLabel);
                rootJPanel.add(quantityUserInput);
                Double quantity = Double.parseDouble(quantityUserInput.getText());

                //object
                if (quantity != null && quantity != 0) {
                    // create object of item info
                        // inside the object, we need it to populate in the cart window,
                    System.out.println("this item: " + quantity);

                }




            }
            in.close();

        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "File not found.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "File cannot be read.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        //Double quantity = Double.parseDouble(quantityUserInput.getText());

    }

    public Catalog() {
        ReadFile();
        // Allow the cart button to open the internal cart window.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        cartJButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // New frame: Cart
                JInternalFrame cartInternalFrame = new JInternalFrame("Cart");

                // Define the exit button
                JButton exitButton = new JButton("Exit");

                JPanel p1 = new JPanel();

                // Add necessary items to the screen
                p1.add(exitButton);
//                p1.add(itemNameAndDescriptionJLabel);
//                p1.add(regularPricePrompt);
//                p1.add(regularPriceJLabel);
//                p1.add(premiumPricePrompt);
//                p1.add(premiumPriceJLabel);
//                p1.add(quantityPrompt);
//                p1.add(quantityUserInput);
                cartInternalFrame.add(p1);

                cartInternalFrame.setVisible(true);
                rootJPanel.add(cartInternalFrame);
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
}

