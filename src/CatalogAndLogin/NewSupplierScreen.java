package CatalogAndLogin;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class NewSupplierScreen extends JFrame {

	private JPanel contentPane;
	private JTextField companynameTF;
	private JTextField usernameTF;
	private JTextField passwordTF;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					NewSupplierScreen frame = new NewSupplierScreen();
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
	public NewSupplierScreen() {
		setTitle("Online Shopping System New Supplier Account");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(200, 200, 500, 415);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
		JLabel companynameLabel = new JLabel("Company Name:");
		companynameLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		companynameLabel.setBounds(50, 40, 120, 16);
		contentPane.add(companynameLabel);
		
		companynameTF = new JTextField();
		companynameTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		companynameTF.setBounds(180, 40, 250, 20);
		contentPane.add(companynameTF);
		companynameTF.setColumns(10);
		
		JLabel usernameLabel = new JLabel("Username:");
		usernameLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		usernameLabel.setBounds(50, 100, 120, 16);
		contentPane.add(usernameLabel);
		
		usernameTF = new JTextField();
		usernameTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		usernameTF.setBounds(180, 100, 250, 20);
		contentPane.add(usernameTF);
		usernameTF.setColumns(10);
		
		JLabel passwordLabel = new JLabel("Password:");
		passwordLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		passwordLabel.setBounds(50, 160, 120, 16);
		contentPane.add(passwordLabel);
		
		passwordTF = new JTextField();
		passwordTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		passwordTF.setBounds(180, 160, 250, 20);
		contentPane.add(passwordTF);
		passwordTF.setColumns(10);
		
		JButton newacntButton = new JButton("Create Account");
		newacntButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String username = usernameTF.getText();
				String password = passwordTF.getText();
				String compname = companynameTF.getText();
				int ln = 1;
				File inputFile = new File("DataStuff/SupplierData.txt");
				File newFile = new File("DataStuff/");
				
				if(!newFile.exists()) {
					newFile.mkdirs();
				}
				
				try {
					FileReader fr = new FileReader(inputFile);
					RandomAccessFile raf = new RandomAccessFile(inputFile, "rw");
					ln = 1;
					RandomAccessFile lsraf = new RandomAccessFile("DataStuff/" + username + "_" + compname + ".txt", "rw");
					
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
					raf.writeBytes("Username:" + username + "\r\n");
					raf.writeBytes("Password:" + password + "\r\n");
					raf.writeBytes("Name:" + compname);
					
					lsraf.writeBytes("Username:" + username + "\r\n");
					lsraf.writeBytes("Name:" + compname + "\r\n");
					
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
		newacntButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		newacntButton.setBounds(145, 250, 150, 23);
		contentPane.add(newacntButton);
		
		JButton exitButton = new JButton("Exit");
		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame frmRegSystem = new JFrame("Exit");
				if (JOptionPane.showConfirmDialog(frmRegSystem, "Confirm if you want to exit", "Login Systems", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_NO_OPTION) {
					System.exit(0);
				}
			}
		});
		exitButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		exitButton.setBounds(145, 300, 150, 23);
		contentPane.add(exitButton);
		
		JButton backButton = new JButton("Return");
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SupplierScreen.main(null);
				dispose();
			}
		});
		backButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		backButton.setBounds(364, 345, 110, 20);
		contentPane.add(backButton);
	}
}
