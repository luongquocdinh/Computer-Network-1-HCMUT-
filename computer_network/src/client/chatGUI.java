package client;

import java.awt.EventQueue;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;



import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Label;
import java.awt.TextArea;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import javax.swing.JProgressBar;
import javax.swing.JPanel;

import data.dataFile;
import tags.DeCode;
import tags.EnCode;
import tags.Tags;
public class chatGUI {

	private static String URL_DIR = System.getProperty("user.dir");
	private static String TEMP = "/temp/";

	private ChatRoom chat;
	private Socket socketChat;
	private String nameUser = "", nameGuest = "", nameFile = "";
	private JFrame frame;
	private JTextField textName;
	private JPanel panelMessage;
	private Label textState, lblReceive;
	private TextArea textDisPlayChat;
	private JButton btnDisConnect, btnSend, btnChoose, btnDel, btnUpLoad;
	public boolean isStop = false, isSendFile = false, isReceiveFile = false;
	private JProgressBar progressSendFile;
	private JTextField textPath;
	private int portServer = 0;
	private JTextField textSend;
	private JPanel panelFile;

	public chatGUI(String user, String guest, Socket socket, int port) {
		nameUser = user;
		nameGuest = guest;
		socketChat = socket;
		this.portServer = port;
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					chatGUI window = new chatGUI(nameUser, nameGuest, socketChat, portServer, 0);
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					chatGUI window = new chatGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void updateChat(String msg) {
		textDisPlayChat.append(msg + "\n");
	}

	public chatGUI() {
		initialize();
	}

	public chatGUI(String user, String guest, Socket socket, int port, int a)
			throws Exception {
		nameUser = user;
		nameGuest = guest;
		socketChat = socket;
		this.portServer = port;
		initialize();
		chat = new ChatRoom(socketChat, nameUser, nameGuest);
		chat.start();
	}

	private void initialize() {
		File fileTemp = new File(URL_DIR + "/temp");
		if (!fileTemp.exists()) {
			fileTemp.mkdirs();
		}
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(200, 200, 688, 559);
		frame.getContentPane().setLayout(null);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		JLabel lblClientIP = new JLabel("Chatting with: ");
		lblClientIP.setBounds(6, 12, 155, 16);
		frame.getContentPane().add(lblClientIP);

		textName = new JTextField(nameUser);
		textName.setEditable(false);
		textName.setBounds(100, 6, 350, 28);
		frame.getContentPane().add(textName);
		textName.setText(nameGuest);
		textName.setColumns(10);

		textDisPlayChat = new TextArea();
		textDisPlayChat.setEditable(false);
		textDisPlayChat.setBounds(6, 40, 668, 317);
		frame.getContentPane().add(textDisPlayChat);

		panelFile = new JPanel();
		panelFile.setBounds(6, 363, 670, 60);
		//panelFile.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "File"));
		frame.getContentPane().add(panelFile);
		panelFile.setLayout(null);

		textPath = new JTextField("");
		textPath.setBounds(100, 21, 388, 25);
		panelFile.add(textPath);
		textPath.setEditable(false);
		textPath.setColumns(10);
		
		////////// Button brown //////////////
		btnChoose = new JButton("Browse");
		btnChoose.setBounds(500, 21, 50, 25);
		panelFile.add(btnChoose);
		btnChoose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File(System
						.getProperty("user.home")));
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int result = fileChooser.showOpenDialog(frame);
				if (result == JFileChooser.APPROVE_OPTION) {
					isSendFile = true;
					String path_send = (fileChooser.getSelectedFile()
							.getAbsolutePath()) ;
					System.out.println(path_send);
					nameFile = fileChooser.getSelectedFile().getName();
					textPath.setText(path_send);
				}
			}
		});
		btnChoose.setBorder(BorderFactory.createEmptyBorder());
		btnChoose.setContentAreaFilled(false);
		
		//////////// Button send //////////
		btnUpLoad = new JButton("Send");
		btnUpLoad.setBounds(550, 21, 50, 25);
		panelFile.add(btnUpLoad);

		btnUpLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (isSendFile)
					try {
						chat.sendMessage(EnCode.sendFile(nameFile));
					} catch (Exception e) {
						e.printStackTrace();
					}
			}
		});
		btnUpLoad.setContentAreaFilled(false);
		btnUpLoad.setBorder(BorderFactory.createEmptyBorder());

		///////////// Button delete //////////////////
		btnDel = new JButton("Clear");
		btnDel.setBounds(600, 21, 50, 25);
		panelFile.add(btnDel);
		btnDel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				isSendFile = false;
				textSend.setText("");
				textPath.setText("");
			}
		});
		btnDel.setContentAreaFilled(false);
		btnDel.setBorder(BorderFactory.createEmptyBorder());

		Label label = new Label("Link send file: ");
		label.setBounds(10, 21, 80, 22);
		panelFile.add(label);

		panelMessage = new JPanel();
		panelMessage.setBounds(6, 420, 670, 71);
		panelMessage.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Message"));
		frame.getContentPane().add(panelMessage);
		panelMessage.setLayout(null);

		textSend = new JTextField("");
		textSend.setBounds(10, 21, 479, 39);
		panelMessage.add(textSend);
		textSend.setColumns(10);

		btnSend = new JButton("SEND");
		btnSend.setBounds(530, 29, 80, 23);
		panelMessage.add(btnSend);
		btnSend.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				if (isStop) {
					updateChat("[" + nameUser +"] : " + textSend.getText().toString());
					textSend.setText("");
					return;
				}
				String msg = textSend.getText();
				if (msg.equals(""))
					return;
				try {
					chat.sendMessage(EnCode.sendMessage(msg));
					updateChat("["+ nameUser +"] : " + msg);
					textSend.setText("");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		textSend.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent arg0) {

			}

			@Override
			public void keyReleased(KeyEvent arg0) {

			}

			@Override
			public void keyPressed(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
					String msg = textSend.getText();
					if (isStop) {
						updateChat("[" + nameUser + "] : " + textSend.getText().toString());
						textSend.setText("");
						return;
					}
					if (msg.equals("")) {
						textSend.setText("");
						textSend.setCaretPosition(0);
						return;
					}
					try {
						chat.sendMessage(EnCode.sendMessage(msg));
						updateChat("["+ nameUser +"] : " + msg);
						textSend.setText("");
						textSend.setCaretPosition(0);
					} catch (Exception e) {
						textSend.setText("");
						textSend.setCaretPosition(0);
					}
				}
			}
		});

		btnDisConnect = new JButton("DISCONNECT");
		btnDisConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int result = Tags.show(frame, "Do you want close chat with "
						+ nameGuest, true);
				if (result == 0) {
					try {
						isStop = true;
						frame.dispose();
						chat.sendMessage(Tags.CHAT_CLOSE_TAG);
						chat.stopChat();
						System.gc();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		btnDisConnect.setBounds(560, 6, 113, 29);
		frame.getContentPane().add(btnDisConnect);

		progressSendFile = new JProgressBar(0, 100);
		progressSendFile.setBounds(93, 510, 388, 14);
		progressSendFile.setStringPainted(true);
		frame.getContentPane().add(progressSendFile);
		progressSendFile.setVisible(false);

		textState = new Label("");
		textState.setBounds(6, 502, 81, 22);
		textState.setVisible(false);
		frame.getContentPane().add(textState);

		lblReceive = new Label("Receiving ...");
		lblReceive.setBounds(491, 510, 83, 14);
		lblReceive.setVisible(false);
		frame.getContentPane().add(lblReceive);

	}

	public class ChatRoom extends Thread {

		private Socket connect;
		private ObjectOutputStream outPeer;
		private ObjectInputStream inPeer;
		private boolean continueSendFile = true, finishReceive = false;
		private int sizeOfSend = 0, sizeOfData = 0, sizeFile = 0,
				sizeReceive = 0;
		private String nameFileReceive = "";
		private InputStream inFileSend;
		private dataFile dataFile;

		public ChatRoom(Socket connection, String name, String guest)
				throws Exception {
			connect = new Socket();
			connect = connection;
			nameGuest = guest;
		}

		@Override
		public void run() {
			super.run();
			OutputStream out = null;
			while (!isStop) {
				try {
					inPeer = new ObjectInputStream(connect.getInputStream());
					Object obj = inPeer.readObject();
					if (obj instanceof String) {
						String msgObj = obj.toString();
						if (msgObj.equals(Tags.CHAT_CLOSE_TAG)) {
							isStop = true;
							Tags.show(frame, nameGuest
									+ " may be close chat with you!", false);
							connect.close();
							break;
						}
						if (DeCode.checkFile(msgObj)) {
							isReceiveFile = true;
							nameFileReceive = msgObj.substring(10,
									msgObj.length() - 11);
							int result = Tags.show(frame, nameGuest
									+ " send file " + nameFileReceive
									+ " for you", true);
							if (result == 0) {
								File fileReceive = new File(URL_DIR + TEMP
										+ "/" + nameFileReceive);
								if (!fileReceive.exists()) {
									fileReceive.createNewFile();
								}
								String msg = Tags.FILE_REQ_ACK_OPEN_TAG
										+ Integer.toBinaryString(portServer)
										+ Tags.FILE_REQ_ACK_CLOSE_TAG;
								sendMessage(msg);
							} else {
								sendMessage(Tags.FILE_REQ_NOACK_TAG);
							}
						} else if (DeCode.checkFeedBack(msgObj)) {
							btnChoose.setEnabled(false);
							btnUpLoad.setEnabled(false);
							btnDel.setEnabled(false);
							new Thread(new Runnable() {
								public void run() {
									try {
										sendMessage(Tags.FILE_DATA_BEGIN_TAG);
										updateChat("you are sending file:	" + nameFile);
										isSendFile = false;
										sendFile(textPath.getText());
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}).start();
						} else if (msgObj.equals(Tags.FILE_REQ_NOACK_TAG)) {
							Tags.show(frame, nameGuest
									+ " wantn't receive file", false);
						} else if (msgObj.equals(Tags.FILE_DATA_BEGIN_TAG)) {
							finishReceive = false;
							lblReceive.setVisible(true);
							out = new FileOutputStream(URL_DIR + TEMP
									+ nameFileReceive);
						} else if (msgObj.equals(Tags.FILE_DATA_CLOSE_TAG)) {
							updateChat("You receive file:	" + nameFileReceive + "with size" + sizeReceive + "KB");
							sizeReceive = 0;
							out.flush();
							out.close();
							lblReceive.setVisible(false);
							new Thread(new Runnable() {

								@Override
								public void run() {
									showSaveFile();
								}
							}).start();
							finishReceive = true;
						} else {
							String message = DeCode.getMessage(msgObj);
							updateChat("[" + nameGuest + "] : " + message);
						}
					} else if (obj instanceof dataFile) {
						dataFile data = (dataFile) obj;
						++sizeReceive;
						out.write(data.data);
					}
				} catch (Exception e) {
					File fileTemp = new File(URL_DIR + TEMP + nameFileReceive);
					if (fileTemp.exists() && !finishReceive) {
						fileTemp.delete();
					}
				}
			}
		}

		private void getData(String path) throws Exception {
			File fileData = new File(path);
			if (fileData.exists()) {
				sizeOfSend = 0;
				dataFile = new dataFile();
				sizeFile = (int) fileData.length();
				sizeOfData = sizeFile % 1024 == 0 ? (int) (fileData.length() / 1024)
						: (int) (fileData.length() / 1024) + 1;
				textState.setVisible(true);
				progressSendFile.setVisible(true);
				progressSendFile.setValue(0);
				inFileSend = new FileInputStream(fileData);
			}
		}

		public void sendFile(String path) throws Exception {
			getData(path);
			textState.setText("Sending ...");
			do {
				if (continueSendFile) {
					continueSendFile = false;
					new Thread(new Runnable() {

						@Override
						public void run() {
							try {
								inFileSend.read(dataFile.data);
								sendMessage(dataFile);
								sizeOfSend++;
								if (sizeOfSend == sizeOfData - 1) {
									int size = sizeFile - sizeOfSend * 1024;
									dataFile = new dataFile(size);
								}
								progressSendFile
										.setValue((int) (sizeOfSend * 100 / sizeOfData));
								if (sizeOfSend >= sizeOfData) {
									inFileSend.close();
									isSendFile = true;
									sendMessage(Tags.FILE_DATA_CLOSE_TAG);
									progressSendFile.setVisible(false);
									textState.setVisible(false);
									isSendFile = false;
									textPath.setText("");
									btnChoose.setEnabled(true);
									btnUpLoad.setEnabled(true);
									btnDel.setEnabled(true);
									updateChat("!!!YOU ARE SEND FILE COMPLETE!!!");
									inFileSend.close();
								}
								continueSendFile = true;
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}).start();
				}
			} while (sizeOfSend < sizeOfData);
		}

		private void showSaveFile() {
			while (true) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File(System
						.getProperty("user.home")));
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int result = fileChooser.showSaveDialog(frame);
				if (result == JFileChooser.APPROVE_OPTION) {
					File file = new File(fileChooser.getSelectedFile()
							.getAbsolutePath() + "/" + nameFileReceive );
					if (!file.exists()) {
						try {
							file.createNewFile();
							Thread.sleep(1000);
							InputStream input = new FileInputStream(URL_DIR
									+ TEMP + nameFileReceive);
							OutputStream output = new FileOutputStream(
									file.getAbsolutePath());
							copyFileReceive(input, output, URL_DIR + TEMP
									+ nameFileReceive);
						} catch (Exception e) {
							Tags.show(frame, "Your file receive has error!!!",
									false);
						}
						break;
					} else {
						int resultContinue = Tags.show(frame,
								"File is exists. You want save file?", true);
						if (resultContinue == 0)
							continue;
						else
							break;
					}
				}
			}
		}

		public synchronized void sendMessage(Object obj) throws Exception {
			outPeer = new ObjectOutputStream(connect.getOutputStream());
			if (obj instanceof String) {
				String message = obj.toString();
				outPeer.writeObject(message);
				outPeer.flush();
				if (isReceiveFile)
					isReceiveFile = false;
			} else if (obj instanceof dataFile) {
				outPeer.writeObject(obj);
				outPeer.flush();
			}
		}

		public void stopChat() {
			try {
				connect.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void copyFileReceive(InputStream inputStr, OutputStream outputStr,
			String path) throws IOException {
		byte[] buffer = new byte[1024];
		int lenght;
		while ((lenght = inputStr.read(buffer)) > 0) {
			outputStr.write(buffer, 0, lenght);
		}
		inputStr.close();
		outputStr.close();
		File fileTemp = new File(path);
		if (fileTemp.exists()) {
			fileTemp.delete();
		}
	}
}
