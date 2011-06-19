package sensor;

import java.util.ArrayList;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

@WebService(name="SensorRef")
@SOAPBinding(style=Style.RPC)

public class Sensor {

    public long id;
    int port;
    String name;
    String existing;
    int[] directions;
    ArrayList<SensorRef> sensorlist;
    SensorRef coordinator;
    
    /**
     * @param port Port to run on
     * @param name Name of this Sensor
     * @param existingsensor Some already existing sensor
     * @param directions The scales we want to write on
     */
    Sensor(int port, String name, String existingsensor, int[] directions) {
        this.port = port;
        this.name = name;
        this.existing = existingsensor;
        this.directions = directions;
    }
    
    public SensorRef[] getSensorlist(){
        return sensorlist.toArray(new SensorRef[]{});
    }
    
    // election algorithm 
    
    public boolean setCoordinator(SensorRef coordinator){
        this.coordinator = coordinator;
        return true;
        
    }
    public boolean election(){
        return true;
    }

    // Webservice functions
    
    public long getID(){
        return id;
    }
    
    // Koordinator functions
  
    public void getsomething(){
        
    }
    
}
