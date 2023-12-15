package Threads;

import Applikation.ControllerÜbersicht;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static Applikation.Login.publicusername;

public class BalanceThread implements Runnable {
    public static Logger log = LogManager.getLogger(ControllerÜbersicht.class);
    @Override public void run() {
        if (publicusername != null) {
            Thread t1 = new Thread(task1);
            t1.start();
        }

    }

    static Runnable task1 = () -> {
        log.info("Task1 is running");
        String url = "jdbc:postgresql://foo.mi.hdm-stuttgart.de/js486";
        String pass = "(JJS)2003ab";
        String user = "js486";

        try {
            Connection con = DriverManager.getConnection(url, user, pass);

            try {
                String sql = "SELECT bankbalance FROM konto" + publicusername + " ORDER BY edate DESC, id DESC";

                PreparedStatement stmt = con.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();

                List<Double> newList = new ArrayList<>();

                while(rs.next()) {
                    double balance = rs.getDouble("bankbalance");
                    newList.add(balance);
                }


                double sum = newList.stream().reduce(0.0, Double::sum);
                double average = sum / newList.size();

                DecimalFormat decimalFormat = new DecimalFormat("#.##");
                String roundedAverage = decimalFormat.format(average);

                log.info("The average account balance is " + roundedAverage);
            } catch (SQLException e) {
                log.warn("Couldn't get average account balance");
            }


        } catch (SQLException e) {
            log.error("Couldn't connect to database");
        }
    };


}
