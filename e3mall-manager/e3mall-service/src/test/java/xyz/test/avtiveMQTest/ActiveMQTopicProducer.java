package xyz.test.avtiveMQTest;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

import javax.jms.*;

public class ActiveMQTopicProducer {

    @Test
    public void testTopicProducer() throws Exception {

//      第一步：创建ConnectionFactory对象，需要指定服务端ip及端口号。
        ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://192.168.25.128:61616");
//      第二步：使用ConnectionFactory对象创建一个Connection对象。
        Connection connection = factory.createConnection();
//      第三步：开启连接，调用Connection对象的start方法。
        connection.start();
//      第四步：使用Connection对象创建一个Session对象。
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
//      第五步：使用Session对象创建一个Destination对象（topic、queue），此处创建一个Topic对象。
        Topic topic = session.createTopic("test_topic");
//      第六步：使用Session对象创建一个Producer对象。
        MessageProducer producer = session.createProducer(topic);
//      第七步：创建一个Message对象，创建一个TextMessage对象。
        TextMessage textMessage = session.createTextMessage("Hello RabbitMQ");
//      第八步：使用Producer对象发送消息。
        producer.send(textMessage);
//      第九步：关闭资源。
        session.close();
        producer.close();
        connection.close();


    }
}
