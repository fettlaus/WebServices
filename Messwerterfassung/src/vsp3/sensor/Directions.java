
package vsp3.sensor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Directions complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Directions">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="NE" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="SE" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="SW" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="NW" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Directions", propOrder = {
    "ne",
    "se",
    "sw",
    "nw"
})
public class Directions {

    @XmlElement(name = "NE")
    protected boolean ne;
    @XmlElement(name = "SE")
    protected boolean se;
    @XmlElement(name = "SW")
    protected boolean sw;
    @XmlElement(name = "NW")
    protected boolean nw;

    /**
     * Gets the value of the ne property.
     * 
     */
    public boolean isNE() {
        return ne;
    }

    /**
     * Sets the value of the ne property.
     * 
     */
    public void setNE(boolean value) {
        this.ne = value;
    }

    /**
     * Gets the value of the se property.
     * 
     */
    public boolean isSE() {
        return se;
    }

    /**
     * Sets the value of the se property.
     * 
     */
    public void setSE(boolean value) {
        this.se = value;
    }

    /**
     * Gets the value of the sw property.
     * 
     */
    public boolean isSW() {
        return sw;
    }

    /**
     * Sets the value of the sw property.
     * 
     */
    public void setSW(boolean value) {
        this.sw = value;
    }

    /**
     * Gets the value of the nw property.
     * 
     */
    public boolean isNW() {
        return nw;
    }

    /**
     * Sets the value of the nw property.
     * 
     */
    public void setNW(boolean value) {
        this.nw = value;
    }

}
