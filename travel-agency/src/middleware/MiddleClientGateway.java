/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middleware;

import booking.model.client.ClientBookingReply;
import booking.model.client.ClientBookingRequest;
import gateway.BookingSerializer;
import gateway.Constants;
import gateway.MessageReceiver;
import gateway.MessageSender;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import javax.naming.NamingException;

/**
 *
 * @author tycho
 */
public abstract class MiddleClientGateway {
    
    private MessageSender sender;
    private MessageReceiver receiver;
    private BookingSerializer serializer;
    private HashMap<ClientBookingRequest, Message> hm;

    public MiddleClientGateway() {
        try {
            sender = new MessageSender();
            hm = new HashMap<>();
            serializer = new BookingSerializer();
            receiver = new MessageReceiver(Constants.clientMiddleDest);
            receiver.setListener((Message msg) -> {
                ClientBookingRequest request;                
                try {
                    request = serializer.requestFromString(((TextMessage) msg).getText());
                    hm.put(request, msg);
                    onBookingRequestArrived(request);
                } catch (JMSException ex) {
                    Logger.getLogger(MiddleClientGateway.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        } catch (NamingException | JMSException ex) {
            Logger.getLogger(MiddleClientGateway.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void sendBookingReply(ClientBookingRequest request, ClientBookingReply reply){
        try {
            Message msg = sender.createTextMessage(serializer.replyToString(reply));
            msg.setJMSCorrelationID(hm.get(request).getJMSMessageID());
            sender.send(msg, hm.get(request).getJMSReplyTo());
        } catch (JMSException ex) {
            Logger.getLogger(MiddleClientGateway.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public abstract void onBookingRequestArrived(ClientBookingRequest request);
}
