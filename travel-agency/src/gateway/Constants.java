/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gateway;

/**
 *
 * @author tycho
 */
public class Constants {
    // communication between client/broker/bank
    public static final String clientMiddleDest = "ClientMiddleDest";
    public static final String middleClientDest = "MiddleClientDest";
    public static final String middleAgencyDest1 = "MiddleAgencyDestCheap";
    public static final String middleAgencyDest2 = "MiddleAgencyDestGood";
    public static final String middleAgencyDest3 = "MiddleAgencyDestFast";
    public static final String agencyMiddleDest = "AgencyMiddleDest";
    //channel adapter destinations
    public static final String middleCreditdest = "MiddleCredit";
    public static final String creditMiddledest = "CreditMiddle";
    public static final String middleAcceptdest = "MiddleAccepted";
    public static final String middleRejectdest = "MiddleRejected";
    
    
    //rules
    public static final String cheap1 = "#{transfer} >= 10 && #{transfer} <= 50";
    public static final String good2 = "#{transfer} >= -1 && #{transfer} <= 40";
    public static final String fast3 = "#{transfer} <= -1";
}
