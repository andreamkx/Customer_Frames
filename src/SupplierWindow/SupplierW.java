package SupplierWindow;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

// Main supplier frame
public class SupplierW extends JFrame {
    // Frame components
    private JPanel mainPanel;
    private JButton viewStockButton;
    private JButton requestOrdersButton;
    private JButton requestReadyOrdersButton;

    // Frame constructor
    public SupplierW() {
        // Frame attributes
        setTitle("Supplier Interface");
        setDefaultCloseOperation((JFrame.EXIT_ON_CLOSE));
        setBounds(0,0,450,450);
        mainPanel  = new JPanel();
        mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(mainPanel);
        mainPanel.setLayout(null);

        // Initialize components
        viewStockButton = new JButton("View Stock");
        viewStockButton.setBounds(10,0,100,25);

        requestOrdersButton = new JButton("Request Orders");
        requestOrdersButton.setBounds(130,0,150,25); // right +120

        requestReadyOrdersButton = new JButton("Request Ready Orders");
        requestReadyOrdersButton.setBounds(300,0,170,25); // right 60+85

        // Add components to panel
        mainPanel.add(viewStockButton);
        mainPanel.add(requestOrdersButton);
        mainPanel.add(requestReadyOrdersButton);

        // Displays dialog window with stock item details
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

        // Populates a new frame of out orders to be processed. Processing orders checks if supplier has enough stock.
        requestOrdersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OrdersFrame ordersFrame = new OrdersFrame();
                ordersFrame.setSize(450,400);
                ordersFrame.setVisible(true);

            }
        });

        // Populates a new frame with "ready" orders that must be shipped. Shipping orders subtracts them from stock.
        requestReadyOrdersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ReadyOrdersFrame readyOrdersFrame = new ReadyOrdersFrame();
                readyOrdersFrame.setSize(450,400);
                readyOrdersFrame.setVisible(true);
            }
        });
    } // end ossSupplier constructor

    public static void main(String[] args) {
        SupplierW supplierFrame = new SupplierW();
        supplierFrame.setSize(500,450);
        supplierFrame.setVisible(true);
    }
}