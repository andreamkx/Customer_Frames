package SupplierWindow;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Scanner;

// Assigns specific actions with each button based on the order associated with the button.
class reserveButton {
    private JButton processB;
    private String orderRead;
    private JPanel orderPanel;

    // reserveButton constructor
    reserveButton(String order, JButton button, JPanel panel){
        this.orderRead = order;
        this.processB = button;
        this.orderPanel = panel;

        // Reserve order's items
        processB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Orders file scanner. Find order associated with button
                Scanner ordersInput = null;
                try {
                    ordersInput = new Scanner(new File("orders.txt"));
                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                }
                String orderToUpdate = ordersInput.nextLine();
                while (!orderToUpdate.equals(orderRead)){
                    orderToUpdate = ordersInput.nextLine();
                }

                // Array of order info. Used to populate String buffer later
                String[] orderArr = orderToUpdate.split(" ", 4);

                // Read each item in the order. check if each item needs to be restocked. If not, make order status "ready"
                String[] itemOrderedArr; // array of item info
                String[] stockItemArr;  // array of stock item info
                int amountOrdered = 0;
                int newAvail = 0;
                boolean needRestock = false;

                // Begin scanning items
                String toss = ordersInput.nextLine(); // toss {
                String pattern = "}";
                try{
                    while (!ordersInput.hasNext(pattern)) {
                        // Get item info
                        itemOrderedArr = (ordersInput.nextLine()).split(" ", 3);
                        amountOrdered = Integer.parseInt(itemOrderedArr[2]);
                        // System.out.println("Item ordered: " + itemOrderedArr[0] + " " + itemOrderedArr[1] + " #" + amountOrdered);

                        // Open stock file to check if item is in stock
                        File stockFile = new File("stock.txt");
                        Scanner stockInput = null;
                        try {
                            stockInput = new Scanner(stockFile);
                        } catch (FileNotFoundException fileNotFoundException) {
                            fileNotFoundException.printStackTrace();
                        }

                        // Find item ordered in stock file
                        String stockItemToUpdate = stockInput.nextLine();
                        while (!(stockItemToUpdate.contains(itemOrderedArr[0]))) {
                            stockItemToUpdate = stockInput.nextLine();
                        }
                        // System.out.println("Stock item to update: " + stockItemToUpdate);

                        // Get current stock's available amount and calculate new amount if reserved
                        stockItemArr = stockItemToUpdate.split(" ", 3); // split information: name, #reserved, #available
                        int stockAvail = Integer.parseInt(stockItemArr[2]);
                        newAvail = stockAvail - amountOrdered;

                        // If less than 0 items are available in stock if order is processed, create restock needed window and set flag.
                        if (newAvail < 0) {
                            JOptionPane.showMessageDialog(orderPanel, "Needs Restock!" +
                                    "\nItem: " + stockItemArr[0] +
                                    "\nItems available if order is processed: " + newAvail +
                                    "\nOrder was not processed.");
                            needRestock = true;
                            break; // exit while loop of reading order's items
                        }

                        // Open stock reader to read from and use to populate stock buffer
                        BufferedReader stockReader = new BufferedReader(new FileReader("stock.txt"));
                        StringBuffer stockBuffer = new StringBuffer();
                        String updatedStock;

                        // Populate stock buffer with updated item info
                        while ((updatedStock = stockReader.readLine()) != null) {
                            int newReserved = Integer.parseInt(stockItemArr[1]) + amountOrdered;
                            if (updatedStock.contains(itemOrderedArr[0])) {
                                updatedStock = (stockItemArr[0] + " " + newReserved + " " + newAvail);
                            }
                            stockBuffer.append(updatedStock + "\n");
                        }
                        stockReader.close();

                        // Write buffer to stock.txt file
                        FileOutputStream stockOutput = new FileOutputStream("stock.txt");
                        stockOutput.write(stockBuffer.toString().getBytes());
                        stockOutput.close();
                    } // end while loop of reading an order's items

                    // If restock flag not set, update orders file
                    if (!needRestock){
                        // Open orders reader to populate orders buffer
                        BufferedReader ordersReader = new BufferedReader(new FileReader("orders.txt"));
                        StringBuffer ordersBuffer = new StringBuffer();
                        String updatedOrder; // new line of potentially updated order info

                        // Write updated info to orders buffer
                        while ((updatedOrder = ordersReader.readLine()) != null) {
                            if (updatedOrder.equals(orderRead)) {
                                updatedOrder = (orderArr[0] + " " + orderArr[1] + " ready " + orderArr[3]);
                            }
                            ordersBuffer.append(updatedOrder + "\n");
                        }
                        ordersReader.close();

                        // Write buffer to orders file
                        FileOutputStream ordersOutput = new FileOutputStream("orders.txt");
                        ordersOutput.write(ordersBuffer.toString().getBytes());
                        ordersOutput.close();

                        // Show dialog and update button
                        JOptionPane.showMessageDialog(orderPanel, "Order Processed");
                        processB.setText("Processed");
                        processB.removeNotify();
                        ordersReader.close();
                    } // end of !needRestock condition

                } catch (FileNotFoundException notFoundException) {
                    notFoundException.printStackTrace();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }

            }
        });// end of reserveButton actionEvent
    } // end of reserveButton constructor
}


// Displays orders to be processed
public class OrdersFrame extends JFrame {
    JPanel ordersPanel;
    int buttonxCoord = 10;
    int buttonyCoord = 0;

    // Default constructor
    OrdersFrame() {
        // Set frame attributes
        setTitle("Out Orders");
        setDefaultCloseOperation((JFrame.DISPOSE_ON_CLOSE));
        ordersPanel = new JPanel();
        ordersPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(ordersPanel);
        ordersPanel.setLayout(null);

        // Open orders file for reading and populating frame
        File ordersFile = new File("orders.txt");
        Scanner ordersInput = null;
        try {
            ordersInput = new Scanner(ordersFile);
        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }

        // Read through orders.txt & display each order and its items with a reserve order button
        String read = null;
        while(ordersInput.hasNextLine()) {
            read = ordersInput.nextLine();
            // System.out.println(read);

            if (read.contains("ordered")) {
                // Create new button and assign an action depending on order
                JButton reserveB = new JButton("Process");
                reserveButton oOrder = new reserveButton(read, reserveB, ordersPanel);
                ordersPanel.add(reserveB);
                reserveB.setBounds(buttonxCoord, buttonyCoord, 100, 25);

                // reserveB.set

                // Populate label with each item's info
                String toss = ordersInput.nextLine(); // toss {
                String pattern = "}";
                JLabel order1L = new JLabel();
                String itemOrdered = "";
                while (!ordersInput.hasNext(pattern)) {
                    String item = ordersInput.nextLine();
                    String[] sArr = item.split(" ", 3);
                    itemOrdered += (sArr[0] + " [" + sArr[2] + "] - ");;
                }
                order1L.setText(itemOrdered);
                order1L.setBounds(buttonxCoord+125, buttonyCoord, 400,25);
                ordersPanel.add(order1L);
                buttonyCoord += 35;
                toss = ordersInput.nextLine(); // toss }
                // System.out.println(toss);
            }
            // if order not ordered, continue to toss lines until next order
            else {
                while (!read.contains("}")){
                    read = ordersInput.nextLine();
                }
            }
        } // end of while for reading orders.txt
    } // end OrdersFrame constructor
}