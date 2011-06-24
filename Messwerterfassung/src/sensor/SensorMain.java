package sensor;

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
        sensorName = args[0];
        if (args[1].equalsIgnoreCase("-k")) {
            meter = args[2];
        } else if (args[1].equalsIgnoreCase("-s")) {
            existingSensor = args[2];
        }

        for (int i = 3; i < args.length; i++) {
            if (args[i].equalsIgnoreCase("NE"))
                directions.setNE(true);
            if (args[i].equalsIgnoreCase("SE"))
                directions.setSE(true);
            if (args[i].equalsIgnoreCase("SW"))
                directions.setSW(true);
            if (args[i].equalsIgnoreCase("NW"))
                directions.setNW(true);
        }

        // System.out.println(sensorName + " " + existingSensor);
        // System.out.println(directions.isNE() + " " + directions.isSE() + " "
        // + directions.isSW() + " " + directions.isSE());

        SensorImpl sensor = new SensorImpl(meter, sensorName, existingSensor, directions);
        Endpoint endpoint = Endpoint.create(sensor);
        endpoint.publish(sensorName + "sensor");
        sensor.run();
        endpoint.stop();

    }

}
