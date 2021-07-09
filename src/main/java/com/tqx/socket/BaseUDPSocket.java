package com.tqx.socket;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class BaseUDPSocket {
	private int localPort;
	private int remotePort;
	private String remoteAddress = "";
	private DatagramSocket readSocket;

	private static Thread readThread;
	private boolean isReading = true;

	public static int DEFAULT_LOCAL_PORT = 8888;
	public static int DEFAULT_REMOTE_PORT = 8889;
	public static String DEFAULT_REMOTE_ADDRESS = "127.0.0.1";

	public BaseUDPSocket() {
		this(DEFAULT_LOCAL_PORT, DEFAULT_REMOTE_PORT, DEFAULT_REMOTE_ADDRESS);
	}

	public BaseUDPSocket(int localPort, int remotePort, String remoteAddress) {
		this.localPort = localPort;
		this.remotePort = remotePort;
		this.remoteAddress = remoteAddress;
	}

	public boolean isReading() {
		return isReading;
	}

	public void setReading(boolean isReading) {
		this.isReading = isReading;
	}

	public void setLocalPort(int localPort) {
		this.localPort = localPort;
	}

	public int getLocalPort() {
		return localPort;
	}

	public void setRemotePort(int remotePort) {
		this.remotePort = remotePort;
	}

	public int getRemotePort() {
		return remotePort;
	}

	public String getRemoteAddress() {
		return remoteAddress;
	}

	public void setRemoteAddress(String remoteAddress) {
		this.remoteAddress = remoteAddress;
	}

	public void read() {
		try {
			while (isReading) {
				if (readSocket == null) {
					readSocket = new DatagramSocket(localPort);
				}
				byte[] recvByte = new byte[1024];
				DatagramPacket recvPacket = new DatagramPacket(recvByte, recvByte.length);

				// 同步
				readSocket.receive(recvPacket);
				String recvMsg = new String(recvPacket.getData(), 0, recvPacket.getLength());

				OnMessage(recvMsg);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			readSocket = null;
		}

	}

	public void send(String sendMsg) {

		DatagramSocket sendSocket = null;
		try {
			sendSocket = new DatagramSocket();
			InetAddress addr = InetAddress.getByName(remoteAddress);

			byte[] sendByte = sendMsg.getBytes();
			DatagramPacket sendPacket = new DatagramPacket(sendByte, sendByte.length, addr, remotePort);

			sendSocket.send(sendPacket);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sendSocket.close();
		}
	}

	public void join() {
		/* 线程开始 */
		isReading = true;
		readThread = new Thread(new Runnable() {
			public void run() {
				read();
			}
		});
		readThread.start();
	}

	public void OnMessage(String recvMsg) {
	}

	public void close() {
		if (readSocket != null) {
			isReading = false;
			readSocket = null;
		}
	}
}
