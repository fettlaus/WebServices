package vsp3.sensor;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.ws.Endpoint;

public class SensorMain {

    public static void main(String[] args) {
        String sensorName = "Sensorx";
        String existingSensor = null;
        String meter = null;
        Directions directions = new Directions();
        if (args.length < 4) {
            System.err.println("must have at least 4 arguments");
            return;
        }

        try {
            sensorName = args[0];
            new URL(new URL("http://localhost:9898/"), args[0]).toString();
        } catch (MalformedURLException e1) {
            System.err.println("malformed location");
            return;
        }
        if (args[1].equalsIgnoreCase("-k")) {
            meter = args[2];
            try {
                new URL(new URL("http://localhost:9898/"), args[2]);
            } catch (MalformedURLException e) {
                System.err.println("malformed HAWMeter location");
                return;
            }
        } else if (args[1].equalsIgnoreCase("-s")) {
            existingSensor = args[2];
            try {
                new URL(new URL("http://localhost:9999/"), args[2]);
            } catch (MalformedURLException e) {
                System.err.println("malformed Bootstrap location");
                return;
            }
        }

        for (int i = 3; i < args.length; i++) {
            if (args[i].equalsIgnoreCase("NE")) {
                directions.setNE(true);
            }
            if (args[i].equalsIgnoreCase("SE")) {
                directions.setSE(true);
            }
            if (args[i].equalsIgnoreCase("SW")) {
                directions.setSW(true);
            }
            if (args[i].equalsIgnoreCase("NW")) {
                directions.setNW(true);
            }
        }

        SensorImpl sensor = new SensorImpl(meter, sensorName, existingSensor, directions);
        Endpoint endpoint = Endpoint.create(sensor);
        endpoint.publish(sensorName + "sensor");
        sensor.run();
        endpoint.stop();

    }

}
