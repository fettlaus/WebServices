package sensor;

import java.net.URL;
import java.util.List;

import javax.jws.WebService;
import javax.xml.namespace.QName;
import javax.xml.ws.Holder;

@WebService(wsdlLocation = "Sensor.wsdl", serviceName = "SensorService", portName = "SensorSOAP", targetNamespace = "http://sensor/", name = "Sensor")
public class SensorImpl implements Sensor {

    public long id;
    SensorObj myObj = new SensorObj();
    boolean iscoordinator = false;
    boolean running = true;
    String bootstrapSensor;
    SensorList sensorlist = new SensorList();
    long sensorlistversion = 0;
    SensorObj coordinator;

    /**
     * @param port Port to run on
     * @param name Name of this Sensor
     * @param bootstrapSensor Some already existing sensor
     * @param directions The scales we want to write on
     */
    SensorImpl(String name, String bootstrap, Directions directions) {

        this.bootstrapSensor = bootstrap;
        myObj.setLocation(name);
        myObj.setId(id);
        myObj.setDirection(directions);
        if (bootstrapSensor == null) {
            iscoordinator = true;
        }
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
        List<SensorObj> sl = sensorlist.getList();
        sl.add(sensor);
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
        list.value = sensorlist;
        version.value = sensorlistversion;
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

    void run() {
        //bootstrap
        SensorObj boot = new SensorObj();
        boot.setLocation(bootstrapSensor);
 
        // startup
        if(iscoordinator)
            coordinator = myObj;
        else
            coordinator = toSensor(boot).getCoordinator();
        toSensor(coordinator).addSensor(myObj);
        
        Holder<SensorList> l = new Holder<SensorList>();
        Holder<Long> v = new Holder<Long>();
        toSensor(coordinator).getDatabase(l, v);
        sensorlist = l.value;
        sensorlistversion = v.value;

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
    }

    // Webservice functions

    private Sensor toSensor(SensorObj sensor) {
        Sensor ref = null;
        try {
            ref = new SensorService(new URL(sensor.getLocation() + "sensor?wsdl"), new QName("http://sensor/",
                    "SensorService")).getSensorSOAP();
        } catch (Exception e) {
            removeFromDatabase(sensor);
        }
        return ref;
    }

    // Koordinator functions

}
