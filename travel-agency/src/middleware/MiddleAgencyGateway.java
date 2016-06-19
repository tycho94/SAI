/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middleware;

import booking.model.agency.AgencyReply;
import booking.model.agency.AgencyRequest;
import gateway.AgencySerializer;
import gateway.Constants;
import gateway.MessageReceiver;
import gateway.MessageSender;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import javax.naming.NamingException;

/**
 *
 * @author tycho
 * Gateway between the middleware and agency on the middleware's side
 */
public abstract class MiddleAgencyGateway {

    private List<MessageSender> senders;
    private MessageReceiver receiver;
    private AgencySerializer serializer;
    private HashMap<String, AgencyRequest> hm;

    public MiddleAgencyGateway() {
        try {
            hm = new HashMap<>();
            serializer = new AgencySerializer();
            senders = new ArrayList<>();
            //add all the agencies to the sender list
            senders.add(new MessageSender(Constants.middleAgencyDest1));
            senders.add(new MessageSender(Constants.middleAgencyDest2));
            senders.add(new MessageSender(Constants.middleAgencyDest3));
            receiver = new MessageReceiver(Constants.agencyMiddleDest);
            receiver.setListener((Message msg) -> {
                try {
                    AgencyReply reply = serializer.replyFromSTring(((TextMessage) msg).getText());
                    onAgencyReplyArrived(hm.get(msg.getJMSCorrelationID()), reply);
                } catch (JMSException ex) {
                    Logger.getLogger(MiddleAgencyGateway.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        } catch (NamingException | JMSException ex) {
            Logger.getLogger(MiddleAgencyGateway.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Forward the request to the agency
    public void sendAgencyRequest(int agency, AgencyRequest request) {
        //integer to identify which agency to send to
        agency--;
        try {
            Message m = senders.get(agency).send(senders.get(agency).createTextMessage(serializer.requestToString(request)));
            hm.put(m.getJMSMessageID(), request);
        } catch (JMSException ex) {
            Logger.getLogger(MiddleAgencyGateway.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public abstract void onAgencyReplyArrived(AgencyRequest request, AgencyReply reply);

}
