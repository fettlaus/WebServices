package sensor;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.xml.namespace.QName;

@WebService(name="Sensor")
@SOAPBinding(style=Style.RPC)
public class SensorImpl {

    public long id;
    String name;
    String existing;
    int[] directions;
    ArrayList<SensorRef> sensorlist;
    // Liste ueber http://ip.address:port/
    ArrayList<String> sensorstrings;
    SensorRef coordinator;
    
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
    
    private SensorRef toSensor(String sensor) throws MalformedURLException{
        return new SensorService(new URL(sensor+"sensor?wsdl"),new QName(sensor,"sensor")).getSensorRefPort();
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
    
    public boolean addSensor(String sensor){
        sensorstrings.add(sensor);
        try {
            sensorlist.add(toSensor(sensor));
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    // Koordinator functions
  
    
}
