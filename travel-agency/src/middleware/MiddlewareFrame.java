/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middleware;

import booking.model.agency.AgencyReply;
import booking.model.agency.AgencyRequest;
import booking.model.client.ClientBookingReply;
import booking.model.client.ClientBookingRequest;
import gateway.Constants;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import model.BookingAgencyListLine;
import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;

/**
 *
 * @author tycho
 */
public class MiddlewareFrame extends javax.swing.JFrame {

    private final DefaultListModel<BookingAgencyListLine> listModel;
    private final HashMap<AgencyRequest, ClientBookingRequest> hmRequests;
    private final HashMap<AgencyRequest, Integer> hmAggregator;
    private final MiddleClientGateway gatewayClient;
    private final MiddleAgencyGateway gatewayAgency;
    private final MiddleGoogleGateway gatewayGoogle;

    /**
     * Creates new form MiddlewareFrame
     */
    public MiddlewareFrame() {
        listModel = new DefaultListModel<>();
        initComponents();
        hmRequests = new HashMap<>();
        hmAggregator = new HashMap<>();

        gatewayClient = new MiddleClientGateway() {
            @Override
            public void onBookingRequestArrived(ClientBookingRequest request) {
                if (request.getTransferToAddress() != null) {
                    gatewayGoogle.sendDistanceRequest(request);
                } else {
                    AgencyRequest newRequest
                            = new AgencyRequest(request.getDestinationAirport(),
                                    request.getOriginAirport(), -1);

                    int counter = 0;
                    if (Rule(Constants.cheap1, newRequest)) {
                        gatewayAgency.sendAgencyRequest(1, newRequest);
                        counter++;
                    }
                    if (Rule(Constants.good2, newRequest)) {
                        gatewayAgency.sendAgencyRequest(2, newRequest);
                        counter++;
                    }
                    if (Rule(Constants.fast3, newRequest)) {
                        gatewayAgency.sendAgencyRequest(3, newRequest);
                        counter++;
                    }
                    add(newRequest);
                    hmRequests.put(newRequest, request);
                    if (counter > 0) {
                        hmAggregator.put(newRequest, counter);
                    }
                }
            }
        };

        gatewayAgency = new MiddleAgencyGateway() {
            @Override
            public void onAgencyReplyArrived(AgencyRequest request, AgencyReply reply) {
                if (hmAggregator.get(request) > 0) {
                    hmAggregator.put(request, (hmAggregator.get(request) - 1));

                    if (getCurrentReply(request) != null) {
                        if (getCurrentReply(request).getTotalPrice() == -1 && reply.getTotalPrice() > 0) {
                            add(request, reply);
                        }
                        if (getCurrentReply(request).getTotalPrice() > reply.getTotalPrice() && reply.getTotalPrice() > 0) {
                            add(request, reply);
                        }
                    } else {
                        add(request, reply);
                    }
                }

                if (hmAggregator.get(request) == 0) {
                    ClientBookingReply newReply = new ClientBookingReply(getCurrentReply(request).getNameAgency(), getCurrentReply(request).getTotalPrice());

                    gatewayClient.sendBookingReply(hmRequests.get(request), newReply);
                }
            }
        };

        gatewayGoogle = new MiddleGoogleGateway() {
            @Override
            public void onDistanceArrived(ClientBookingRequest request, long distance) {
                AgencyRequest newRequest
                        = new AgencyRequest(request.getDestinationAirport(),
                                request.getOriginAirport(), distance / 1000);
                int counter = 0;
                if (Rule(Constants.cheap1, newRequest)) {
                    gatewayAgency.sendAgencyRequest(1, newRequest);
                    counter++;
                }
                if (Rule(Constants.good2, newRequest)) {
                    gatewayAgency.sendAgencyRequest(2, newRequest);
                    counter++;
                }
                if (Rule(Constants.fast3, newRequest)) {
                    gatewayAgency.sendAgencyRequest(3, newRequest);
                    counter++;
                }
                add(newRequest);
                hmRequests.put(newRequest, request);
                if (counter > 0) {
                    hmAggregator.put(newRequest, counter);
                }
            }
        };
    }

    /**
     * @param rule Rule to check
     * @param request The request in question
     * @return if the request is OK according to the rule true else false
     */
    public boolean Rule(String rule, AgencyRequest request) {
        String resultString;
        boolean result = false;
        Evaluator evaluator = new Evaluator();

        evaluator.putVariable("transfer", Double.toString(request.getTransferDistance()));

        try {
            resultString = evaluator.evaluate(rule);
            result = resultString.equals("1.0");
        } catch (EvaluationException ex) {
            Logger.getLogger(MiddlewareFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    private AgencyReply getCurrentReply(AgencyRequest request) {
        for (int i = 0; i < listModel.size(); i++) {
            BookingAgencyListLine rr = listModel.get(i);
            if (rr.getRequest() == request) {
                return rr.getReply();
            }
        }
        return null;
    }

    public void add(AgencyRequest request) {
        listModel.addElement(new BookingAgencyListLine(request, null));
        jList1.repaint();
    }

    public void add(AgencyRequest request, AgencyReply reply) {
        for (int i = 0; i < listModel.size(); i++) {
            BookingAgencyListLine rr = listModel.get(i);
            if (rr.getRequest() == request) {
                rr.setReply(reply);
                jList1.repaint();
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jList1.setModel(listModel);
        jList1.setToolTipText("");
        jList1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jScrollPane2.setViewportView(jList1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 734, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 15, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList<BookingAgencyListLine> jList1;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables
    private static final long serialVersionUID = 7526472295622776147L;
}
