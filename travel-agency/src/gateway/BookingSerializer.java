/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gateway;

import booking.model.client.ClientBookingReply;
import booking.model.client.ClientBookingRequest;
import com.owlike.genson.Genson;

/**
 *
 * @author tycho
 */
public class BookingSerializer {

    private Genson genson;

    public BookingSerializer() {
        genson = new Genson();
    }

    public String requestToString(ClientBookingRequest request) {
        return genson.serialize(request);
    }

    public ClientBookingRequest requestFromString(String str) {
        return genson.deserialize(str, ClientBookingRequest.class);
    }

    public String replyToString(ClientBookingReply reply) {
        return genson.serialize(reply);
    }

    public ClientBookingReply replyFromSTring(String str) {
        return genson.deserialize(str, ClientBookingReply.class);
    }
}
