package rabbitmq.simple;

import java.io.IOException;
import java.util.Scanner;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

public class Sender {
	public static void main(String[] args) throws Exception {
		ConnectionFactory f = new ConnectionFactory();
		f.setHost("192.168.64.140"); //101.37.86.214
		f.setPort(5672);
		f.setUsername("admin");
		f.setPassword("admin");
		
		Connection con = f.newConnection();
		Channel c = con.createChannel();
		
		c.queueDeclare("helloworld", true,false,false,null);
		
		while (true) {
			System.out.println("输入:");
			String s = new Scanner(System.in).nextLine();
			c.basicPublish("", "helloworld", MessageProperties.PERSISTENT_TEXT_PLAIN, s.getBytes());
		}
	}
}
