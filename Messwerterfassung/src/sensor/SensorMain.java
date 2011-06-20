package sensor;

import java.util.ArrayList;
import java.util.List;

import javax.xml.ws.Endpoint;

public class SensorMain {

  
	
    public static void main(String[] args) {
        // TODO Auto-generated method stub
    	//Port /Endpoint(Sensor x)/ existierenden Sensor/ ... / 
    	 String host = "localhost";
         String port = "1080";
         String sensorName = "Sensorx";
         String existingSensor = "";
         String anzeige[] = new String[args.length - 3];
         int anzeigeCus[];
         
         
         if (args.length >= 1) {
        	 sensorName = args[0];
         }

         if (args.length >= 2) {
        	 existingSensor = args[1];
         }

         if (args.length >= 3) {
        	 for (int i = 0; i < args.length - 2; i++) {
        		 anzeige[i] = args[i+3];
        	 }
        	 
         }
         anzeigeCus = customize(anzeige);
         
//         System.out.println(port + " " + sensorName + " " + existingSensor);
//         

//     	 for (int i = 0; i < anzeigeCus.length; i++) {
//    		 System.out.println(anzeigeCus[i]);;
//    	 }
         Sensor sensor = new SensorImpl(sensorName, existingSensor, anzeigeCus);
         Endpoint endpoint =
             Endpoint.publish("http://"+ host +":"+ port +"/sensor", sensor);
         
         
         
    }
    private static int[] customize(String[] anzeige){
    	List<Integer> ausgabe = new ArrayList<Integer>();
    	
    	for (String anz : anzeige) {
			if(anz.equals("NO") && !ausgabe.contains(0)){
				ausgabe.add(0); 
			}else if(anz.equals("SO") && !ausgabe.contains(1)){
				ausgabe.add(1); 
			}else if(anz.equals("SW") && !ausgabe.contains(2)){
				ausgabe.add(2); 
			}else if(anz.equals("NW") && !ausgabe.contains(3)){
				ausgabe.add(3); 
			}
		}
    	
    	int anzeigeCus[] = new int[ausgabe.size()];
    	for (int i = 0; i < anzeigeCus.length; i++) {
    		anzeigeCus[i] = ausgabe.get(i);
    	}
    	
    	return anzeigeCus;
	}
}
