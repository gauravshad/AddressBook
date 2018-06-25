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

package com.eai.addressbook.client;

import com.eai.addressbook.model.Contact;
import com.eai.addressbook.model.JSONconverter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gaurav shad
 */
public class RestRequestBuilder {
    private static RestRequestBuilder instance;
    String host;
    int port;
    String hostURL;
    
    public static RestRequestBuilder getInstance(){
        if(instance == null)
            instance = new RestRequestBuilder();
        
        return instance;
    }
    
    public void setupBuilder(int port){
        this.host = "localhost";
        this.port = port;
        
        this.hostURL = "http://" + host + ":" + port;
    }
    
    public String Get(String request, String api){
        try {
            URL url = new URL(hostURL + api);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(request);
            connection.setDoOutput(true);
            connection.addRequestProperty("Accept", "application/json");
            connection.connect();
            
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream output = connection.getInputStream();
                
                String response = JSONconverter.toString(output);
                output.close();
                connection.disconnect();
                return response;
            }
            else
            {
                connection.disconnect();
                return "Error in Server response";
            }
            
        } catch (MalformedURLException e) {
            return "Invalid request!!!";
        } catch (ProtocolException e) {
            return "Request Protocol Error";
        } catch (IOException e) {
            return "connection error";
        }
    }
    
    public String Post(String request, String api, Contact contact){
        try {
            URL url = new URL(hostURL + api);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod(request);
            connection.addRequestProperty("Accept", "application/json");
            connection.connect();

            String json = JSONconverter.toJson(contact);

            OutputStreamWriter requestBody = new OutputStreamWriter(connection.getOutputStream());
            requestBody.write(json);
            requestBody.close();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream output = connection.getInputStream();
                String response = JSONconverter.toString(output);
                output.close();
                connection.disconnect();
                return response;
            } else {
                connection.disconnect();
                return "Error in Server response";
            }
        }   catch (MalformedURLException ex) {
            return "Invalid request!!!";
        }  catch (ProtocolException e) {
            return "Request Protocol Error";
        }   catch (IOException ex) {
            return "connection error";
        }
    }
}
