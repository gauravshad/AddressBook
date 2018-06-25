/*
 * Copyright 2018 gaurav shad.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.eai.addressbook.server;

import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
//import org.apache.http.impl.bootstrap.HttpServer;

/**
 *
 * @author gaurav shad
 */
public class RestServer {
    private HttpServer server;
    
    public void setupServer(int port){
        try{
            server = HttpServer.create(new InetSocketAddress(port), 10);
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
