package com.example.app;

import java.util.concurrent.TimeUnit;

public class JVMShutdownHookTest {
	public static void main(String[] args) {
		JVMShutdownHook jvmShutdownHook = new JVMShutdownHook();
		Runtime.getRuntime().addShutdownHook(jvmShutdownHook);
		System.out.println("NNNNN JVM Shutdown Hook Registered.");
		System.out.println("Pre exit.");
		
		try {
			System.out.println("sleeping now");
			TimeUnit.SECONDS.sleep(30);
			System.out.println("waken up");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		try {
//			System.out.println("going to sleep");
//			Thread.sleep(10000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		throw new NullPointerException();
		//System.exit(0);
//		System.out.println("Post exit.");
	}

	private static class JVMShutdownHook extends Thread {
		public void run() {
			System.out.println("Clean up here");
		}
	}
}