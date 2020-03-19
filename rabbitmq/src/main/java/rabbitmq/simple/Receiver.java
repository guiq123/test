package rabbitmq.simple;

import java.io.IOException;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

public class Receiver {
	public static void main(String[] args) throws Exception {
		ConnectionFactory f = new ConnectionFactory();
		f.setHost("192.168.64.140");
		f.setPort(5672);
		f.setUsername("admin");
		f.setPassword("admin");
		Connection con = f.newConnection();
		Channel c = con.createChannel();
		
		c.queueDeclare("helloworld",true,false,false,null);
		
		DeliverCallback deliverCallback = new DeliverCallback() {
			@Override
			public void handle(String consumerTag, Delivery message) throws IOException {
				byte[] a = message.getBody();
				String s = new String(a);
				System.out.println("收到:"+s);
				for (int i = 0; i < s.length(); i++) {
					if (s.charAt(i) == '.') {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
						}
					}
				}
				c.basicAck(message.getEnvelope().getDeliveryTag(), false);
				System.out.println("--消息处理完成---------------/n/n");
			}
		};
		CancelCallback cancelCallback = new CancelCallback() {
			@Override
			public void handle(String consumerTag) throws IOException {
			}
		};
		c.basicQos(1);
		c.basicConsume("helloworld", false, deliverCallback, cancelCallback);
	}
}









