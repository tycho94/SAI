/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gateway;

import com.owlike.genson.Genson;
import model.TravelRequest;

/**
 *
 * @author tycho
 * 
 */
public class TravelRequestSerializer {

    Genson genson;

    public TravelRequestSerializer() {
        genson = new Genson();
    }

    public String travelRequestToString(TravelRequest a) {
        return genson.serialize(a);
    }

    public TravelRequest travelRequestFromString(String str) {
        return genson.deserialize(str, TravelRequest.class);
    }

}
