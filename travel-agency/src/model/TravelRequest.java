/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import booking.model.client.Address;

/**
 *
 * @author tycho
 */
public class TravelRequest {

    public String fromAirport;
    public Address toAddress;

    public TravelRequest() {
    }

    public TravelRequest(String fromAddress, Address toAddress) {
        this.fromAirport = fromAddress;
        this.toAddress = toAddress;
    }

    public void setFromAirport(String fromAirport) {
        this.fromAirport = fromAirport;
    }

    public void setToAddress(Address toAddress) {
        this.toAddress = toAddress;
    }

    public String getFromAddress() {
        return fromAirport;
    }

    public Address getToAddress() {
        return toAddress;
    }

}
