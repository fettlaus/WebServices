package de.fettlaus.webservicetest;

import hawmetering.HAWMetering;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.SwingUtilities;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.GridLayout;
import javax.swing.JToggleButton;
import javax.xml.ws.Endpoint;

import vsp3.sensor.Directions;
import vsp3.sensor.SensorImpl;

public class Visual {

    private JFrame jFrame = null;
    private JPanel jContentPane = null;
    private JToggleButton jToggleButton = null;
    private JToggleButton jToggleButton1 = null;
    private ItemListener il = null;
    private SensorImpl[] rs = new SensorImpl[6];
    private Endpoint[] ep = new Endpoint[6];
    //private Thread[] threads = null;
    Directions d[] = new Directions[6];  //  @jve:decl-index=0:
    String sen[];  //  @jve:decl-index=0:
    String meter;  //  @jve:decl-index=0:
    private JToggleButton jToggleButton2 = null;
    private JToggleButton jToggleButton3 = null;
    private JToggleButton jToggleButton4 = null;
    private JToggleButton jToggleButton5 = null;
    /**
     * This method initializes jToggleButton	
     * 	
     * @return javax.swing.JToggleButton	
     */
    private JToggleButton getJToggleButton() {
        if (jToggleButton == null) {
            jToggleButton = new JToggleButton();
            jToggleButton.setText("Sensor 1");
            jToggleButton.addItemListener(il);
        }
        return jToggleButton;
    }

    /**
     * This method initializes jToggleButton1	
     * 	
     * @return javax.swing.JToggleButton	
     */
    private JToggleButton getJToggleButton1() {
        if (jToggleButton1 == null) {
            jToggleButton1 = new JToggleButton();
            jToggleButton1.setText("Sensor 2");
            jToggleButton1.setEnabled(false);
            jToggleButton1.addItemListener(il);
        }
        return jToggleButton1;
    }

    /**
     * This method initializes jToggleButton2	
     * 	
     * @return javax.swing.JToggleButton	
     */
    private JToggleButton getJToggleButton2() {
        if (jToggleButton2 == null) {
            jToggleButton2 = new JToggleButton();
            jToggleButton2.setText("Sensor 3");
            jToggleButton2.setEnabled(false);
            jToggleButton2.addItemListener(il);
        }
        return jToggleButton2;
    }

    /**
     * This method initializes jToggleButton3	
     * 	
     * @return javax.swing.JToggleButton	
     */
    private JToggleButton getJToggleButton3() {
        if (jToggleButton3 == null) {
            jToggleButton3 = new JToggleButton();
            jToggleButton3.setText("Sensor 4");
            jToggleButton3.setEnabled(false);
            jToggleButton3.addItemListener(il);
        }
        return jToggleButton3;
    }

    /**
     * This method initializes jToggleButton4	
     * 	
     * @return javax.swing.JToggleButton	
     */
    private JToggleButton getJToggleButton4() {
        if (jToggleButton4 == null) {
            jToggleButton4 = new JToggleButton();
            jToggleButton4.setText("Sensor 5");
            jToggleButton4.setEnabled(false);
            jToggleButton4.addItemListener(il);
        }
        return jToggleButton4;
    }

    /**
     * This method initializes jToggleButton5	
     * 	
     * @return javax.swing.JToggleButton	
     */
    private JToggleButton getJToggleButton5() {
        if (jToggleButton5 == null) {
            jToggleButton5 = new JToggleButton();
            jToggleButton5.setText("Sensor 6");
            jToggleButton5.setEnabled(false);
            jToggleButton5.addItemListener(il);
        }
        return jToggleButton5;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("Starting!");
        HAWMetering.main(null);
        
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Visual application = new Visual();
                application.getJFrame().setVisible(true);
                
            }
        });
    }

    /**
     * This method initializes jFrame
     * 
     * @return javax.swing.JFrame
     */
    private JFrame getJFrame() {
        if (jFrame == null) {
            jFrame = new JFrame();
            jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            jFrame.setSize(300, 200);
            jFrame.setContentPane(getJContentPane());
            jFrame.setTitle("WebServices Tester");
        }
        return jFrame;
    }

    /**
     * This method initializes jContentPane
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            GridLayout gridLayout = new GridLayout();
            gridLayout.setRows(6);
            gridLayout.setColumns(4);
            jContentPane = new JPanel();
            jContentPane.setLayout(gridLayout);
            d[0] = new Directions();
            d[1] = new Directions();
            d[2] = new Directions();
            d[3] = new Directions();
            d[4] = new Directions();
            d[5] = new Directions();
            d[0].setNE(true);
            d[1].setNE(true);
            d[1].setSE(true);
            d[2].setNE(true);
            d[2].setSE(true);
            d[2].setSW(true);
            d[3].setNE(true);
            d[3].setSE(true);
            d[3].setSW(true);
            d[3].setNW(true);
            d[4].setSE(true);
            d[4].setSW(true);
            d[4].setNW(true);
            d[5].setSW(true);
            d[5].setNW(true);
            meter = "http://localhost:9999/";
            sen = new String[]{"http://localhost:8787/","http://localhost:8788/","http://localhost:8789/","http://localhost:8790/","http://localhost:8791/","http://localhost:8792/"};         

            il = new java.awt.event.ItemListener() {
                @Override
                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    int sensorID = 0;
                    if(e.getItem().equals(jToggleButton)){
                        sensorID = 0;
                    }else if(e.getItem().equals(jToggleButton1)){
                        sensorID = 1;
                    }else if(e.getItem().equals(jToggleButton2)){
                        sensorID = 2;
                    }else if(e.getItem().equals(jToggleButton3)){
                        sensorID = 3;
                    }else if(e.getItem().equals(jToggleButton4)){
                        sensorID = 4;
                    }else if(e.getItem().equals(jToggleButton5)){
                        sensorID = 5;
                    }
                    if(e.getStateChange() == ItemEvent.SELECTED){
                        System.out.println("Starting Sensor "+sensorID);
                        if(sensorID == 0){
                            jToggleButton1.setEnabled(true);
                            jToggleButton2.setEnabled(true);
                            jToggleButton3.setEnabled(true);
                            jToggleButton4.setEnabled(true);
                            jToggleButton5.setEnabled(true);
                            rs[sensorID] = new SensorImpl(meter, sen[sensorID], null, d[sensorID]);                            
                        }else{
                            rs[sensorID] = new SensorImpl(null, sen[sensorID], sen[0], d[sensorID]);
                        }
                        
                        ep[sensorID] = Endpoint.create(rs[sensorID]);
                        ep[sensorID].publish(sen[sensorID] + "sensor");
                        new Thread(rs[sensorID]).start();
                    }else{
                        if(sensorID==0){
                            if(!jToggleButton1.isSelected())
                                jToggleButton1.setEnabled(false);
                            if(!jToggleButton2.isSelected())
                                jToggleButton2.setEnabled(false);
                            if(!jToggleButton3.isSelected())
                                jToggleButton3.setEnabled(false);
                            if(!jToggleButton4.isSelected())
                                jToggleButton4.setEnabled(false);
                            if(!jToggleButton5.isSelected())
                                jToggleButton5.setEnabled(false);
                        }
                        if(!jToggleButton.isSelected())
                            switch(sensorID){
                                case 1: jToggleButton1.setEnabled(false); break;
                                case 2: jToggleButton2.setEnabled(false); break;
                                case 3: jToggleButton3.setEnabled(false); break;
                                case 4: jToggleButton4.setEnabled(false); break;
                                case 5: jToggleButton5.setEnabled(false); break;
                            }
                        System.out.println("Stopping Sensor "+sensorID);
                       rs[sensorID].stop();
                       ep[sensorID].stop();

                    }
                }                       
            };
            
            jContentPane.add(getJToggleButton(), null);
            jContentPane.add(getJToggleButton1(), null);
            jContentPane.add(getJToggleButton2(), null);
            jContentPane.add(getJToggleButton3(), null);
            jContentPane.add(getJToggleButton4(), null);
            jContentPane.add(getJToggleButton5(), null);
        }
        return jContentPane;
    }

}
