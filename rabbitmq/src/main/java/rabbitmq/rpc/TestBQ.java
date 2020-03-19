package rabbitmq.rpc;

import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class TestBQ {

	static BlockingQueue<Double> q = new ArrayBlockingQueue<>(10);
	public static void main(String[] args) {
		new Thread() {
			@Override
			public void run() {
				System.out.println("回车放入数据:");
				new Scanner(System.in).nextLine();
				double d = Math.random();
				q.offer(d);
				System.out.println("已放入数据:"+d);
			}
		}.start();
		new Thread() {
			@Override
			public void run() {
				try {
					System.out.println("\n等待拿数据\n");
					double d = q.take();
					System.out.println("已取出数据:" + d);
				} catch (Exception e) {
				}
			}
		}.start();
		
		
	}
}
