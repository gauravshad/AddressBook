/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eai.addressbook.server;

import com.eai.addressbook.model.Contact;
import com.eai.addressbook.model.JSONconverter;
import com.eai.addressbook.model.RestResponse;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;

/**
 *
 * @author gaura
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
                                //make all contacts
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
