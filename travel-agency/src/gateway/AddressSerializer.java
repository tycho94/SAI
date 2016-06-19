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
 */
public class AddressSerializer {

    Genson genson;

    public AddressSerializer() {
        genson = new Genson();
    }

    public String addressesToString(TravelRequest a) {
        return genson.serialize(a);
    }

    public TravelRequest addressesFromString(String str) {
        return genson.deserialize(str, TravelRequest.class);
    }

}
