package sensor;

public class SensorMain {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
    	//Port /Endpoint(Sensor x)/ existierenden Sensor/ ... / 
    	 String host = "localhost";
         String port = "1080";
         String sensorName = "Sensorx";
         String existingSensor = "";
         String 
         final String name;
         
         if (args.length >= 1) {
             port = args[0];
         }

         if (args.length >= 2) {
             sensorName = args[1];
         }

         if (args.length >= 3) {
             existingSensor = args[2];
         }

         if (args.length >= 4) {
             name = args[3];
         } else {
             name = "STARTER" + rnd.nextInt(Integer.MAX_VALUE);
         }
         
    }
}
