package CatalogWindowCont;

import CatalogWindow.CartFrame;
import CatalogWindow.LoginScreen;

import javax.swing.*;
import java.io.*;
import java.io.File;
import java.util.Scanner;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


// make order frame
public class makeorder extends JFrame {
    private JTextField textField1;
    private JRadioButton location1RadioButton;
    private JRadioButton location2RadioButton;
    private JRadioButton location3RadioButton;
    private JButton submitButton;
    private JPanel mainPanel;
    Scanner in = new Scanner(System.in);
    double orderTotal;

    public makeorder(double subtotal) {
        mainPanel = new JPanel();
        orderTotal = subtotal;


        textField1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // String inputaddress = textField1.getText();
                choosemail();

            }
        });

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Scanner input = null;
                try {
                    input = new Scanner(new File("DataStuff/LoginData.txt"));
                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                }
                System.out.println("Test");
                String userInfo = "";
                String userCCNum = "";

                // find user premium status to use appropriate price
                while( input.hasNextLine() ){ // find user in file
                    userInfo = input.nextLine();
                    if (userInfo.contains(LoginScreen.username)){
                        break;
                    }
                }
                while( input.hasNextLine() ){ // find user premium status
                    userInfo = input.nextLine();
                    if (userInfo.contains("CC#")){
                        String[] arr = userInfo.split(":",2);
                        userCCNum = arr[1];
                    }
                    if (userInfo.contains("Premium")) break;
                }
                if (userInfo.contains("true")) {
                    // opens option pane prompting for premium membership continuation
                    premiumFirstOrder();
                }

                MessageBufferResponse q = new MessageBufferResponse();
                String userCCNUm;
                new orderprocess(q, userCCNUm);
                new bankprocess(q);
            }
        });
    } // end make order constructor

    public void choosemail() {
        int finalcost = 0 ;
        finalcost += 3;
    }

    public void storeOrder() {
        try {
            File orderFile = new File("C:\\Orders.txt");
            if (!orderFile.exists()) {
                orderFile.createNewFile();
            }
            FileWriter fileWritter = new FileWriter(orderFile.getName(), true);
            BufferedWriter bw = new BufferedWriter(fileWritter);
            // bw.write();
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void premiumFirstOrder(){
        if (JOptionPane.showConfirmDialog(mainPanel, "Would you like to continue your premium membership? This will add $40 to your subtotal", "Premium customer", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_NO_OPTION) {
            orderTotal+=40;
            JOptionPane.showMessageDialog(mainPanel, "Your total is now $" +  orderTotal);
        }
        else {
            // change premium status to false ** ask will
            
        }
    }

}

// Threading classes
class MessageBufferResponse {
    String response; // Authoriztion num
    String message; // CCNum

    private boolean messageBufferFull = false;
    private boolean responseBufferFull = false;

    synchronized String send(String creditCardNum) {
        this.message = creditCardNum;
        messageBufferFull = true;
        notify();

        while (!responseBufferFull)
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("InterruptedException caught");
            }

        System.out.println("Response of this operation is : " + response);
        responseBufferFull = false;
        notify();
        return (response);
    }

    synchronized String receive() {
        while (!messageBufferFull) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("InterruptedException caught");
                messageBufferFull = false;
            }
        }
        notify();
        return (message);
    }

    synchronized void reply(String authorizationNum) {
        response = authorizationNum;
        responseBufferFull = true;
        notify();
    }
}


class orderprocess implements Runnable {
    MessageBufferResponse q;
    String CCNum;

    public orderprocess(MessageBufferResponse q, String userCCNum) {
        this.q = q;
        new Thread(this, "Customer").start();
        this.CCNum = userCCNum;
    }

    @Override
    public void run() {
        Scanner in = new Scanner(System.in);

        String authorizationNum = q.send(CCNum);
        if (authorizationNum.equals("-1")){
            System.out.println("Enter a new credit card number:");
            String newCreditCardNum = in.nextLine();
            String newauthorizationNum = q.send(newCreditCardNum);
        }
        if (!authorizationNum.equals("-1")){
            //account.creditCardNum = newCreditCardNum;
        }
        else {
            System.out.println("too many invalid attempts.");
        }
    }
}


class bankprocess implements Runnable {

    MessageBufferResponse q;
    public bankprocess(MessageBufferResponse q) {
        this.q = q;
        new Thread(this, "Supplier").start();
    }

    @Override
    public void run() {

    }
}


