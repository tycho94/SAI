package main;

import agency.BookingAgencyFrame;
import client.BookingClientFrame;
import java.awt.EventQueue;

public class StartAllAgencies {

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                BookingAgencyFrame frame = new BookingAgencyFrame("Book Fast",
                        "bookFastQueue");
                frame.setLocation(1300, 20);
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        EventQueue.invokeLater(() -> {
            try {
                BookingAgencyFrame frame = new BookingAgencyFrame("Book Cheap",
                        "bookCheapQueue");
                frame.setLocation(1300, 320);
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        EventQueue.invokeLater(() -> {
            try {
                BookingAgencyFrame frame = new BookingAgencyFrame("Book Good Service",
                        "bookGoodServiceQueue");
                frame.setLocation(1300, 620);
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        /* Create and display the form */
        EventQueue.invokeLater(() -> {
            new BookingClientFrame().setVisible(true);
        });
    }
}
