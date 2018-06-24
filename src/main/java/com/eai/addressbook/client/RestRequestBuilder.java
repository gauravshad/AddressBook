/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
 * @author gaura
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
    
    public void setupBuilder(String port){
        this.host = "localhost";
        this.port = Integer.parseInt(port);
        
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
