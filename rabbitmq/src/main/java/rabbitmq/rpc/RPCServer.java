package rabbitmq.rpc;

import java.io.IOException;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

public class RPCServer {

	public static void main(String[] args) throws Exception{
		ConnectionFactory f = new ConnectionFactory();
		f.setHost("192.168.64.140"); //101.37.86.214
		f.setPort(5672);
		f.setUsername("admin");
		f.setPassword("admin");
		
		Connection con = f.newConnection();
		Channel c = con.createChannel();
		
		c.queueDeclare("rpc_queue",false,false,false,null);
		DeliverCallback DeliverCallback = new DeliverCallback() {
			
			@Override
			public void handle(String consumerTag, Delivery message) throws IOException {

				String s = new String(message.getBody());
				int n = Integer.parseInt(s);
				long r = fbnq(n);
				
				String replyTo = message.getProperties().getReplyTo();
				String correlationId = message.getProperties().getCorrelationId();
				BasicProperties prop = 
						new BasicProperties.Builder()
						.correlationId(correlationId)
						.build();
				
				c.basicPublish("", replyTo, prop, (""+r).getBytes());
			}
		};
		CancelCallback CancelCallback = new CancelCallback() {
			
			@Override
			public void handle(String consumerTag) throws IOException {
				// TODO Auto-generated method stub
				
			}
		};
		c.basicConsume("rpc_queue", true, DeliverCallback, CancelCallback);
		
	}
	
	static long fbnq(int n) {
		if (n == 1 || n == 2) {
			return 1;
		}
		return fbnq(n-1) + fbnq(n-2);
	}
}
