package SupplierWindow;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Scanner;

class shipButton {
    private JButton shipB;
    private String orderRead;
    private JPanel readyOrdersPanel;

    shipButton(String read, JButton button, JPanel panel){
        this.orderRead = read;
        this.shipB = button;
        this.readyOrdersPanel = panel;

        // Ship order associated with shipB button
        shipB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create buffers and scanners to proccess file information
                try{
                    // Open an orders reader to read from and populate orders buffer
                    BufferedReader ordersReader = new BufferedReader(new FileReader("orders.txt"));
                    StringBuffer ordersBuffer = new StringBuffer();
                    String updatedOrder; // new line of updated order info

                    // Orders file scanner to find customer order
                    File ordersFile = new File("orders.txt");
                    Scanner ordersInput = null;
                    try {
                        ordersInput = new Scanner(ordersFile);
                    } catch (FileNotFoundException fileNotFoundException) {
                        fileNotFoundException.printStackTrace();
                    }
                    // find order associated with button in the orders file
                    String orderToUpdate = ordersInput.nextLine();
                    while (!orderToUpdate.equals(orderRead)){
                        orderToUpdate = ordersInput.nextLine();
                    }

                    // Get order information
                    String[] orderInfoArr = orderToUpdate.split(" ", 4);

                    // Copy file info to buffer and update order status
                    while ((updatedOrder = ordersReader.readLine()) != null){
                        if (updatedOrder.equals(orderRead)){
                            updatedOrder = (orderInfoArr[0] + " " + orderInfoArr[1] + " shipped " + orderInfoArr[3]);
                        }
                        ordersBuffer.append(updatedOrder + "\n");
                    }

                    // Write new status to file
                    // System.out.println("Putting order buffer in file");
                    FileOutputStream ordersOut = new FileOutputStream(("orders.txt"));
                    ordersOut.write(ordersBuffer.toString().getBytes());
                    ordersOut.close();
                    ordersReader.close();

                    // Find # of items ordered
                    String toss = ordersInput.nextLine(); // toss {
                    String pattern = new String("}");
                    // Read each item in the order. Calculate new reserved amounts and write to stock file
                    while (!ordersInput.hasNext(pattern)) {
                        // Get item's info
                        String[] itemInfoArr = (ordersInput.nextLine()).split(" ", 3);
                        int numToShip = Integer.parseInt(itemInfoArr[2]); // get num of items to ship

                        // Open stock reader for overwriting stock amount
                        BufferedReader stockReader = new BufferedReader(new FileReader("stock.txt"));
                        StringBuffer stockBuffer = new StringBuffer();
                        String updatedStock;

                        // Open stock file to find # of items reserved
                        File stockFile = new File("stock.txt");
                        Scanner stockInput = null;
                        try{
                            stockInput = new Scanner(stockFile);
                        } catch (FileNotFoundException fileNotFoundException) {
                            fileNotFoundException.printStackTrace();
                        }

                        // Find item ordered in stock file
                        String stockItemToUpdate = stockInput.nextLine();
                        while(!(stockItemToUpdate.contains(itemInfoArr[0]))){
                            stockItemToUpdate = stockInput.nextLine();
                        }
                        // System.out.println(stockItemToUpdate);

                        // Get current stock's reserved amount and calculate new amount post shipping
                        String stockItemArr[] = stockItemToUpdate.split(" ", 3);
                        int reserved = Integer.parseInt(stockItemArr[1]);
                        int newReserved = reserved - numToShip;

                        // Populate buffer with updated info
                        while((updatedStock = stockReader.readLine()) != null) {
                            if (updatedStock.contains(itemInfoArr[0])){
                                updatedStock = (stockItemArr[0] + " " + newReserved + " " + stockItemArr[2]);
                            }
                            stockBuffer.append(updatedStock + "\n");
                        }
                        // Write buffer to stock file
                        FileOutputStream stockOut = new FileOutputStream("stock.txt");
                        stockOut.write(stockBuffer.toString().getBytes());
                        stockOut.close();
                        stockReader.close();
                    }

                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                JOptionPane.showMessageDialog(readyOrdersPanel, "Order Shipped");
                shipB.setText("Shipped");
                shipB.removeNotify();
            }
        });// end of shipButton actionEvent
    } // end of shipButton constructors
}


class ReadyOrdersFrame extends JFrame {
    JPanel readyOrdersPanel;
    int buttonxCoord = 10;
    int buttonyCoord = 0;

    ReadyOrdersFrame() {
        // set frame attributes
        setTitle("Ready Orders");
        setDefaultCloseOperation((JFrame.DISPOSE_ON_CLOSE));
        readyOrdersPanel = new JPanel();
        readyOrdersPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(readyOrdersPanel);
        readyOrdersPanel.setLayout(null);

        // Open orders file for reading and populating frame
        File ordersFile = new File("orders.txt");
        Scanner ordersInput = null;
        try {
            ordersInput = new Scanner(ordersFile);
        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }

        // Read through orders.txt & display each order and its items with a button
        String read = null;
        while(ordersInput.hasNextLine()) {
            read = ordersInput.nextLine();
            // System.out.println(read);

            if (read.contains("ready")) {
                // create new button and action for button depending on order in String read
                JButton shipB = new JButton();
                shipB.setText("Ship");
                shipButton rOrder = new shipButton(read, shipB, readyOrdersPanel);
                shipB.setBounds(buttonxCoord, buttonyCoord, 80, 25);
                readyOrdersPanel.add(shipB);

                // Populate label with each item's info
                String toss = ordersInput.nextLine(); // toss {
                String pattern = "}";
                JLabel order1L = new JLabel();
                String itemOrdered = "";
                while (!ordersInput.hasNext(pattern)) {
                    String item = ordersInput.nextLine();
                    String[] sArr = item.split(" ", 3);
                    itemOrdered += (sArr[0] + " [" + sArr[2] + "] - ");
                }
                order1L.setText(itemOrdered);
                order1L.setBounds(buttonxCoord+115, buttonyCoord, 400, 25);
                readyOrdersPanel.add(order1L);
                buttonyCoord += 35;
                toss = ordersInput.nextLine();
                // System.out.println(toss);
            }
            // if order not ready, continue to toss lines until next ready order
            else {
                while (!read.contains("}")){
                    read = ordersInput.nextLine();
                }
            }
        } // end of reading orders.txt
    } // end ReadyOrders constructor
}