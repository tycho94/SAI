/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adapter;

import gateway.AddressSerializer;
import gateway.Constants;
import gateway.MessageReceiver;
import gateway.MessageSender;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import javax.naming.NamingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import model.TravelRequest;

/**
 *
 * @author tycho
 */
public class GoogleMapsAdapter {

    MessageSender sender;
    MessageReceiver receiver;
    AddressSerializer serializer;

    public GoogleMapsAdapter() {
        try {
            serializer = new AddressSerializer();
            sender = new MessageSender(Constants.googleMiddleDest);
            receiver = new MessageReceiver(Constants.middleGoogleDest);
            //receiver.SetRedelivery();
            receiver.setListener((Message msg) -> {
                try {
                    TravelRequest request = serializer.addressesFromString(((TextMessage) msg).getText());
                    Message m = sender.createTextMessage(Long.toString(GetDistance(request)));
                    m.setJMSCorrelationID(msg.getJMSMessageID());
                    sender.send(m);
                } catch (JMSException ex) {
                    Logger.getLogger(GoogleMapsAdapter.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        } catch (NamingException | JMSException ex) {
            Logger.getLogger(GoogleMapsAdapter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private long GetDistance(TravelRequest request) {
        Client client = ClientBuilder.newClient();

        String uri
                = "https://maps.googleapis.com/maps/api/distancematrix/json?origins="
                + request.getFromAddress()
                + "&destinations=" + request.getToAddress().getCity() + "%7C"
                + Integer.toString(request.getToAddress().getNumber()) + "+"
                + request.getToAddress().getStreet().replace(' ', '+')
                + "&key=AIzaSyBNJJl2xo192UcCN94KWxz5qsxwBSpSDOo";

        WebTarget googleService = client.target(uri);

        Response r = googleService.request().accept(MediaType.APPLICATION_JSON).get();

        Object d = r.readEntity(Object.class);
        ArrayList<HashMap> u = (ArrayList<HashMap>) ((HashMap) d).get("rows");
        ArrayList<HashMap> v = (ArrayList<HashMap>) u.get(0).get("elements");
        HashMap w = (HashMap) v.get(0).get("distance");
        return (long) w.get("value");
    }
}
