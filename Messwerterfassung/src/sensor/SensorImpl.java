package sensor;

import hawmetering.HAWMeteringWebservice;
import hawmetering.HAWMeteringWebserviceService;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

import javax.jws.WebService;
import javax.xml.namespace.QName;
import javax.xml.ws.Holder;

@WebService(wsdlLocation = "Sensor.wsdl", serviceName = "SensorService", portName = "SensorSOAP", targetNamespace = "http://sensor/", name = "Sensor", endpointInterface = "sensor.Sensor")
public class SensorImpl implements Sensor {

    class ConnectionException extends Exception{
        private static final long serialVersionUID = -5728422902093458359L;
        
    }
    
    // Status vars
    boolean inconsistent = true;
    boolean running = true;
    boolean iscoordinator = false;
    boolean needElection = false;
    
    //my Data
    public long id;
    SensorObj myObj = new SensorObj();
    
    //DB related
    SensorList sensorlist = new SensorList();
    LinkedHashSet<SensorObj> defunctsensors = new LinkedHashSet<SensorObj>();
    LinkedHashSet<SensorObj> newsensors = new LinkedHashSet<SensorObj>();
    long sensorlistversion = 0;
    
    //Environment
    String bootstrapSensor;
    SensorObj coordinator;
    String meterURI;
    HAWMeteringWebservice meterNE;
    HAWMeteringWebservice meterSE;
    HAWMeteringWebservice meterSW;
    HAWMeteringWebservice meterNW;
    int value = 0;
    
    Directions activeDirections = new Directions();
    SensorLog l;

    /**
     * @param meter URI to HAWMeter
     * @param name Name of this Sensor
     * @param bootstrap Some already existing sensor
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
        // client-only function
        if (!iscoordinator) {
            if (sensorlistversion == version) {
                l.log(Level.FINE, "Add sensor. DBVersion(" + sensorlistversion + ")");
                sensorlist.getList().add(sensor);
                sensorlistversion++;
            } else {
                l.log(Level.INFO, "inconsistent database");
                // refresh DB on next possibility
                inconsistent = true;
            }
        }
        return false;
    }

    @Override
    public boolean addSensor(SensorObj sensor) {
        if (iscoordinator) {
            newsensors.add(sensor);
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
        Random rnd = new Random();
        value += rnd.nextInt()%10;
        value = (value < 0)? (value*-1) : value;
        if(activeDirections.isNE())
            meterNE.setValue(value);
        if(activeDirections.isSE())
            meterSE.setValue(value);
        if(activeDirections.isSW())
            meterSW.setValue(value);
        if(activeDirections.isNW())
            meterNW.setValue(value);

        // TODO needs failsafe
        return false;
    }

    @Override
    public synchronized boolean removeDatabase(SensorObj sensor, long version) {
        if (!iscoordinator) {
            if (sensorlistversion == version) {
                l.log(Level.FINE, "Removing sensor. DBVersion(" + sensorlistversion + ")");

                List<SensorObj> list = sensorlist.getList();
                while (list.remove(sensor)) {
                    ;
                }
                sensorlistversion++;
                return true;
            }
            l.log(Level.INFO, "inconsistent database");
            //Refresh DB on next possibility
            inconsistent = true;
        }
        return false;
    }

    @Override
    public boolean removeSensor(SensorObj sensor) {
        if (iscoordinator) {
            setDefunct(sensor);
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

    private synchronized void refreshDatabase() {
        l.log(Level.INFO, "Refreshing database");
        Holder<SensorList> list = new Holder<SensorList>();
        Holder<Long> version = new Holder<Long>();
        try {
            toSensor(coordinator).getDatabase(list, version);
            sensorlist = list.value;
            sensorlistversion = version.value;
        } catch (Exception e) {
            needElection = true;
        }
    }

    // Webservice functions

    private synchronized void addToDatabase() {
        for(Iterator<SensorObj> i = newsensors.iterator();i.hasNext();){
            SensorObj s = i.next();
            i.remove();
            for (SensorObj sensor : sensorlist.getList()) {
                if(sensor.getLocation() != myObj.getLocation()){
                    try {
                        toSensor(sensor).addDatabase(s, sensorlistversion);
                    } catch (ConnectionException e) {
                        ;
                    }
                }
            }
            sensorlistversion++;
            sensorlist.getList().add(s);
        }
    }

    private Sensor toSensor(SensorObj sensor) throws ConnectionException{
        Sensor ref = null;
        try {
            ref = new SensorService(new URL(sensor.getLocation() + "sensor?wsdl"), new QName("http://sensor/",
                    "SensorService")).getSensorSOAP();
        } catch (Exception e) {
            setDefunct(sensor);
            throw new ConnectionException();
        }
        return ref;
    }
    
    private void setDefunct(SensorObj sensor){
        for(SensorObj s : sensorlist.getList()){
            if(s.getLocation().equals(sensor.getLocation()))
                    defunctsensors.add(s);
        }
    }
    
    private synchronized void cleanDatabase(){
        List<SensorObj> temp = new LinkedList<SensorObj>();
        SensorObj toremove;
        for(Iterator<SensorObj> i = defunctsensors.iterator();i.hasNext();){
            // get a defect Sensor
            toremove = i.next();
            i.remove();
            // remove sensor from every reachable database
            for (SensorObj sensor : temp) {
                // if it is not us
                if(sensor.getLocation()!=myObj.getLocation()){
                    try {
                        toSensor(sensor).removeDatabase(toremove, sensorlistversion);
                    } catch (Exception e) {
                        ;
                    }
                }               
            }
            sensorlistversion++;
            while(sensorlist.getList().remove(toremove));
        }
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

            try {
                coordinator = toSensor(boot).getCoordinator();
                m = toSensor(boot).getDisplay();
                // add us first, to get updates
                toSensor(coordinator).addSensor(myObj);
            } catch (ConnectionException e) {
                e.printStackTrace();
                return;
            }
            //refreshDatabase();
        }

        try {
            meterNE = new HAWMeteringWebserviceService(new URL(m + "hawmetering/no?wsdl"), new QName(
                    "http://hawmetering/", "HAWMeteringWebserviceService")).getHAWMeteringWebservicePort();
            meterSE = new HAWMeteringWebserviceService(new URL(m + "hawmetering/so?wsdl"), new QName(
                    "http://hawmetering/", "HAWMeteringWebserviceService")).getHAWMeteringWebservicePort();
            meterSW = new HAWMeteringWebserviceService(new URL(m + "hawmetering/sw?wsdl"), new QName(
                    "http://hawmetering/", "HAWMeteringWebserviceService")).getHAWMeteringWebservicePort();
            meterNW = new HAWMeteringWebserviceService(new URL(m + "hawmetering/nw?wsdl"), new QName(
                    "http://hawmetering/", "HAWMeteringWebserviceService")).getHAWMeteringWebservicePort();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            l.log(Level.SEVERE, "No meter reachable");
            return;
        }

        while (running) {
            if (iscoordinator) {
                for (SensorObj sensor : sensorlist.getList()) {
                    try {
                        toSensor(sensor).ping();
                    } catch (ConnectionException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            } else {
                // check for ping
            }
            // common

        }
        // shutdown
        try {
            toSensor(coordinator).removeSensor(myObj);
        } catch (ConnectionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // Koordinator functions

}
