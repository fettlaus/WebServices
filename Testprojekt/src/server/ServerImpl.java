package server;

import java.net.URL;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.xml.namespace.QName;
import javax.xml.ws.Endpoint;

@WebService(serviceName = "server_service", portName = "serverSOAP", targetNamespace = "http://server/", name = "server",endpointInterface="server.Server")
@SOAPBinding(style=Style.RPC)
public class ServerImpl implements Server {

    @Override
    public void msg(int msgRequest) {
        System.out.println("Received: "+msgRequest);
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        ServerImpl srv = new ServerImpl();
        Endpoint ep = Endpoint.create(srv);
        ep.publish("http://localhost:9090/server");
        try {
            ServerService ss = new ServerService(new URL("http://localhost:9090/server?wsdl"),new QName("http://server/","server_service"));
            Server s = ss.getServerSOAP();
            //Server s = new Server_Service().getServerSOAP();
            
            
            s.msg(3);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }


}
