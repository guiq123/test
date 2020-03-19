package rabbitmq.route;

import java.io.IOException;
import java.util.Scanner;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

public class Receiver {

	public static void main(String[] args) throws Exception{
		ConnectionFactory f = new ConnectionFactory();
		f.setHost("192.168.64.140"); //101.37.86.214
		f.setPort(5672);
		f.setUsername("admin");
		f.setPassword("admin");
		
		Connection con = f.newConnection();
		Channel c = con.createChannel();
		
		String queue = c.queueDeclare().getQueue();
		c.exchangeDeclare("direct_logs", BuiltinExchangeType.DIRECT);
		System.out.println("请输入绑定键,用空格隔开:");
		String s = new Scanner(System.in).nextLine();
		String[] a = s.split("\\s+");
		
		for (String key : a) {
			c.queueBind(queue, "direct_logs", key);
		}

		DeliverCallback DeliverCallback = new DeliverCallback() {
			
			@Override
			public void handle(String consumerTag, Delivery message) throws IOException {

				String s = new String(message.getBody());
				System.out.println("收到:"+s);
			}
		};
		CancelCallback CancelCallback = new CancelCallback() {
			
			@Override
			public void handle(String consumerTag) throws IOException {
				// TODO Auto-generated method stub
				
			}
		};
		
		c.basicConsume("queue", true, DeliverCallback, CancelCallback);
		
		
	}
}
