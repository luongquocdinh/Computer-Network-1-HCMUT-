package server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import data.dataPeer;
import tags.DeCode;
import tags.Tags;

public class server {

	private ArrayList<dataPeer> dataPeer = null;	
	private ServerSocket server;						
	private Socket connection;			
	private ObjectOutputStream obOutputClient;		
	private ObjectInputStream obInputStream;			
	public boolean isStop = false, isExit = false;		
	
	/////////////// Tao server soccket ////////////////
	public server(int port) throws Exception {
		server = new ServerSocket(port);		
		dataPeer = new ArrayList<dataPeer>();
		(new WaitForConnect()).start();			
	}
	
	////////////// Hien tags xml tren server ////////////////
	private String sendSessionAccept() throws Exception {
		String msg = Tags.SESSION_ACCEPT_OPEN_TAG;
		int size = dataPeer.size();				
		for (int i = 0; i < size; i++) {		
			dataPeer peer = dataPeer.get(i);	
			msg += Tags.PEER_OPEN_TAG;			
			msg += Tags.PEER_NAME_OPEN_TAG;
			msg += peer.getName();
			msg += Tags.PEER_NAME_CLOSE_TAG;
			msg += Tags.IP_OPEN_TAG;
			msg += peer.getHost();
			msg += Tags.IP_CLOSE_TAG;
			msg += Tags.PORT_OPEN_TAG;
			msg += peer.getPort();
			msg += Tags.PORT_CLOSE_TAG;
			msg += Tags.PEER_CLOSE_TAG;			
		}
		msg += Tags.SESSION_ACCEPT_CLOSE_TAG;	
		return msg;
	}
	
	//////////// dong server ///////////////
	public void stopserver() throws Exception {
		isStop = true;
		server.close();							
		connection.close();						
	}
	
	////////////// client ket noi toi server ////////////////////
	private boolean waitForConnection() throws Exception {
		connection = server.accept();			
		obInputStream = new ObjectInputStream(connection.getInputStream());		
		String msg = (String) obInputStream.readObject();						
		ArrayList<String> getData = DeCode.getUser(msg);					
		serverGUI.updateMessage(msg);											
		if (getData != null) {
			if (!isExsistName(getData.get(0))) {						
				saveNewPeer(getData.get(0), connection.getInetAddress()			
						.toString(), Integer.parseInt(getData.get(1)));			
				serverGUI.updateMessage(getData.get(0));						
			} else
				return false;
		} else {
			int size = dataPeer.size();					
			DeCode.updatePeerOnline(dataPeer, msg);			
			if (size != dataPeer.size()) {					
				isExit = true;								
			}
		}
		return true;
	}
	
	////////// Luu user name moi ////////////
	private void saveNewPeer(String user, String ip, int port) throws Exception {
		dataPeer newPeer = new dataPeer();		
		if (dataPeer.size() == 0)				
			dataPeer = new ArrayList<dataPeer>();
		newPeer.setPeer(user, ip, port);		
		dataPeer.add(newPeer);					
	}
	
	//////////// Kiem tra trung user name
	private boolean isExsistName(String name) throws Exception {
		if (dataPeer == null)
			return false;
		int size = dataPeer.size();
		for (int i = 0; i < size; i++) {
			dataPeer peer = dataPeer.get(i);
			if (peer.getName().equals(name))
				return true;
		}
		return false;
	}
	
	/////////// Tao luong, cho connect vao server//////////////////s
	public class WaitForConnect extends Thread {

		@Override
		public void run() {
			super.run();
			try {
				while (!isStop) {
					if (waitForConnection()) {
						if (isExit) {
							isExit = false;
						} else {
							obOutputClient = new ObjectOutputStream(connection.getOutputStream());
							obOutputClient.writeObject(sendSessionAccept());
							obOutputClient.flush();
							obOutputClient.close();
						}
					} else {
						obOutputClient = new ObjectOutputStream(connection.getOutputStream());
						obOutputClient.writeObject(Tags.SESSION_DENY_TAG);
						obOutputClient.flush();
						obOutputClient.close();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}

