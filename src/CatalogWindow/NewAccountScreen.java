package CatalogWindow;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JOptionPane;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class NewAccountScreen extends JFrame {

	private JPanel contentPane;
	private JTextField nameTF;
	private JTextField usernameTF;
	private JTextField passwordTF;
	private JTextField addressTF;
	private JTextField phonenumTF;
	private JTextField creditcardTF;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					NewAccountScreen frame = new NewAccountScreen();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public NewAccountScreen() {
		setTitle("Online Shopping System New Account Screen");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(200, 200, 500, 480);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
		JLabel nameLabel = new JLabel("Name:");
		nameLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		nameLabel.setBounds(50, 40, 100, 14);
		contentPane.add(nameLabel);
		
		nameTF = new JTextField();
		nameTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		nameTF.setBounds(150, 40, 150, 20);
		contentPane.add(nameTF);
		nameTF.setColumns(10);
		
		JLabel usernameLabel = new JLabel("Username:");
		usernameLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		usernameLabel.setBounds(50, 80, 100, 14);
		contentPane.add(usernameLabel);
		
		usernameTF = new JTextField();
		usernameTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		usernameTF.setBounds(150, 80, 150, 20);
		contentPane.add(usernameTF);
		usernameTF.setColumns(10);
		
		JLabel passwordLabel = new JLabel("Password:");
		passwordLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		passwordLabel.setBounds(50, 120, 100, 14);
		contentPane.add(passwordLabel);
		
		passwordTF = new JTextField();
		passwordTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		passwordTF.setBounds(150, 120, 150, 20);
		contentPane.add(passwordTF);
		passwordTF.setColumns(10);
		
		JLabel addressLabel = new JLabel("Address:");
		addressLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		addressLabel.setBounds(50, 160, 100, 14);
		contentPane.add(addressLabel);
		
		addressTF = new JTextField();
		addressTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		addressTF.setBounds(150, 160, 300, 20);
		contentPane.add(addressTF);
		addressTF.setColumns(10);
		
		JLabel phonenumberLabel = new JLabel("Phone Number:");
		phonenumberLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		phonenumberLabel.setBounds(50, 200, 100, 14);
		contentPane.add(phonenumberLabel);
		
		phonenumTF = new JTextField();
		phonenumTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		phonenumTF.setBounds(150, 200, 150, 20);
		contentPane.add(phonenumTF);
		phonenumTF.setColumns(10);
		
		JLabel phonenumberexplLabel = new JLabel("Please enter only the number, no spaces or dashes");
		phonenumberexplLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		phonenumberexplLabel.setBounds(50, 220, 325, 15);
		contentPane.add(phonenumberexplLabel);
		
		JLabel creditcardLabel = new JLabel("Credit Card #:");
		creditcardLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		creditcardLabel.setBounds(50, 260, 100, 14);
		contentPane.add(creditcardLabel);
		
		JLabel creditcardexplLabel = new JLabel("Please enter only the number, no spaces or dashes");
		creditcardexplLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		creditcardexplLabel.setBounds(50, 280, 325, 15);
		contentPane.add(creditcardexplLabel);
		
		creditcardTF = new JTextField();
		creditcardTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		creditcardTF.setBounds(150, 260, 150, 20);
		contentPane.add(creditcardTF);
		creditcardTF.setColumns(10);
		
		JCheckBox premiumCB = new JCheckBox("Premium Account?");
		premiumCB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		premiumCB.setFont(new Font("Tahoma", Font.PLAIN, 14));
		premiumCB.setBounds(150, 310, 150, 23);
		contentPane.add(premiumCB);
		
		JButton exitButton = new JButton("Exit");
		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame frmRegSystem = new JFrame("Exit");
				if (JOptionPane.showConfirmDialog(frmRegSystem, "Confirm if you want to exit", "Registration Systems", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_NO_OPTION) {
					System.exit(0);
				}
			}
		});
		exitButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		exitButton.setBounds(275, 360, 100, 23);
		contentPane.add(exitButton);
		
		JButton createAcntButton = new JButton("Create Account");
		createAcntButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String username = usernameTF.getText();
				String password = passwordTF.getText();
				String address = addressTF.getText();
				String phonenum = phonenumTF.getText();
				String name = nameTF.getText();
				String ccnum = creditcardTF.getText();
				boolean premium = false;
				int ln;
				File inputFile = new File("DataStuff/LoginData.txt");
				File newFile = new File("DataStuff/");
				
				if(!newFile.exists()) {
					newFile.mkdirs();
				}
				
				try {
					FileReader fr = new FileReader(inputFile);
					RandomAccessFile raf = new RandomAccessFile(inputFile, "rw");
					ln = 1;
					RandomAccessFile lsraf = new RandomAccessFile("DataStuff/" + username + "_" + name + ".txt", "rw");
					
					for(@SuppressWarnings("unused")
					int i = 0; raf.readLine() != null; i++) {
						ln++;
					}
					System.out.println("Number of lines: " + ln);
					
					for(int i = 0;i < ln; i++){
		                raf.readLine();
		            }
					if(ln > 2){
			            raf.writeBytes("\r\n");
			            raf.writeBytes("\r\n");
			        }
					
					if(premiumCB.isSelected()) {
						premium = true;
					}
					else {
						premium = false;
					}
					
					raf.writeBytes("Username:" + username + "\r\n");
					raf.writeBytes("Password:" + password + "\r\n");
					raf.writeBytes("Name:" + name + "\r\n");
					raf.writeBytes("Address:" + address + "\r\n");
					raf.writeBytes("Phone#:" + phonenum + "\r\n");
					raf.writeBytes("CC#:" + ccnum + "\r\n");
					raf.writeBytes("Premium:" + premium);
					
					lsraf.writeBytes("Username:" + username + "\r\n");
					lsraf.writeBytes("Name:" + name + "\r\n");
					
					fr.close();
					raf.close();
					lsraf.close();
				} 
				catch(FileNotFoundException excp) {
					JOptionPane.showMessageDialog(null, "User Database Not Found", "Error", JOptionPane.ERROR_MESSAGE);
				} 
				catch (IOException ex) {
					JOptionPane.showMessageDialog(null, "Cannot Write to the file", "Error", JOptionPane.ERROR_MESSAGE);
				}	
				
			}
		});
		createAcntButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		createAcntButton.setBounds(100, 360, 150, 23);
		contentPane.add(createAcntButton);
		
		JButton backButton = new JButton("Return");
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LoginScreen.main(null);
				dispose();
			}
		});
		backButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		backButton.setBounds(364, 410, 110, 20);
		contentPane.add(backButton);
	}
}
