package CatalogWindowCont;

import javax.swing.*;
import java.io.*;
import java.io.File;
import java.util.Scanner;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class MessageBufferResponse {
    String response; //credit card num
    String message; // auth num

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


public class makeorder extends JFrame {
    private JTextField textField1;
    private JRadioButton location1RadioButton;
    private JRadioButton location2RadioButton;
    private JRadioButton location3RadioButton;
    private JButton submitButton;

    public makeorder() {
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new viewOrder();
                frame.setBounds(133,100,532,400);
                viewOrder a = new viewOrder();
                a.setVisible(true);
            }
        });

        textField1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputaddress = textField1.getText();
                ProcessCustomerOrder pc = new ProcessCustomerOrder();
                pc.choosemail();
            }
        });

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MessageBufferResponse q = new MessageBufferResponse();
                new orderprocess(q);
                new bankprocess(q);
            }
        });
    }

}

class ProcessCustomerOrder {

    Scanner in = new Scanner(System.in);

    public void choosemail() {
        int finalcost = 0 ;
        finalcost += 3;
    }

    public void storeOrder() {
        try {
            File orderFile = new File("orders.txt");
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
        System.out.println("Do you want to continue premium membership?");
        String ans = in.nextLine();

        if (ans.equals("yes")) {
            int finalcost = 0;
            finalcost += 40;
        }
    }
}

class orderprocess implements Runnable {
    MessageBufferResponse q;

    public orderprocess(MessageBufferResponse q) {
        this.q = q;
        new Thread(this, "Customer").start();
    }

    @Override
    public void run() {
        Scanner in = new Scanner(System.in);

        String creditcardnumber = null;
        String authorizationNum = q.send(creditcardnumber);
        if (authorizationNum.equals("-1")){
            System.out.println("Enter a new credit card number:");
            String newCreditCardNum = in.nextLine();
            String newauthorizationNum = q.send(newCreditCardNum);
        }
        if (!authorizationNum.equals("-1")){
            account.creditCardNum = newCreditCardNum;
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
        authorizationNum = bankApprove(q.receive());
        q.reply(authorizationNum);
        if (authorizationNum.equals(-1)){
            authorizationNum = bankApprove(q.receive());
            q.reply(authorizationNum);
        }

        int bankApprove(int testCC){

            File orderFile = new File("");
            while ()
        }
    }
}

class Main {
    public void main(String[] arg) {
        JFrame frame = new makeorder();
        frame.setBounds(133, 100, 532, 400);
        frame.setVisible(true);
    }
}
