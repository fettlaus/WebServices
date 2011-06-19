
package hawmetering;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for webColor complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="webColor">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="red" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="green" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="blue" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="alpha" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "webColor", propOrder = {
    "red",
    "green",
    "blue",
    "alpha"
})
public class WebColor {

    protected int red;
    protected int green;
    protected int blue;
    protected int alpha;

    /**
     * Gets the value of the red property.
     * 
     */
    public int getRed() {
        return red;
    }

    /**
     * Sets the value of the red property.
     * 
     */
    public void setRed(int value) {
        this.red = value;
    }

    /**
     * Gets the value of the green property.
     * 
     */
    public int getGreen() {
        return green;
    }

    /**
     * Sets the value of the green property.
     * 
     */
    public void setGreen(int value) {
        this.green = value;
    }

    /**
     * Gets the value of the blue property.
     * 
     */
    public int getBlue() {
        return blue;
    }

    /**
     * Sets the value of the blue property.
     * 
     */
    public void setBlue(int value) {
        this.blue = value;
    }

    /**
     * Gets the value of the alpha property.
     * 
     */
    public int getAlpha() {
        return alpha;
    }

    /**
     * Sets the value of the alpha property.
     * 
     */
    public void setAlpha(int value) {
        this.alpha = value;
    }

}
