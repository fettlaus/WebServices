
package vsp3.sensor;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the vsp3.sensor package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: vsp3.sensor
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link SensorList }
     * 
     */
    public SensorList createSensorList() {
        return new SensorList();
    }

    /**
     * Create an instance of {@link Directions }
     * 
     */
    public Directions createDirections() {
        return new Directions();
    }

    /**
     * Create an instance of {@link SensorObj }
     * 
     */
    public SensorObj createSensorObj() {
        return new SensorObj();
    }

}
