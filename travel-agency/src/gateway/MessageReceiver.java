/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gateway;

import java.util.Properties;
import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;

/**
 *
 * @author tycho
 */
public final class MessageReceiver {

    Connection connection;
    Session session;
    Destination destination;
    MessageConsumer consumer;

    //Constructor to create a Receiver
    public MessageReceiver(String channelName) throws NamingException, JMSException {
        Properties props = new Properties();
        props.setProperty(Context.INITIAL_CONTEXT_FACTORY,
                "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        props.setProperty(Context.PROVIDER_URL,
                "tcp://localhost:61616");
        props.put(("queue." + channelName), channelName);
        Context jndiContext = new InitialContext(props);
        ActiveMQConnectionFactory connectionFactory = (ActiveMQConnectionFactory) jndiContext
                .lookup("ConnectionFactory");
        connectionFactory.setTrustAllPackages(true);
        connection = connectionFactory.createConnection();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        destination = (Destination) jndiContext.lookup(channelName);
        consumer = session.createConsumer(destination);
        // Turn off redelivery by standard
        StopRedelivery();
        connection.start();
    }

    //Set message Listener
    public void setListener(MessageListener ml) throws JMSException {
        consumer.setMessageListener(ml);
    }

    //Get destination to set ReplyTo
    public Destination getDestination() {
        return destination;
    }

    //This is called after the constructor to turn on redelivery for specific receivers
    public void SetRedelivery() throws JMSException {
        RedeliveryPolicy policy = ((ActiveMQConnection) connection).getRedeliveryPolicy();
        policy.setInitialRedeliveryDelay(10000);
        policy.setRedeliveryDelay(3000);
        policy.setMaximumRedeliveries(3);
    }

    //This is Called in the constructor.
    public void StopRedelivery() throws JMSException {
        RedeliveryPolicy policy = ((ActiveMQConnection) connection).getRedeliveryPolicy();
        policy.setMaximumRedeliveries(0);
    }

}
