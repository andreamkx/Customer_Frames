package CatalogWindowCont;

// import CatalogWindow.CartFrame;
import CatalogWindow.LoginScreen;
import java.util.Random;
import javax.swing.*;
import java.io.*;
import java.io.File;
import java.util.Scanner;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// make order frame
public class makeorder extends JFrame {
    private JLabel mailOrderLabel = new JLabel("Mail Order");
    private JTextField textField1;
    private JLabel pickupLabel = new JLabel("Or select a pickup location");
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

        mailOrderLabel.setBounds(10, 5, 125,25);
        mainPanel.add(mailOrderLabel);

        pickupLabel.setBounds(10,35,125,25);
        mainPanel.add(pickupLabel);

        location1RadioButton = new JRadioButton("Lubbock Downtown Post Office");
        location1RadioButton.setBounds(10,75,200,25);

        location2RadioButton = new JRadioButton("Lubbock Amazon Post Office");
        location2RadioButton.setBounds(10, 115, 200, 25);

        location3RadioButton = new JRadioButton("Texas Tech University Post Office");
        location3RadioButton.setBounds(10, 155, 200, 25);


        textField1 = new JTextField();
        textField1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // String inputaddress = textField1.getText();
                choosemail();
            }
        });
        textField1.setBounds(10, 50, 300,25);
        mainPanel.add(textField1);

        submitButton = new JButton("Submit");
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
                String userCCNum = null;

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
                //String userCCNUm;
                new orderprocess(q, userCCNum);
                new bankprocess(q);
            }
        });
        submitButton.setBounds(400, 400, 125, 25 );
        mainPanel.add(submitButton);
    } // end make order constructor

    // if textfield not null, call this
    public void choosemail() {
        orderTotal += 3;
    }

    public void storeOrder() {
        // use supplier as reference to store order info
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
            System.out.println("Changing premium Status");
        }
    }

}

// Threading classes
class MessageBufferResponse {
    String response; // Authoriztion num
    String message; // CCNum

    private boolean messageBufferFull = false;
    private boolean responseBufferFull = false;

    // customer calls this to send ccnum, then returns authnum
    synchronized String send(String creditCardNum) {
        this.message = creditCardNum;
        messageBufferFull = true;
        notify();

        while (!responseBufferFull)
            try {
                wait(); // starts banking system thread
            } catch (InterruptedException e) {
                System.out.println("InterruptedException caught");
            }

        System.out.println("Response of this operation is : " + response);
        responseBufferFull = false;
        notify();
        return (response);
    }

    // bankingSystem waits to get ccNum
    synchronized String receive() {
        while (!messageBufferFull) {
            try {
                wait(); // wait until ccnum is sent to buffer
            } catch (InterruptedException e) {
                System.out.println("InterruptedException caught");
            }
        }
        messageBufferFull = false;
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
        this.CCNum = userCCNum;
        new Thread(this, "Customer").start();
    }

    @Override
    public void run() {
        Scanner in = new Scanner(System.in);
        String authorizationNum = q.send(CCNum);
        if (authorizationNum.equals("-1")) {

//          **DOUBLE CHECK
            String newCreditCardNum = JOptionPane.showInputDialog("Enter a new credit card number: ");
            //int newCCNum = Integer.parseInt((newCreditCardNum));
            String newauthorizationNum = q.send(newCreditCardNum);
        }
        if (authorizationNum.equals("-1")){
            //account.creditCardNum = newCreditCardNum; ** ask will
            System.out.println("new creditcard num given");
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
        String authNum = bankApprove(q.receive());
        q.reply(authNum);
    }

    String bankApprove(String testCC) {
        int authNum = -1;
        Scanner input = null;
        try {
            input = new Scanner(new File("bankSystem.txt"));
        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }

        while (input.hasNextLine()){
            if (input.nextLine() == testCC){
                Random rand = new Random();
                authNum = rand.nextInt(10000);
                // charge customer bank account
                ;
            }
                    }
        return String.valueOf(authNum);
    }
}


