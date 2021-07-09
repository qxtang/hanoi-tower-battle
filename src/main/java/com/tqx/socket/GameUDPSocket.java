package com.tqx.socket;

import com.tqx.entity.Publisher;

import java.util.Observer;

public class GameUDPSocket extends BaseUDPSocket{

	public static String JOIN = "join";
	public static String CONNECT = "connect";
	public static String CONNECTED = "connected";
	public static String START = "start";
	public static String FIRST_CLICK = "firstClick";
	public static String SECOND_CLICK = "secondClick";
	public static String WIN = "win";
	public static String EXIT = "exit";
	public static String SEMICOLON = ";";
	
	private Publisher publisher = new Publisher();
	
	public GameUDPSocket() {
		super();
	}
	public GameUDPSocket(int localPort, int remotePort, String remoteAddress) {
		super(localPort, remotePort, remoteAddress);
	}

	@Override
	public void OnMessage(String recvMsg) {
		String[] paramArray = resolveData(recvMsg);
		publisher.notifyObservers(paramArray);
	}
	
	private String[] resolveData(String msg){
		return msg.split(SEMICOLON);
	}
	
	public void addObserver(Observer o){
		publisher.addObserver(o);
	}
	
	public void deleteObserver(Observer o){
		publisher.deleteObserver(o);
	}
	
	public int countObservers(){
		return publisher.countObservers();
	}
	
	
}
