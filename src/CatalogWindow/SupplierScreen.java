package CatalogWindow;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class SupplierScreen extends JFrame {

	private JPanel contentPane;
	private JTextField usernameTF;
	private JPasswordField passwordPF;
	public static int line;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SupplierScreen frame = new SupplierScreen();
					frame.setVisible(true);
					File inputFile = new File("DataStuff/SupplierData.txt");
					RandomAccessFile raf = new RandomAccessFile(inputFile, "rw");
					int ln = 1;
					for(@SuppressWarnings("unused")
					int i = 0; raf.readLine() != null; i++) {
						ln++;
					}
					line = ln;
					raf.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public SupplierScreen() {
		setTitle("Online Shopping System Supplier Login Screen");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(200, 200, 450, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
		JLabel usernameLabel = new JLabel("Username: ");
		usernameLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		usernameLabel.setBounds(80, 50, 100, 14);
		contentPane.add(usernameLabel);
		
		usernameTF = new JTextField();
		usernameTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		usernameTF.setBounds(170, 50, 150, 20);
		contentPane.add(usernameTF);
		usernameTF.setColumns(10);
		
		JLabel passwordLabel = new JLabel("Password:");
		passwordLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		passwordLabel.setBounds(80, 120, 100, 14);
		contentPane.add(passwordLabel);
		
		passwordPF = new JPasswordField();
		passwordPF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		passwordPF.setBounds(170, 120, 150, 20);
		contentPane.add(passwordPF);
		
		JButton loginButton = new JButton("Login");
		loginButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File inputFile = new File("DataStuff/SupplierData.txt");
				char[] charPassword = passwordPF.getPassword();
				String passwordin = String.valueOf(charPassword);
				String usernamein = usernameTF.getText();
				
				try {
					FileReader fr = new FileReader(inputFile);
					RandomAccessFile raf = new RandomAccessFile(inputFile, "rw");
					for(int j = 0; j < line; j+=4){
						System.out.println("count " + j);
						String username = raf.readLine().substring(9);
						String password = raf.readLine().substring(9);
						System.out.println(username);
						System.out.println(password);
						if(usernamein.equals(username) && passwordin.equals(password)){
							JOptionPane.showMessageDialog(null, "Login Successful", "Success", JOptionPane.INFORMATION_MESSAGE);
							JFrame loggedIn = new JFrame("Logged In Screen");
							if (JOptionPane.showConfirmDialog(loggedIn, "You have Logged out, would you like to log in again?", "Login Systems", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_NO_OPTION) {
								usernameTF.setText("");
								passwordPF.setText("");
							}
							else {
								System.exit(0);
							}
						}
						else{
							JOptionPane.showMessageDialog(null, "Invalid Username / Password Combo", "Error", JOptionPane.ERROR_MESSAGE);
						}
						for(int k = 1;k <= 3; k++){
		                    raf.readLine();
		                }
					}
					fr.close();
					raf.close();
				}
				catch(FileNotFoundException ex){
					JOptionPane.showMessageDialog(null, "User Database Not Found", "Error", JOptionPane.ERROR_MESSAGE);
				}
				catch (IOException exe) {
					JOptionPane.showMessageDialog(null, "Cannot Write to the file", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		loginButton.setBounds(80, 220, 100, 23);
		contentPane.add(loginButton);
		
		JButton registerButton = new JButton("New Account");
		registerButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		registerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				NewSupplierScreen.main(null);
				dispose();
			}
		});
		registerButton.setBounds(230, 220, 150, 23);
		contentPane.add(registerButton);
		
		JButton exitButton = new JButton("Exit");
		exitButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		exitButton.setBounds(150, 285, 100, 23);
		contentPane.add(exitButton);
		
		JButton backButton = new JButton("Return");
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LoginScreen.main(null);
				dispose();
			}
		});
		backButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		backButton.setBounds(314, 330, 110, 20);
		contentPane.add(backButton);
		exitButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				JFrame frmLoginSystem = new JFrame("Exit");
				if (JOptionPane.showConfirmDialog(frmLoginSystem, "Confirm if you want to exit", "Login Systems", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_NO_OPTION) {
					System.exit(0);
				}
			}
		});
	}
}
