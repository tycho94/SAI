/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agency;

import booking.model.agency.AgencyReply;
import booking.model.agency.AgencyRequest;
import gateway.AgencySerializer;
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
 * Gateway between the middleware and agency on the agency's side
 * 
 * Follows the basic rules like every other gateway
 */
public abstract class AgencyMiddleGateway {

    private MessageSender sender;
    private MessageReceiver receiver;
    private AgencySerializer serializer;
    private HashMap<AgencyRequest, String> hm;

    public AgencyMiddleGateway(String Channel) {
        try {
            hm = new HashMap<>();
            serializer = new AgencySerializer();
            receiver = new MessageReceiver(Constants.middleAgencyDest + Channel);
            receiver.setListener((Message msg) -> {
                AgencyRequest request = null;
                try {
                    request = serializer.requestFromString(((TextMessage) msg).getText());
                    hm.put(request, msg.getJMSMessageID());
                    onAgencyRequestArrived(request);
                } catch (JMSException ex) {
                    Logger.getLogger(AgencyMiddleGateway.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        } catch (NamingException | JMSException ex) {
            Logger.getLogger(AgencyMiddleGateway.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    //send the reply to a request to the middleware
    public void replyToAgencyRequest(AgencyRequest request, AgencyReply reply) {
        try {
            //Send a reply according back and set the correlation ID to the request using a hashmap            
            sender = new MessageSender(Constants.agencyMiddleDest);
            Message msg = sender.createTextMessage(serializer.replyToString(reply));
            msg.setJMSCorrelationID(hm.get(request));
            sender.send(msg);
        } catch (NamingException | JMSException ex) {
            Logger.getLogger(AgencyMiddleGateway.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public abstract void onAgencyRequestArrived(AgencyRequest request);
}
