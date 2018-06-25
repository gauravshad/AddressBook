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

package com.eai.addressbook.model;

import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;


/**
 *
 * @author gaurav shad
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
