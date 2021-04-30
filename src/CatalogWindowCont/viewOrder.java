package CatalogWindowCont;

import javax.swing.*;
import java.beans.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class viewOrder extends JFrame {
    private JLabel custLabel;
    private JPanel mainPanel;
    private JLabel appLabel;
    private JButton viewOrderButton1;
    private JLabel orderLabel1;
    private JButton viewOrderButton5;
    private JLabel orderLabel2;
    private JLabel orderLabel3;
    private JButton viewOrderButton4;
    private JLabel orderLabel4;
    private JButton viewOrderButton2;
    private JButton viewOrderButton3;
    private JLabel orderLabel5;
    String orderline, custline = "";
    int labelcount = 1;

    public viewOrder() {

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);

        File custFile = new File("customer.txt");

        BufferedReader custBr = null;
        try {
            custBr = new BufferedReader(new FileReader(custFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            if (custBr != null) {
                custline = custBr.readLine();
            }
            while (custline != null) {
                String[] values;
                values = custline.split(",");
                if (values[0].equals("1000")) {
                    custLabel.setText(values[2]);
                }
                if (custBr != null) {
                    custline = custBr.readLine();
                    System.out.print(custline+ " ");
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        File orderFile = new File("C:\\Orders.csv");

        BufferedReader orderBr = null;
        try {
            orderBr = new BufferedReader(new FileReader(orderFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            if (orderBr != null) {
                orderline = orderBr.readLine();
            }
            while (orderline != null) {
                String[] values;
                values = orderline.split(",");
                if (values[0].equals("1000")) {
                    if (labelcount == 1) {
                        orderLabel1.setText(values[1]);
                    }
                    if (labelcount == 2) {
                        orderLabel2.setText(values[2]);
                    }
                    if (labelcount == 3) {
                        orderLabel3.setText(values[3]);
                    }
                    if (labelcount == 4) {
                        orderLabel4.setText(values[4]);
                    }
                    if (labelcount == 5) {
                        orderLabel5.setText(values[5]);
                    }
                    labelcount++;
                }
                if (orderBr != null) {
                    orderline = orderBr.readLine();

                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        viewOrderButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        JFrame frame1 = new JFrame("Order Details");
                        frame1.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
                        frame1.getContentPane().add(new orderDetails().getter());
                        frame1.setBounds(433, 300, 532, 400);
                        frame1.setVisible(true);
                    }
                });

            }
        });
    }


    public static void main(String[] arg) {
        JFrame frame = new viewOrder();
        frame.setBounds(133, 100, 532, 400);
        frame.setVisible(true);

    }
}
