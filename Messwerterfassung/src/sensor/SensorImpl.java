package sensor;

import hawmetering.HAWMeteringWebservice;
import hawmetering.HAWMeteringWebserviceService;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

import javax.jws.WebService;
import javax.xml.namespace.QName;
import javax.xml.ws.Holder;

@WebService(wsdlLocation = "Sensor.wsdl", serviceName = "SensorService", portName = "SensorSOAP", targetNamespace = "http://sensor/", name = "Sensor", endpointInterface = "sensor.Sensor")
public class SensorImpl implements Sensor {

    public long id;
    SensorObj myObj = new SensorObj();
    boolean iscoordinator = false;
    boolean running = true;
    String bootstrapSensor;
    SensorList sensorlist = new SensorList();
    long sensorlistversion = 0;
    SensorObj coordinator;
    Directions activeDirections = new Directions();
    SensorLog l;
    String meterURI;
    HAWMeteringWebservice meter;

    /**
     * @param port Port to run on
     * @param name Name of this Sensor
     * @param bootstrapSensor Some already existing sensor
     * @param directions The scales we want to write on
     */
    SensorImpl(String meter, String name, String bootstrap, Directions directions) {
        l = new SensorLog("sensor.SensorImpl", null);
        bootstrapSensor = bootstrap;

        Random rnd = new Random();
        id = rnd.nextLong();

        l.log(Level.FINE, "Starting with ID " + id);

        myObj.setLocation(name);
        myObj.setId(id);
        myObj.setDirection(directions);

        meterURI = meter;
        if (bootstrapSensor == null) {
            iscoordinator = true;
        }

    }

    @Override
    public synchronized boolean addDatabase(SensorObj sensor, long version) {
        if (sensorlistversion == version) {
            l.log(Level.FINE, "Add sensor. DBVersion(" + sensorlistversion + ")");
            sensorlist.getList().add(sensor);
            sensorlistversion++;
        } else {
            l.log(Level.INFO, "inconsistent database");
            refreshDatabase();
        }
        return false;
    }

    @Override
    public boolean addSensor(SensorObj sensor) {
        if (iscoordinator) {
            addToDatabase(sensor);
            return true;
        }
        return false;

    }

    @Override
    public boolean election() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public SensorObj getCoordinator() {
        return coordinator;
    }

    @Override
    public void getDatabase(Holder<SensorList> list, Holder<Long> version) {
        list.value = sensorlist;
        version.value = sensorlistversion;
    }

    @Override
    public String getDisplay() {
        return meterURI;
    }

    @Override
    public boolean ping() {
        // TODO
        // if(activeDirections.isNE())
        // / meter.

        return false;
    }

    @Override
    public synchronized boolean removeDatabase(SensorObj sensor, long version) {
        if (sensorlistversion == version) {
            l.log(Level.FINE, "Removing sensor. DBVersion(" + sensorlistversion + ")");
            
            List<SensorObj> list = sensorlist.getList();           
            while(list.remove(sensor));                      
            sensorlistversion++;
        } else {
            l.log(Level.INFO, "inconsistent database");
            refreshDatabase();
        }
        sensorlistversion++;
        return false;
    }

    @Override
    public boolean removeSensor(SensorObj sensor) {
        if (iscoordinator) {
            removeFromDatabase(sensor);
            return true;
        }
        return false;
    }

    @Override
    public boolean setCoordinator(SensorObj coordinator) {
        this.coordinator = coordinator;

        return true;

    }

    @Override
    public boolean setDisplay(Directions direction) {
        activeDirections = direction;
        return false;
    }

    private void addToDatabase(SensorObj customer) {
        List<SensorObj> temp = new LinkedList<SensorObj>();
        temp.addAll(sensorlist.getList());
        for (SensorObj sensor : temp) {
            toSensor(sensor).addDatabase(customer, sensorlistversion);
        }
    }

    private void refreshDatabase() {
        l.log(Level.INFO, "Refreshing database");
        Holder<SensorList> list = new Holder<SensorList>();
        Holder<Long> version = new Holder<Long>();
        toSensor(coordinator).getDatabase(list, version);
        sensorlist = list.value;
        sensorlistversion = version.value;
    }

    // Webservice functions

    private void removeFromDatabase(SensorObj victim) {
        List<SensorObj> temp = new LinkedList<SensorObj>();
        temp.addAll(sensorlist.getList());
        for (SensorObj sensor : temp) {
            try {
                toSensor(sensor).removeDatabase(victim, sensorlistversion);
            } catch (Exception e) {
                removeFromDatabase(sensor);
            }
        }
    }

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

    void run() {

        String m;
        if (iscoordinator) {
            // starting as coordinator
            coordinator = myObj;
            m = meterURI;
            sensorlist.getList().add(myObj);
        } else {
            // starting from bootstrap
            SensorObj boot = new SensorObj();
            boot.setLocation(bootstrapSensor);

            coordinator = toSensor(boot).getCoordinator();
            m = toSensor(boot).getDisplay();
            toSensor(coordinator).addSensor(myObj);
            refreshDatabase();
        }

        try {
            meter = new HAWMeteringWebserviceService(new URL(m + "hawmetering/nw?wsdl"), new QName(
                    "http://hawmetering/", "HAWMeteringWebserviceService")).getHAWMeteringWebservicePort();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            l.log(Level.SEVERE, "No meter reachable");
            return;
        }

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

    // Koordinator functions

}
