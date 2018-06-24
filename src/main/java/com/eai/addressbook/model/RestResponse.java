/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eai.addressbook.model;

import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;


/**
 *
 * @author gaura
 */
public class RestResponse {
    public static JsonObject createResponse(String key, String value){
        JsonObject json = new JsonObject();
        json.addProperty(key, value);
        return json;
    }
    
    public static void sendResponse(String msg, HttpExchange httpExchange){
        try{
        httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
        OutputStreamWriter response = new OutputStreamWriter(httpExchange.getResponseBody());
        response.write(JSONconverter.toJson(createResponse("Response", msg)));
        response.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
