package sensor;

public class Sensor {

    int port;
    String name;
    String existing;
    int[] directions;
    
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

}
