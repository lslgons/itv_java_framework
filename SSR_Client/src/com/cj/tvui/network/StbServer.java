package com.cj.tvui.network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

import com.cj.tvui.util.LOG;

/**
 * 클라이언트 소켓
 * @author daegon.kim (daegon.kim1@cj.net)
 * @deprecated
 */
public class StbServer {
	
	private static StbServer instance=null;
	public static StbServer getInstance() {
		if(instance == null) instance = new StbServer();
		return instance;
	}
	
	List receiverPool;
	Thread _thread;
	StbServerImpl stbServerImpl;
	
	private class StbServerImpl implements Runnable {

		public boolean isRun=false;
		
		public StbServerImpl() {
			isRun = true;
		}
		
		public void run() {
			// TODO Auto-generated method stub
			ServerSocket serverSocket = null;
			try {
				serverSocket = new ServerSocket(com.cj.tvui.Constants.STB_SERVER_PORT);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			while(isRun) {
				try {
					Socket connectionSocket = serverSocket.accept();
					BufferedReader br = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
					BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connectionSocket.getOutputStream()));
					String line = null;
					StringBuffer strBuf = new StringBuffer();
					while ((line = br.readLine()) != null) {
						if(line.trim().isEmpty()) break;
						strBuf.append(line+"\r\n");
					}
					bw.write("HTTP/1.1 200 OK \r\n");
					for(int i=0;i<receiverPool.size();++i) {
						System.out.println("2 : "+receiverPool.size());
						ClientMessageReceiver receiver = (ClientMessageReceiver) receiverPool.get(i);
						System.out.println("3");
						if(receiver != null) {
							System.out.println("4");
							receiver.messageReceived(strBuf.toString());
						}
					}
					
					bw.close();
					br.close();
					connectionSocket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			try {
				serverSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	
	private StbServer() {
		receiverPool = new LinkedList();
	}
	
	public interface ClientMessageReceiver {
		/**
		 * 서버로부터 수신된 메시지 Delegator
		 * @param msg 수신된 메시지
		 */
		void messageReceived(final String msg);
	}
	
	public void startServer() {
		if(com.cj.tvui.Constants.STB_SERVER_ENABLE) {
			stbServerImpl = new StbServerImpl();
			_thread = new Thread(stbServerImpl);
			_thread.start();
		}
		
	}
	
	public void stopServer() {
		if(_thread != null) {
			stbServerImpl.isRun = false;
			_thread.interrupt();
			stbServerImpl = null;
			_thread = null;
		}
		
	}
	
	public void registerReceiver(ClientMessageReceiver receiver) {
		for(int i=0;i<receiverPool.size();++i) {
			ClientMessageReceiver _r = (ClientMessageReceiver) receiverPool.get(i);
			if(_r.hashCode() == receiver.hashCode()) {
				LOG.print(this, "This receiver is already registered");
				return;
			}
		}
		LOG.print("Register Receiver"+receiver.toString());
		receiverPool.add(receiver);
	}
	
	public void unregisterReceiver(ClientMessageReceiver receiver) {
		for(int i=0;i<receiverPool.size();++i) {
			ClientMessageReceiver _r = (ClientMessageReceiver) receiverPool.get(i);
			if(_r.hashCode() == receiver.hashCode()) {
				LOG.print(this, "Found receiver");
				receiverPool.remove(i);
			}
		}
	}
	
	public static void main(String args[]) {
		StbServer server = StbServer.getInstance();
		System.out.println("Start Server...");
		
		ClientMessageReceiver r = new ClientMessageReceiver() {
			
			public void messageReceived(String msg) {
				// TODO Auto-generated method stub
				LOG.print(this, "Message Received : " + msg);
				
			}
		};
		server.registerReceiver(r);
		server.startServer();
		
		
		
	}
	
	

}
