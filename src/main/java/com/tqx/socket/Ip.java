package com.tqx.socket;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Ip {
	
	public static HashMap ping; // ping 后的结果集

	public HashMap getPing() { // 用来得到ping后的结果集
		return ping;
	}

	// 当前线程的数量, 防止过多线程摧毁电脑
	static int threadCount = 0;

	public Ip() {
		ping = new HashMap();
	}

	public void PingAll() throws Exception {
		// 首先得到本机的IP，得到网段
		InetAddress host = InetAddress.getLocalHost();
		String hostAddress = host.getHostAddress();
		int k = 0;
		k = hostAddress.lastIndexOf(".");
		String ss = hostAddress.substring(0, k + 1);
		for (int i = 1; i <= 255; i++) { // 对所有局域网Ip
			String iip = ss + i;
			Ping(iip);
		}

		// 等着所有Ping结束
		while (threadCount > 0){
			Thread.sleep(50);
		}
			
	}
	
	public void Ping(String ip) throws Exception {
		// 最多30个线程
		while (threadCount > 30){
			Thread.sleep(50);
		}
		threadCount += 1;
		PingIp p = new PingIp(ip);
		p.start();
	}

	public static void main(String[] args) throws Exception {
		Ip ip = new Ip();
		ip.PingAll();
		
		Set entries = ping.entrySet();
		Iterator iter = entries.iterator();

		String k;
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String key = (String) entry.getKey();
			String value = (String) entry.getValue();

			if (value.equals("true")){
				System.out.println(key + "-->" + value);
			}
		}
	}

	class PingIp extends Thread {
		public String ip; // IP

		public PingIp(String ip) {
			this.ip = ip;
		}

		public void run() {
			try {
				Process p = Runtime.getRuntime().exec("ping " + ip + " -w 300 -n 1");
				InputStreamReader ir = new InputStreamReader(p.getInputStream());
				LineNumberReader input = new LineNumberReader(ir);
				// 读取结果行
				for (int i = 1; i < 7; i++){
					input.readLine();
				}
				String line = input.readLine();
				System.out.println(line);
				
				if (line!=null) {
					if (line.length() < 17 || line.substring(8, 17).equals("timed out")){
						ping.put(ip, "false");
					}else{
						ping.put(ip, "true");
					}
				}
				
				// 线程结束
				threadCount -= 1;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
}
