package Threads;

import Applikation.ControllerÜbersicht;
import Applikation.DatenbankConnector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

        DatenbankConnector dc = new DatenbankConnector();
        try (Connection con = dc.getConnection()) {

            try {
                String sql = "SELECT bankbalance FROM konto" + publicusername + " ORDER BY edate DESC, id DESC";

                PreparedStatement stmt = con.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();

                List<Double> newList = new ArrayList<>();

                while(rs.next()) {
                    double balance = rs.getDouble("bankbalance");
                    newList.add(balance);
                }


                double sum = newList.parallelStream().reduce(0.0, Double::sum);
                Optional<Double> maxBalance = newList.parallelStream().max(Double::compareTo);
                double average = sum / newList.size();

                DecimalFormat decimalFormat = new DecimalFormat("#.##");
                String roundedAverage = decimalFormat.format(average);


                log.info("The average account balance is " + roundedAverage);
                if (maxBalance.isPresent()) {
                    log.info("The overall highest balance was " + maxBalance.get());
                }

            } catch (SQLException e) {
                log.warn("Couldn't get average account balance");
            }


        } catch (SQLException e) {
            log.error("Couldn't connect to Database", e);
        }
    };
}
