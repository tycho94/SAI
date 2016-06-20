/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middleware;

import booking.model.client.ClientBookingRequest;
import gateway.TravelRequestSerializer;
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
import model.TravelRequest;

/**
 *
 * @author tycho
 * Gateway to the googleAdapter
 */
public abstract class MiddleGoogleGateway {

    MessageSender sender;
    MessageReceiver receiver;
    TravelRequestSerializer serializer;
    HashMap<String, ClientBookingRequest> hm = new HashMap<>();

    public MiddleGoogleGateway() {
        try {
            hm = new HashMap<>();
            serializer = new TravelRequestSerializer();

            receiver = new MessageReceiver(Constants.googleMiddleDest);
            receiver.SetRedelivery();
            receiver.setListener((Message msg) -> {
                try {
                    Long distance = Long.valueOf(((TextMessage) msg).getText());                    
                    onDistanceArrived(hm.get(msg.getJMSCorrelationID()), distance.doubleValue());
                } catch (JMSException ex) {
                    Logger.getLogger(MiddleGoogleGateway.class.getName()).log(Level.SEVERE, null, ex);
                }
            });

        } catch (NamingException | JMSException ex) {
            Logger.getLogger(MiddleGoogleGateway.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //send the request to the adapter
    public void sendDistanceRequest(ClientBookingRequest request) {
        try {
            sender = new MessageSender(Constants.middleGoogleDest);
            Message m = sender.createTextMessage(
                    serializer.travelRequestToString(new TravelRequest(request.getDestinationAirport(), request.getTransferToAddress())));
            sender.send(m);
            hm.put(m.getJMSMessageID(), request);
        } catch (NamingException | JMSException ex) {
            Logger.getLogger(MiddleGoogleGateway.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public abstract void onDistanceArrived(ClientBookingRequest request, double distance);
}
