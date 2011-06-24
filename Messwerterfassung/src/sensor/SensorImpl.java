package sensor;

import hawmetering.HAWMeteringWebservice;
import hawmetering.HAWMeteringWebserviceService;

import java.net.URL;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.jws.WebService;
import javax.xml.namespace.QName;
import javax.xml.ws.Holder;

@WebService(wsdlLocation = "Sensor.wsdl", serviceName = "SensorService", portName = "SensorSOAP", targetNamespace = "http://sensor/", name = "Sensor", endpointInterface = "sensor.Sensor")
public class SensorImpl implements Sensor {

    class ConnectionException extends Exception{
        private static final long serialVersionUID = -5728422902093458359L;
        
    }
    //Config
    int maxTimeout = 1200;
    int waitingtime = 500;
    
    // Status vars
    boolean inconsistent = false;
    boolean running = true;
    boolean iscoordinator = false;
    boolean needElection = false;
    boolean needDirections = true;
    int value = 50;
    Directions activeDirections = new Directions();
    
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
    
    Logger l;
    long timeout;
    Random rnd = new Random();
    LogManager lm;

    /**
     * @param meter URI to HAWMeter
     * @param name Name of this Sensor
     * @param bootstrap Some already existing sensor
     * @param directions The scales we want to write on
     */
    SensorImpl(String meter, String name, String bootstrap, Directions directions) {
        l = Logger.getLogger(SensorImpl.class.getName());
        ConsoleHandler c = new ConsoleHandler();
        l.setLevel(Level.ALL);
        c.setLevel(Level.ALL);
        l.addHandler(c);
        
        bootstrapSensor = bootstrap;

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
            l.log(Level.FINE, "new sensor on list");
            newsensors.add(sensor);
            return true;
        }
        return false;

    }

    @Override
    public boolean election() {
        l.log(Level.FINE, "election started");
        needElection = false;
        for(SensorObj s : sensorlist.getList()){
            if(s.getId()>myObj.getId()){
                try{
                    toSensor(s).election();
                    return false;
                }catch(Exception e){
                    ;
                }
            }
        }
        l.log(Level.FINE, "I won ("+myObj.getId()+")");
        // I won! set coordinator
        iscoordinator = true;
        coordinator = myObj;
        for(SensorObj s : sensorlist.getList()){
            if(!s.getLocation().equals(myObj.getLocation())){
                try {
                    toSensor(s).setCoordinator(myObj);
                } catch (ConnectionException e) {
                    ;
                }
            }
        }
        return true;
    }

    @Override
    public SensorObj getCoordinator() {
        return coordinator;
    }

    @Override
    public void getDatabase(Holder<SensorList> list, Holder<Long> version) {
        needDirections = true;
        list.value = sensorlist;
        version.value = sensorlistversion;
    }

    @Override
    public String getDisplay() {
        return meterURI;
    }

    @Override
    public boolean ping(long version) {
        if(version != sensorlistversion)
            inconsistent = true;
        l.log(Level.FINER, "Ping received! ("+myObj.getId()+")");
        timeout = System.currentTimeMillis() + maxTimeout;
        value += rnd.nextInt()%5;
        value %= 100;
        value = (value < 0)? (value*-1) : value;
        try{
        if(activeDirections.isNE()){
            meterNE.setValue(value);
            meterNE.setTitle(Long.toHexString(myObj.getId()));
        }
            
        if(activeDirections.isSE()){
            meterSE.setValue(value);
            meterSE.setTitle(Long.toHexString(myObj.getId()));
        }
            
        if(activeDirections.isSW()){
            meterSW.setValue(value);
            meterSW.setTitle(Long.toHexString(myObj.getId()));
        }
            
        if(activeDirections.isNW()){
            meterNW.setValue(value);
            meterNW.setTitle(Long.toHexString(myObj.getId()));
        }
            
        }catch(Exception e){
            running = false;
            return false;
        }
        return true;
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
            l.log(Level.FINE, "removing sensor by usr cmd ("+sensor.getId()+")");
            setDefunct(sensor);
            return true;
        }
        return false;
    }

    @Override
    public boolean setCoordinator(SensorObj coordinator) {
        iscoordinator = false;
        this.coordinator = coordinator;
        l.log(Level.FINE, myObj.id+" got "+coordinator.id+" as a new coordinator");
        return true;

    }

    @Override
    public boolean setDisplay(Directions direction) {
        activeDirections = direction;
        return false;
    }

    private synchronized void refreshDatabase() {
        l.log(Level.INFO, myObj.id+" refreshing database");
        Holder<SensorList> list = new Holder<SensorList>();
        Holder<Long> version = new Holder<Long>();
        try {
            toSensor(coordinator).getDatabase(list, version);
            sensorlist = list.value;
            sensorlistversion = version.value;
            inconsistent = false;
        } catch (Exception e) {
            needElection = true;
        }
    }

    // Webservice functions

    private synchronized void updateDatabase() {
        l.log(Level.FINE, myObj.id+" updating database");
        for(Iterator<SensorObj> i = newsensors.iterator();i.hasNext();){
            SensorObj s = i.next();
            i.remove();
            for (SensorObj sensor : sensorlist.getList()) {
                if(!sensor.getLocation().equals(myObj.getLocation())){
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
        l.log(Level.FINE, sensor.id+" added to defunct sensors");
        for(SensorObj s : sensorlist.getList()){
            if(s.getLocation().equals(sensor.getLocation()))
                    defunctsensors.add(s);
        }
    }
    
    private synchronized void cleanDatabase(){
        l.log(Level.FINE, "cleaning database");
        List<SensorObj> temp = new LinkedList<SensorObj>();
        SensorObj toremove;
        for(Iterator<SensorObj> i = defunctsensors.iterator();i.hasNext();){
            // get a defect Sensor
            toremove = i.next();
            i.remove();
            // remove sensor from every reachable database
            for (SensorObj sensor : temp) {
                if(!sensor.getLocation().equals(myObj.getLocation())){
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
    
    private void updateDirections(){
        l.log(Level.FINE, "updating directions");
        Directions inuse = new Directions();
        Directions active;
        Directions wanted;
        for(SensorObj s : sensorlist.getList()){
            wanted = s.getDirection();
            active = new Directions();
            //break if every meter is in use 
            if(inuse.ne&&inuse.nw&&inuse.se&&inuse.sw)
                break;
                active.setNE(wanted.ne&&!inuse.ne);           
                active.setSE(wanted.se&&!inuse.se);
                active.setSW(wanted.sw&&!inuse.sw);
                active.setNW(wanted.nw&&!inuse.nw);
            try {
                toSensor(s).setDisplay(active);
                inuse.ne = inuse.ne || active.ne;
                inuse.se = inuse.se || active.se;
                inuse.sw = inuse.sw || active.sw;
                inuse.nw = inuse.nw || active.nw;
            } catch (ConnectionException e) {
                ;
            }
            
            
        }
    }

    void run() {

        if (iscoordinator) {
            // starting as coordinator
            coordinator = myObj;
            sensorlist.getList().add(myObj);
        } else {
            // starting from bootstrap
            SensorObj boot = new SensorObj();
            boot.setLocation(bootstrapSensor);

            try {
                coordinator = toSensor(boot).getCoordinator();
                meterURI = toSensor(boot).getDisplay();

            } catch (ConnectionException e) {
                e.printStackTrace();
                return;
            }

        }

        try {
            meterNE = new HAWMeteringWebserviceService(new URL(meterURI + "hawmetering/no?wsdl"), new QName(
                    "http://hawmetering/", "HAWMeteringWebserviceService")).getHAWMeteringWebservicePort();
            meterSE = new HAWMeteringWebserviceService(new URL(meterURI + "hawmetering/so?wsdl"), new QName(
                    "http://hawmetering/", "HAWMeteringWebserviceService")).getHAWMeteringWebservicePort();
            meterSW = new HAWMeteringWebserviceService(new URL(meterURI + "hawmetering/sw?wsdl"), new QName(
                    "http://hawmetering/", "HAWMeteringWebserviceService")).getHAWMeteringWebservicePort();
            meterNW = new HAWMeteringWebserviceService(new URL(meterURI + "hawmetering/nw?wsdl"), new QName(
                    "http://hawmetering/", "HAWMeteringWebserviceService")).getHAWMeteringWebservicePort();
        } catch (Exception e) {
            e.printStackTrace();
            l.log(Level.SEVERE, "No meter reachable");
            return;
        }
        timeout = System.currentTimeMillis() + maxTimeout;
        
        if(!iscoordinator){
            // now add us and get started
            try {
                toSensor(coordinator).addSensor(myObj);
            } catch (ConnectionException e1) {
                return;
            }
            refreshDatabase();
        }
        while (running) {
            if (iscoordinator) {
                
                if(needDirections){
                    updateDirections();
                    needDirections = false;
                }
                
                for (SensorObj sensor : sensorlist.getList()) {
                    try {
                        toSensor(sensor).ping(sensorlistversion);
                    } catch (ConnectionException e) {
                        ;
                    }
                }
                
                if(!defunctsensors.isEmpty()){
                    cleanDatabase();
                    needDirections = true;
                }
                
                if(!newsensors.isEmpty()){
                    updateDatabase();
                    needDirections = true;
                }
                
                try {
                    Thread.sleep(waitingtime);
                } catch (InterruptedException e) {
                    ;
                }
            } else {
                if(inconsistent)
                   refreshDatabase(); 
                if(timeout < System.currentTimeMillis() || needElection){
                    election();
                }
            }
            // common

        }
        
        // try graceful shutdown
        try {
            toSensor(coordinator).removeSensor(myObj);
        } catch (ConnectionException e) {
            return;
        }
    }

}
