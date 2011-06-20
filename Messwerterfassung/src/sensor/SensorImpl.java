package sensor;

import java.net.URL;

import javax.jws.WebService;
import javax.xml.namespace.QName;
import javax.xml.ws.Endpoint;
import javax.xml.ws.Holder;

@WebService(wsdlLocation = "Sensor.wsdl", serviceName = "Sensor", portName = "SensorSOAP", targetNamespace = "http://sensor/", name = "Sensor")
public class SensorImpl implements Sensor {

    public long id;
    SensorObj myObj = new SensorObj();
    boolean iscoordinator = false;
    boolean running = true;
    String existing;
    SensorList sensorlist = new SensorList();
    long sensorlistversion = 0;
    SensorObj coordinator;
    Endpoint endpoint;

    /**
     * @param port Port to run on
     * @param name Name of this Sensor
     * @param existingsensor Some already existing sensor
     * @param directions The scales we want to write on
     */
    SensorImpl(String name, String existingsensor, Directions directions) {

        existing = existingsensor;
        myObj.setLocation(name);
        myObj.setId(id);
        myObj.setDirection(directions);
        if (existingsensor == null) {
            iscoordinator = true;
        }
        run();
    }

    @Override
    public boolean activateDisplay(int direction) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean addDatabase(SensorObj sensor, long version) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean addSensor(SensorObj sensor) {
        sensorlist.getList().add(sensor);
        return true;
    }

    @Override
    public boolean election() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public SensorObj getCoordinator() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void getDatabase(Holder<SensorList> list, Holder<Long> version) {
        // TODO Auto-generated method stub

    }

    @Override
    public String getDisplay() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean ping() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean removeDatabase(SensorObj sensor, long version) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean removeSensor(SensorObj sensor) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean setCoordinator(SensorObj coordinator) {
        this.coordinator = coordinator;

        return true;

    }

    private void removeFromDatabase(SensorObj victim) {
        sensorlist.getList().remove(victim);
        sensorlistversion += 1;
        for (SensorObj sensor : sensorlist.getList()) {
            try {
                toSensor(sensor).removeDatabase(victim, sensorlistversion - 1);
            } catch (Exception e) {
                removeFromDatabase(sensor);
            }
        }
    }

    private void run() {
        // startup
        toSensor(coordinator).addSensor(myObj);

        while (running) {
            if (iscoordinator) {
                for (SensorObj sensor : sensorlist.getList()) {
                    toSensor(sensor).ping();
                }
            } else {
                // check for ping
            }
            // common

        }
        // shutdown
        toSensor(coordinator).removeSensor(myObj);
        endpoint.stop();
    }

    // Webservice functions

    private Sensor toSensor(SensorObj sensor) {
        Sensor ref = null;
        try {
            ref = new Sensor_Service(new URL(sensor.getLocation() + "sensor?wsdl"), new QName("http://sensor/",
                    "sensor")).getSensorSOAP();
        } catch (Exception e) {
            removeFromDatabase(sensor);
        }
        return ref;
    }

    void setEndpoint(Endpoint endpoint) {
        this.endpoint = endpoint;
    }

    // Koordinator functions

}
