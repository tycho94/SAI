/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gateway;

import booking.model.agency.AgencyReply;
import booking.model.agency.AgencyRequest;
import com.owlike.genson.Genson;

/**
 *
 * @author tycho
 */
//Simple Serializer for AgencyRequest and Reply
public class AgencySerializer {

    private Genson genson;

    public AgencySerializer() {
        genson = new Genson();
    }

    public String requestToString(AgencyRequest request) {
        return genson.serialize(request);
    }

    public AgencyRequest requestFromString(String str) {
        return genson.deserialize(str, AgencyRequest.class);
    }

    public String replyToString(AgencyReply reply) {
        return genson.serialize(reply);
    }

    public AgencyReply replyFromSTring(String str) {
        return genson.deserialize(str, AgencyReply.class);
    }
}
