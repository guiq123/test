package rabbitmq.work;

import java.io.IOException;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

public class Receiver {
	public static void main(String[] args) throws Exception {
		//建立连接
		ConnectionFactory f = new ConnectionFactory();
		f.setHost("192.168.64.140");
		f.setUsername("admin");
		f.setPassword("admin");
		Channel c = f.newConnection().createChannel();
		
		//定义队列
		c.queueDeclare("helloworld",false,false,false,null);
		
		DeliverCallback deliverCallback = new DeliverCallback() {
			@Override
			public void handle(String consumerTag, Delivery message) throws IOException {
				String s = new String(message.getBody());
				System.out.println("收到: "+s);
				// 每个点字符暂停一秒, 模拟耗时运算
				for (int i = 0; i < s.length(); i++) {
					if (s.charAt(i) == '.') {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
						}
					}
				}
				System.out.println("--消息处理结束---------------------------\n\n");
			}
		};
		CancelCallback cancelCallback = new CancelCallback() {
			@Override
			public void handle(String consumerTag) throws IOException {
			}
		};
		
		//消费
		c.basicConsume("helloworld", true, deliverCallback, cancelCallback);
	}
}
