package sensor;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import javax.jws.WebService;
import javax.xml.namespace.QName;

@WebService(wsdlLocation="Sensor.wsdl")
public class SensorImpl implements Sensor {

    public long id;
    String name;
    String existing;
    int[] directions;
    SensorList sensorlist;
    // Liste ueber http://ip.address:port/
    ArrayList<String> sensorstrings;
    Sensor coordinator;
    
    /**
     * @param port Port to run on
     * @param name Name of this Sensor
     * @param existingsensor Some already existing sensor
     * @param directions The scales we want to write on
     */
    SensorImpl(String name, String existingsensor, int[] directions) {
        this.name = name;
        this.existing = existingsensor;
        this.directions = directions;
        sensorstrings.add("name");
    }
    SensorImpl(){
        
    }
    
    private Sensor toSensor(String sensor) throws MalformedURLException{
        return new Sensor_Service(new URL(sensor+"sensor?wsdl"),new QName(sensor,"sensor")).getSensorSOAP();
    }
    
    public String[] getSensors(){
        return sensorstrings.toArray(new String[]{});
    }
    
  //public SensorService[] getSensorServicelist(){
   //     return sensorservicelist.toArray(new SensorService[]{});
   // }
    
    // election algorithm 
    
    public boolean setCoordinator(String coordinator){
            try {
                this.coordinator = toSensor(coordinator);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return false;
            }
        
        return true;
        
    }
    public boolean election(){
        return true;
    }

    // Webservice functions
    
    public long getID(){
        return id;
    }
    
    @Override
    public boolean addSensor(SensorObj sensor){
        sensorlist.getList().add(sensor);
        return true;
    }
    @Override
    public boolean election(long requestingID) {
        // TODO Auto-generated method stub
        return false;
    }
    @Override
    public SensorList getSensorList() {       
        return sensorlist;
    }
    @Override
    public void setCoordinator(SensorObj coordinator) {
        // TODO Auto-generated method stub
        
    }
    
    // Koordinator functions
  
    
}
