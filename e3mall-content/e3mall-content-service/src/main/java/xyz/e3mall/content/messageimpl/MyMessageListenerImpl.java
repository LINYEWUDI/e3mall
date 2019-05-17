package xyz.e3mall.content.messageimpl;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class MyMessageListenerImpl implements MessageListener {
    @Override
    public void onMessage(Message message) {
        //强转消费的数据
        TextMessage textMessage = (TextMessage)message;

        try {
            String text = textMessage.getText();
            System.out.println(text);

        } catch (JMSException e) {
            e.printStackTrace();
        }

    }
}
