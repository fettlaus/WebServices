package sensor;

import java.net.MalformedURLException;
import java.net.URL;
import javax.jws.WebService;
import javax.xml.namespace.QName;
import javax.xml.ws.Holder;

@WebService(wsdlLocation="Sensor.wsdl",serviceName="Sensor",portName="SensorSOAP",targetNamespace="http://sensor/",name="Sensor")
public class SensorImpl implements Sensor {

    public long id;
    String name;
    String existing;
    SensorList sensorlist = new SensorList();
    Sensor coordinator;
    Directions directions;
    
    /**
     * @param port Port to run on
     * @param name Name of this Sensor
     * @param existingsensor Some already existing sensor
     * @param directions The scales we want to write on
     */
    SensorImpl(String name, String existingsensor, Directions directions) {
        this.name = name;
        this.existing = existingsensor;
        this.directions = directions;
    }
    
    private Sensor toSensor(SensorObj sensor) throws MalformedURLException{
        return new Sensor_Service(new URL(sensor.getLocation()+"sensor?wsdl"),new QName("http://sensor/", "sensor")).getSensorSOAP();
    }
    
    @Override
    public boolean ping() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public SensorObj getCoordinator() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getDisplay() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean activateDisplay(int direction) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void getDatabase(Holder<SensorList> list, Holder<Long> version) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean addDatabase(SensorObj sensor, long version) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean removeDatabase(SensorObj sensor, long version) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean election() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean setCoordinator(SensorObj coordinator){
              try {
                this.coordinator = toSensor(coordinator);
            } catch (MalformedURLException e) {                
                e.printStackTrace();
                return false;
            }
            return true;

        
    }

    // Webservice functions

    
    @Override
    public boolean addSensor(SensorObj sensor){
        sensorlist.getList().add(sensor);
        return true;
    }

    @Override
    public boolean removeSensor(SensorObj sensor) {
        // TODO Auto-generated method stub
        return false;
    }

    
    // Koordinator functions
  
    
}
