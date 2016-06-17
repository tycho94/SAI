/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

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
public abstract class ClientMiddleGateway {
    
    private MessageSender sender;
    private MessageReceiver receiver;
    private BookingSerializer serializer;
    private HashMap<String, ClientBookingRequest> hm;

    public ClientMiddleGateway(String Channel) {
        try {
            hm = new HashMap<>();
            serializer = new BookingSerializer();
            receiver = new MessageReceiver(Constants.middleClientDest + Channel);
            receiver.setListener((Message msg) -> { 
                ClientBookingReply reply = null;
                try {
                    reply = serializer.replyFromSTring(((TextMessage) msg).getText());
                    onBookingReplyArrived(hm.get(msg.getJMSCorrelationID()), reply);
                } catch (JMSException ex) {
                    Logger.getLogger(ClientMiddleGateway.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        } catch (NamingException | JMSException ex) {
            Logger.getLogger(ClientMiddleGateway.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void sendBookingRequest(ClientBookingRequest request){
        try {
            sender = new MessageSender(Constants.clientMiddleDest);
            Message msg = sender.createTextMessage(serializer.requestToString(request));
            msg.setJMSReplyTo(receiver.getDestination());
            sender.send(msg);
            hm.put(msg.getJMSMessageID(), request);
        } catch (NamingException | JMSException ex) {
            Logger.getLogger(ClientMiddleGateway.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
        public abstract void onBookingReplyArrived(ClientBookingRequest request, ClientBookingReply reply);
}