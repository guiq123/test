package rabbitmq.work;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;

public class Sender {
	public static void main(String[] args) throws Exception {
		//建立连接
		ConnectionFactory f = new ConnectionFactory();
		f.setHost("192.168.64.140");
		//f.setPort(5672);   //jdbc:mysql://localhost:3306/xxxx?....
		f.setUsername("admin");
		f.setPassword("admin");
		Channel c = f.newConnection().createChannel();
		
		//定义队列
		c.queueDeclare("helloworld", false, false, false, null);
		
		//发送数据
		while (true) {
			System.out.print("输入:");
			String s = new Scanner(System.in).nextLine();
			c.basicPublish("", "helloworld", null, s.getBytes());
		}
	}
}






