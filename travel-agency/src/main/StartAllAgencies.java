package main;

import adapter.GoogleMapsAdapter;
import agency.BookingAgencyFrame;
import client.BookingClientFrame;
import java.awt.EventQueue;
import middleware.MiddlewareFrame;


//Start every form and the maps adapter

public class StartAllAgencies {

    public static void main(String[] args) {
        GoogleMapsAdapter adapter = new GoogleMapsAdapter();

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
                BookingAgencyFrame frame = new BookingAgencyFrame("Book Cheap",
                        "Cheap");
                frame.setLocation(1500, 70);
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        EventQueue.invokeLater(() -> {
            try {
                BookingAgencyFrame frame = new BookingAgencyFrame("Book Good",
                        "Good");
                frame.setLocation(1500, 370);
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        EventQueue.invokeLater(() -> {
            try {
                BookingAgencyFrame frame = new BookingAgencyFrame("Book Fast",
                        "Fast");
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

        // GoogleMapsAdapter adapter = new GoogleMapsAdapter();
    }
}
