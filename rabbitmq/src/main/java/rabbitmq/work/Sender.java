package rabbitmq.work;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;

public class Sender {
	public static void main(String[] args) throws Exception {
		//��������
		ConnectionFactory f = new ConnectionFactory();
		f.setHost("192.168.64.140");
		//f.setPort(5672);   //jdbc:mysql://localhost:3306/xxxx?....
		f.setUsername("admin");
		f.setPassword("admin");
		Channel c = f.newConnection().createChannel();
		
		//�������
		c.queueDeclare("helloworld", false, false, false, null);
		
		//��������
		while (true) {
			System.out.print("����:");
			String s = new Scanner(System.in).nextLine();
			c.basicPublish("", "helloworld", null, s.getBytes());
		}
	}
}






