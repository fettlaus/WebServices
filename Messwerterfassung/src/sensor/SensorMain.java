package sensor;

import javax.xml.ws.Endpoint;


public class SensorMain {

  
	
    public static void main(String[] args) {
        // TODO Auto-generated method stub
    	//Port /Endpoint(Sensor x)/ existierenden Sensor/ ... / 
    	 String host = "localhost";
         String sensorName = "Sensorx";
         String existingSensor = "";
         String anzeige[] = null;
         int anzeigeCus[];
         Directions directions;
         int dirAs = 2;
         
         
         if (args.length >= 1) {
        	 sensorName = args[0];
         }

         if (args.length >= 2) {
        	 if (args[1].equals("-k")) {
        		 existingSensor = null;
        	 }else if (args[1].equals("-s")){
        		 existingSensor = args[2];
        		 dirAs = 3;
        	 }
        	 
         }

         if (args.length >= 3) {
        	 anzeige = new String[args.length - dirAs];
        	 for (int i = 0; i < args.length - dirAs; i++) {
        		 anzeige[i] = args[i+dirAs];
        	 }
        	 
         }
         directions = customize(anzeige);
         
//         System.out.println(sensorName + " " + existingSensor);
//     	 System.out.println(directions.isNE() + " " + directions.isSE() + " " + directions.isSW() + " " + directions.isSE());

         SensorImpl sensor = new SensorImpl(sensorName, existingSensor, directions);
         @SuppressWarnings("unused")
        Endpoint endpoint =
             Endpoint.publish("http://"+ sensorName +"/sensor", sensor);
         
         
         
    }
    private static Directions customize(String[] anzeige){
    	Directions directions = new Directions();
    	for (String anz : anzeige) {
			if(anz.equals("NE")){
				directions.setNE(true); 
			}else if(anz.equals("SE")){
				directions.setSE(true); 
			}else if(anz.equals("SW")){
				directions.setSW(true);
			}else if(anz.equals("NW")){
				directions.setNW(true); 
			}
		}
    	    	
    	return directions;
	}
}
