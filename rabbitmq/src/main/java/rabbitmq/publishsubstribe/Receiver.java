package rabbitmq.publishsubstribe;

import java.io.IOException;
import java.util.UUID;

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
		c.exchangeDeclare("logs", "fanout");
		c.queueBind(queue, "logs", "");
		
		DeliverCallback DeliverCallback = new DeliverCallback() {
			
			@Override
			public void handle(String consumerTag, Delivery message) throws IOException {

				byte[] a = message.getBody();
				String s = new String(a);
				System.out.println("收到:"+s);
			}
		};
		CancelCallback CancelCallback = new CancelCallback() {
			
			@Override
			public void handle(String consumerTag) throws IOException {
				// TODO Auto-generated method stub
				
			}
		};
		c.basicConsume(queue, true, DeliverCallback, CancelCallback);
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	}
}
