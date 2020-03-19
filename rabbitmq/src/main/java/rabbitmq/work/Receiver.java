package rabbitmq.work;

import java.io.IOException;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

public class Receiver {
	public static void main(String[] args) throws Exception {
		//��������
		ConnectionFactory f = new ConnectionFactory();
		f.setHost("192.168.64.140");
		f.setUsername("admin");
		f.setPassword("admin");
		Channel c = f.newConnection().createChannel();
		
		//�������
		c.queueDeclare("helloworld",false,false,false,null);
		
		DeliverCallback deliverCallback = new DeliverCallback() {
			@Override
			public void handle(String consumerTag, Delivery message) throws IOException {
				String s = new String(message.getBody());
				System.out.println("�յ�: "+s);
				// ÿ�����ַ���ͣһ��, ģ���ʱ����
				for (int i = 0; i < s.length(); i++) {
					if (s.charAt(i) == '.') {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
						}
					}
				}
				System.out.println("--��Ϣ�������---------------------------\n\n");
			}
		};
		CancelCallback cancelCallback = new CancelCallback() {
			@Override
			public void handle(String consumerTag) throws IOException {
			}
		};
		
		//����
		c.basicConsume("helloworld", true, deliverCallback, cancelCallback);
	}
}
