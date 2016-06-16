package main;

import agency.BookingAgencyFrame;
import client.BookingClientFrame;
import java.awt.EventQueue;
import middleware.MiddlewareFrame;

public class StartAllAgencies {

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                MiddlewareFrame frame = new MiddlewareFrame();
                frame.setLocation(650, 300);
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        EventQueue.invokeLater(() -> {
            try {
                BookingAgencyFrame frame = new BookingAgencyFrame("Book Fast",
                        "bookFastQueue");
                frame.setLocation(1500, 70);
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        EventQueue.invokeLater(() -> {
            try {
                BookingAgencyFrame frame = new BookingAgencyFrame("Book Cheap",
                        "bookCheapQueue");
                frame.setLocation(1500, 370);
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        EventQueue.invokeLater(() -> {
            try {
                BookingAgencyFrame frame = new BookingAgencyFrame("Book Good Service",
                        "bookGoodServiceQueue");
                frame.setLocation(1500, 670);
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        EventQueue.invokeLater(() -> {
            try {
                BookingClientFrame frame = new BookingClientFrame();
                frame.setLocation(100, 250);
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
