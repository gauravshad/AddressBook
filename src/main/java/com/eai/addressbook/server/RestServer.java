/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eai.addressbook.server;

import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
//import org.apache.http.impl.bootstrap.HttpServer;

/**
 *
 * @author gaura
 */
public class RestServer {
    private HttpServer server;
    
    public void setupServer(String port){
        try{
            server = HttpServer.create(new InetSocketAddress(Integer.parseInt(port)), 10);
        }
        catch(Exception e){
            System.out.println("Could not setup http server");
            e.printStackTrace();
        }
        
        server.setExecutor(null);
        
        server.createContext("/", new RestRequestHandler());
        
        server.start();
    }
    
    public void close(){
        server.stop(0);
    }
}
