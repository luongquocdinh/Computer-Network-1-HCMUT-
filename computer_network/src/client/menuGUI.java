package client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.TextArea;

import javax.swing.JButton;

import tags.Tags;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class menuGUI {

	private client clientNode;
	private static String IPClient = "", nameUser = "", dataUser = "";
	private static int portClient = 0;
	private JFrame frame;
	private JTextField textName, textNameFriend;
	private static TextArea textList;
	private JButton btnChat, btnExit;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					menuGUI window = new menuGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public menuGUI(String arg, int arg1, String name, String msg) throws Exception {
		IPClient = arg;
		portClient = arg1;
		nameUser = name;
		dataUser = msg;
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					menuGUI window = new menuGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public menuGUI() throws Exception {
		initialize();
		clientNode = new client(IPClient, portClient, nameUser, dataUser);
	}

	public static void updateFiend(String msg) {
		textList.append(msg + "\n");
	}

	public static void clearAll() {
		textList.setText("");
		textList.setText("");
	}
	
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 330, 556);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		textName = new JTextField(nameUser);
		textName.setEditable(false);
		textName.setColumns(10);
		textName.setBounds(90, 11, 186, 28);
		frame.getContentPane().add(textName);

		JLabel label = new JLabel("User Name: ");
		label.setBounds(10, 17, 82, 16);
		frame.getContentPane().add(label);

		textList = new TextArea();
		textList.setText("");
		textList.setEditable(false);
		textList.setBounds(10, 53, 310, 372);
		frame.getContentPane().add(textList);
		
		JLabel lblFriendsName = new JLabel("Name Friend: ");
		lblFriendsName.setBounds(10, 445, 110, 16);
		frame.getContentPane().add(lblFriendsName);

		textNameFriend = new JTextField("");
		textNameFriend.setColumns(10);
		textNameFriend.setBounds(100, 439, 205, 28);
		frame.getContentPane().add(textNameFriend);

		btnChat = new JButton("Chat");

		btnChat.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				String name = textNameFriend.getText();
				if (name.equals("") || client.client == null) {
					Tags.show(frame, "Name 's friend mistake!", false);
					return;
				}
				if (name.equals(nameUser)) {
					Tags.show(frame, "You can't chat with yourself ><", false);
					return;
				}
				int size = client.client.size();
				for (int i = 0; i < size; i++) {
					if (name.equals(client.client.get(i).getName())) {
						try {
							clientNode.requestChat(client.client.get(i).getHost(),client.client.get(i).getPort(), name);
							return;
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				Tags.show(frame, "FRIEND NOT FOUND", false);
			}
		});
		btnChat.setBounds(10, 478, 113, 29);
		frame.getContentPane().add(btnChat);
		btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int result = Tags.show(frame, "Do you want exit now?", true);
				if (result == 0) {
					try {
						clientNode.exit();
						frame.dispose();
					} catch (Exception e) {
						frame.dispose();
					}
				}
			}
		});
		btnExit.setBounds(190, 478, 113, 29);
		frame.getContentPane().add(btnExit);
		
		////////////// Button Clear ////////////////
		
	}
		
	

	public static int request(String msg, boolean type) {
		JFrame frameMessage = new JFrame();
		return Tags.show(frameMessage, msg, type);
	}
}
