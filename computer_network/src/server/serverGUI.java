package server;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Inet4Address;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.TextArea;

public class serverGUI {

	public static int port = 8080;
	private JFrame frame;
	private JTextField txtIP, txtPort;
	private static TextArea txtMessage;
	private static JLabel lblNumber;
	server server;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					serverGUI window = new serverGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public serverGUI() {
		initialize();
	}

	public static void updateMessage(String msg) {
		txtMessage.append(msg + "\n");
	}

	public static void updateNumberClient() {
		int number = Integer.parseInt(lblNumber.getText());
		lblNumber.setText(Integer.toString(number + 1));
	}

	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(200, 200, 622, 442);			////// Vi tri form server
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblIP = new JLabel("IP ADDRESS :");
		lblIP.setBounds(36, 55, 76, 16);					////// Vi tri lbl IP
		frame.getContentPane().add(lblIP);

		txtIP = new JTextField();
		txtIP.setEditable(false);
		txtIP.setBounds(120, 49, 176, 28);				////// Vi tri text Ip
		frame.getContentPane().add(txtIP);
		txtIP.setColumns(10);
		try {
			txtIP.setText(Inet4Address.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		JLabel lblNewLabel = new JLabel("PORT : ");
		lblNewLabel.setBounds(315, 55, 61, 16);			////// Vi tri lbl Port
		frame.getContentPane().add(lblNewLabel);

		txtPort = new JTextField();
		txtPort.setEditable(false);
		txtPort.setColumns(10);
		txtPort.setBounds(366, 49, 208, 28);
		frame.getContentPane().add(txtPort);			///// Vi tri cua text Port
		txtPort.setText("8080");

		JButton btnStart = new JButton("START");
		btnStart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					server = new server(8080);
					serverGUI.updateMessage("START SERVER");
				} catch (Exception e) {
					serverGUI.updateMessage("START ERROR");
					e.printStackTrace();
				}
			}
		});
		btnStart.setBounds(36, 90, 260, 29);			/////// Vi tri button START
		frame.getContentPane().add(btnStart);
		
		JLabel lblNhom = new JLabel("Nhom 3");
		lblNhom.setBounds(290, 6, 109, 16);
		frame.getContentPane().add(lblNhom);

		txtMessage = new TextArea();					
		txtMessage.setEditable(false);
		txtMessage.setBounds(6, 130, 602, 270);		////// Vi tri textArea
		frame.getContentPane().add(txtMessage);

		JButton btnStop = new JButton("STOP");
		btnStop.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					server.stopserver();
					serverGUI.updateMessage("STOP SERVER");
				} catch (Exception e) {
					e.printStackTrace();
					serverGUI.updateMessage("STOP SERVER");
				}
			}
		});
		btnStop.setBounds(315, 90, 260, 29);						//// Vi tri button Stop
		frame.getContentPane().add(btnStop);

	}
}

