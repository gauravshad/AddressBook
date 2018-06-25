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

import com.eai.addressbook.model.Contact;
import com.eai.addressbook.model.JSONconverter;
import com.eai.addressbook.model.RestResponse;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.util.Set;

/**
 *
 * @author gaurav shad
 */
public class RestRequestHandler implements HttpHandler{
    
    ElasticSearch api = ElasticSearch.getInstance();
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        
        String[] url = httpExchange.getRequestURI().toString().split("/");
        
        String request = httpExchange.getRequestMethod();
        
        if(!url[1].toLowerCase().contains("contact"))
            RestResponse.sendResponse("Invalid Request, Please try again!!!", httpExchange);
        else{
            
            switch(request.toLowerCase()){
                case "get": if(url.length >=3){
                                String name = url[2];
                                try{
                                Contact res = api.getContact(name);
                                
                                if(res!=null)
                                    RestResponse.sendResponse(JSONconverter.toJson(res), httpExchange);
                                else
                                    RestResponse.sendResponse("Contact Not Found!!!", httpExchange);
                                
                                }
                                catch(Exception e){
                                    RestResponse.sendResponse("Error Occured!!!", httpExchange);
                                }
                            }
                            else{
                                try{
                                    
                                    String query = null;
                                    if(url[1].contains("?")){
                                        query = url[1].split("\\?")[1];
                                    }
                                    
                                    Set<Contact> res = api.getWithQuery(query);
                                    
                                    if(res!=null)
                                        RestResponse.sendResponse(JSONconverter.toJson(res), httpExchange);
                                    else
                                        RestResponse.sendResponse("No results match the given query!!!", httpExchange);
                                }
                                catch(Exception e){
                                    RestResponse.sendResponse("Error Occured!!! "+e.getMessage(), httpExchange);
                                }
                                
                            }
                            
                            break;
                            
                case "post": try{
                                Contact contact = (Contact) JSONconverter.toObject(JSONconverter.toString(httpExchange.getRequestBody()), Contact.class);
                                boolean status = api.createContact(contact);
                                if(status)
                                    RestResponse.sendResponse("Contact successfully created", httpExchange);
                                else
                                    RestResponse.sendResponse("Contact Creation Failed!!!", httpExchange);
                                
                            }
                            catch(Exception e){
                                
                            }
                            break;
                            
                case "put": if(url.length >=3){
                                String name = url[2];
                                
                                try{
                                    boolean status = api.updateContact(name);
                                    
                                    if(status)
                                        RestResponse.sendResponse("Contact successfully updated", httpExchange);
                                    else
                                        RestResponse.sendResponse("Contact update failed!!!", httpExchange);
                                    
                                }
                                catch(Exception e){
                                    RestResponse.sendResponse(e.getMessage(), httpExchange);
                                }
                            }
                            else{
                                RestResponse.sendResponse("Contact name not provided!!!", httpExchange);
                            }
                            break;
                    
                case "delete": if(url.length >=3){
                                    String name = url[2];
                                    try{
                                       boolean status = api.deleteContact(name);
                                       if(status)
                                           RestResponse.sendResponse("Contact successfully deleted", httpExchange);
                                       else
                                           RestResponse.sendResponse("Contact Not Found!!!", httpExchange);
                                    }
                                    catch(Exception e){

                                    }
                                }
                                else{
                                    RestResponse.sendResponse("Contact name not provided!!!", httpExchange);
                                }
                                break;
                                
                default: RestResponse.sendResponse("Invalid request, Please try again!!!", httpExchange);
                         break;
            }
            
        }
    }
    
    
    
}
