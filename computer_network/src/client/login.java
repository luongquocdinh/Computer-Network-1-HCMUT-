package client;

import java.awt.EventQueue;
import java.util.Random;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import client.menuGUI;
import tags.EnCode;
import tags.Tags;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class login {

	private static String NAME_FAILED = "CONNECT WITH OTHER NAME";
	private static String NAME_EXSIST = "NAME IS EXSISED";
	private static String SERVER_NOT_START = "SERVER NOT START";

	private Pattern checkName = Pattern.compile("[a-zA-Z][^<>]*");

	private JFrame frame;
	private JTextField textField_1;
	private JLabel lblError;
	private String name = "", IP = "";
	private JTextField textIP;
	private JTextField textName;
	private JButton btnlogin;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					login window = new login();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public login() {
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 448, 204);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblHiWelcomeConnect = new JLabel("Connect With Server\r\n");
		lblHiWelcomeConnect.setBounds(10, 11, 258, 14);
		frame.getContentPane().add(lblHiWelcomeConnect);

		JLabel lblHostServer = new JLabel("IP Server : ");
		lblHostServer.setBounds(10, 50, 86, 20);
		frame.getContentPane().add(lblHostServer);

		JLabel lblPortServer = new JLabel("Port Server : ");
		lblPortServer.setBounds(263, 53, 79, 14);
		frame.getContentPane().add(lblPortServer);

		textField_1 = new JTextField();
		textField_1.setText("8080");
		textField_1.setEditable(false);
		textField_1.setColumns(10);
		textField_1.setBounds(356, 50, 65, 20);
		frame.getContentPane().add(textField_1);

		JLabel lblNewLabel = new JLabel("User Name: ");
		lblNewLabel.setBounds(10, 82, 86, 17);
		frame.getContentPane().add(lblNewLabel);

		lblError = new JLabel("");
		lblError.setBounds(120, 141, 380, 14);
		frame.getContentPane().add(lblError);

		textIP = new JTextField();
		textIP.setBounds(101, 46, 152, 28);
		frame.getContentPane().add(textIP);
		textIP.setColumns(10);

		textName = new JTextField();
		textName.setColumns(10);
		textName.setBounds(101, 77, 152, 30);
		frame.getContentPane().add(textName);

		btnlogin = new JButton("login");
		btnlogin.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				name = textName.getText();
				lblError.setVisible(false);
				IP = textIP.getText();
				if (checkName.matcher(name).matches() && !IP.equals("")) {
					try {
						Random rd = new Random();
						int portPeer = 10000 + rd.nextInt() % 1000;
						InetAddress ipServer = InetAddress.getByName(IP);
						int portServer = Integer.parseInt("8080");
						Socket socketClient = new Socket(ipServer, portServer);
						String msg = EnCode.getCreateAccount(name,Integer.toString(portPeer));
						ObjectOutputStream serverOutputStream = new ObjectOutputStream(socketClient.getOutputStream());
						serverOutputStream.writeObject(msg);
						serverOutputStream.flush();
						ObjectInputStream serverInputStream = new ObjectInputStream(socketClient.getInputStream());
						msg = (String) serverInputStream.readObject();
						socketClient.close();
						if (msg.equals(Tags.SESSION_DENY_TAG)) {
							lblError.setText(NAME_EXSIST);
							lblError.setVisible(true);
							return;
						}
						new menuGUI(IP, portPeer, name, msg);
						frame.dispose();
					} catch (Exception e) {
						lblError.setText(SERVER_NOT_START);
						lblError.setVisible(true);
						e.printStackTrace();
					}
				} else {
					lblError.setText(NAME_FAILED);
					lblError.setVisible(true);
					lblError.setText(NAME_FAILED);
				}
			}
		});
		btnlogin.setBounds(263, 78, 169, 29);
		frame.getContentPane().add(btnlogin);
		lblError.setVisible(false);
		
		JButton btnclear = new JButton("Clear");
		btnclear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				textIP.setText("");
				textName.setText("");
			}
		});
		btnclear.setBounds(6, 120, 100, 29);
		frame.getContentPane().add(btnclear);
		lblError.setVisible(false);
		
	}
}

