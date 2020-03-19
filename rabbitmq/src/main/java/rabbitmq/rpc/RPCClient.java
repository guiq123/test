package rabbitmq.rpc;

import java.io.IOException;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

public class RPCClient {
	static BlockingQueue<Long> q = new ArrayBlockingQueue<>(10);

	public static void main(String[] args) throws Exception {
		System.out.println("求第几个斐波那契数:");
		int n = new Scanner(System.in).nextInt();
		long r = f(n);
		System.out.println(n);
	}

	private static long f(int n) throws Exception {
		ConnectionFactory f = new ConnectionFactory();
		f.setHost("192.168.64.140"); // 101.37.86.214
		f.setPort(5672);
		f.setUsername("admin");
		f.setPassword("admin");

		Connection con = f.newConnection();
		Channel c = con.createChannel();

		c.queueDeclare("rpc_queue", false, false, false, null);
		String replyTo = c.queueDeclare().getQueue();
		String correlationId = UUID.randomUUID().toString();
		BasicProperties props = new BasicProperties.Builder().replyTo(replyTo).correlationId(correlationId).build();

		c.basicPublish("", "rpc_queue", props, ("" + n).getBytes());

		System.out.println("其他运算......");

		DeliverCallback DeliverCallback = new DeliverCallback() {

			@Override
			public void handle(String consumerTag, Delivery message) throws IOException {

				String s = new String(message.getBody());
				String cid = message.getProperties().getCorrelationId();
				if (cid.equals(correlationId)) {
					q.offer(Long.parseLong(s));
				}
				try {
					c.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		CancelCallback CancelCallback = new CancelCallback() {

			@Override
			public void handle(String consumerTag) throws IOException {
				// TODO Auto-generated method stub

			}
		};

		c.basicConsume("rpc_queue", true, DeliverCallback, CancelCallback);
		return q.take();
	}
}
