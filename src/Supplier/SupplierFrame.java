package Supplier;

import CatalogAndLogin.LoginScreen;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

// Main supplier frame
public class SupplierFrame extends JFrame {
    // Frame components
    private JPanel mainPanel;
    private JButton viewStockButton;
    private JButton requestOrdersButton;
    private JButton requestReadyOrdersButton;
    private JButton logOutButton;

    // Frame constructor
    public SupplierFrame() {
        // Frame attributes
        setTitle("Supplier Interface");
        setDefaultCloseOperation((JFrame.EXIT_ON_CLOSE));
        setBounds(0,0,450,450);
        mainPanel  = new JPanel();
        mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(mainPanel);
        mainPanel.setLayout(null);

        // Displays dialog window with stock item details
        viewStockButton = new JButton("View Stock");
        viewStockButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open stock.txt for scanning
                File stockFile = new File("stock.txt");
                Scanner stockInput = null;
                try {
                    stockInput = new Scanner(stockFile);
                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                }

                // Read through stock.txt & calculate the sum of the total, reserved, and available items in stock
                String read = null;
                int reserved = 0;
                int avail = 0;
                int total = 0;
                while (stockInput.hasNextLine()) {
                    read = stockInput.nextLine();
                    String[] arr = read.split(" ", 3); // split line to get values: name, #reserved, #available
                    reserved += Integer.parseInt(arr[1]);
                    avail += Integer.parseInt(arr[2]);
                    // System.out.println("Scanning stock.txt. Items reserved: " + reserved + " avail: " + avail);
                }
                total += reserved + avail;
                JOptionPane.showMessageDialog(rootPane, "Total Items: " + total + "\nReserved Items: " + reserved + "\nAvailable Items: " + avail);
            }
        });
        viewStockButton.setBounds(10,0,100,25);
        mainPanel.add(viewStockButton);

        // Populates a new frame with out orders to be processed. Processing orders checks if supplier has enough stock.
        requestOrdersButton = new JButton("Request Orders");
        requestOrdersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OrdersFrame ordersFrame = new OrdersFrame();
                ordersFrame.setSize(450,400);
                ordersFrame.setVisible(true);
            }
        });
        requestOrdersButton.setBounds(130,0,150,25); // right +120
        mainPanel.add(requestOrdersButton);

        // Populates a new frame with ready orders that must be shipped. Shipping orders subtracts them from stock.
        requestReadyOrdersButton = new JButton("Request Ready Orders");
        requestReadyOrdersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ShipOrdersFrame shipOrdersFrame = new ShipOrdersFrame();
                shipOrdersFrame.setSize(450,400);
                shipOrdersFrame.setVisible(true);
            }
        });
        requestReadyOrdersButton.setBounds(300,0,170,25); // right 60+85
        mainPanel.add(requestReadyOrdersButton);

        // Logs supplier out of OSS
        logOutButton = new JButton("Log Out");
        logOutButton.addActionListener(new ActionListener() {
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
        logOutButton.setBounds(490,0,100,25);
        mainPanel.add(logOutButton);

    } // end ossSupplier constructor

    public static void main(String[] args) {
        SupplierFrame supplierFrame = new SupplierFrame();
        supplierFrame.setSize(615,450);
        supplierFrame.setVisible(true);
    }
}