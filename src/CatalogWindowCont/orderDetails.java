package CatalogWindowCont;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class orderDetails extends JFrame{
    private JLabel itemNoLabel;
    private JLabel itemDescLabel;
    private JLabel itemQuantLabel;
    private JLabel itemRegPrice;
    private JLabel itemPremPrice;
    private JButton backButton;
    private JPanel orderPanel;


    public  orderDetails(){

        File custFile = new File("C:\\Orders.txt");

        itemDescLabel.setText ("Press Go Back to Go Back...");

        backButton.addActionListener (new ActionListener ( ){
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }


    public JPanel getter(){

        return this.orderPanel;

    }


}
