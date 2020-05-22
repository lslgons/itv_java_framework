package com.tcom.network;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class TimeoutSocket {
	
	/**
     * 세션 연결시도
     * @param ip 서버 IP
     * @param port 서버 port
     * @param delay 연결시도 제한시간
     * @return 연결이 제대로 되면 연결된 Socket 반환
     * @exception Exception 접속 오류 또는 Connection timeout 발생시
     */
    public static Socket getSocket( String ip, int port, long delay ) throws Exception {
        
        long startTime = System.currentTimeMillis();
        SocketThread socketThread = new SocketThread( ip, port );
        ( new Thread( socketThread ) ).start();
        
        while( true ) {
            if( socketThread.isConnected() ) {
                return socketThread.getConnectedSocket();
                
            } else if( socketThread.isConnectionError() ) {
                throw socketThread.getConnectionException();
                
            } else if( ( System.currentTimeMillis() - startTime ) > delay ) {
                throw new Exception();
            }
            
            try {
                Thread.sleep( 100 );
            } catch( Exception ex ) {
                //
            }
        }
    }

	static class SocketThread implements Runnable {
		private String  ip;
        private int     port;
        
        private Socket      connectedSocket;
        private Exception   connectException;
        
        public SocketThread( String ip, int port ) {
            this.ip     = ip;
            this.port   = port;
        }
        
        public void run() {
            try {
                this.connectedSocket = new Socket( this.ip, this.port );
                
            } catch( Exception ex ) {
//                this.connectException = new IOException( "서버에 접속할 수 없습니다."
//                        + System.getProperty( "line.separator" )
//                        + "잠시 후, 다시 이용해 주십시오" );
            	//this.connectException = ex;
            }
            
            try {
				if (this.connectedSocket == null) {
					Thread.sleep(300);

					//this.connectedSocket = new Socket( this.ip, this.port );
                    SocketAddress socketAddress = new InetSocketAddress(this.ip, this.port);
                    this.connectedSocket = new Socket();
                    this.connectedSocket.connect(socketAddress, 10*1000);


				}
			} catch (Exception e) {
				this.connectException = e;
			}
        }
        
        /**
         * 접속 상황 반환
         */
        public boolean isConnected() {
            return this.connectedSocket != null;
        }
        
        /**
         * 접속 에러 상황 반환
         */
        public boolean isConnectionError() {
            return this.connectException != null;
        }
        
        /**
         * 접속 Socket 반환
         */
        public Socket getConnectedSocket() {
            return this.connectedSocket;
        }
        
        /**
         * 접속 에러 Exception 객체 반환
         */
        public Exception getConnectionException() {
            return this.connectException;
        }
		
	}
}
