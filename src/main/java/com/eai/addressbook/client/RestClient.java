/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eai.addressbook.client;

import com.eai.addressbook.model.Contact;
import com.eai.addressbook.server.ElasticSearch;
import com.eai.addressbook.server.RestServer;
import java.util.Scanner;

/**
 *
 * @author gaura
 */
public class RestClient {
    private static ElasticSearch es;
    private static RestServer rs;
    private static RestRequestBuilder rb;
    private static Scanner input;
    
    static void populateOptions(){
        System.out.println("1. Make an API call \n2. Help [show commands] \n3. Exit \n");
    }
    
    static void help(){
        System.out.println("Basic syntax of API calls: \n GET/contact/name \n POST/contact \n PUT/contact/name \n DELETE/contact/name");
    }
    
    public static void main(String[] args){
        
        String port = "8080";
        
        es = ElasticSearch.getInstance();
        es.SetupServer("9300");
        
        rs = new RestServer();
        rs.setupServer(port);
        
        rb = RestRequestBuilder.getInstance();
        rb.setupBuilder(port);
        
        input = new Scanner(System.in);
        int in = 0;
        boolean choice = true;
        populateOptions();
        
        while(choice){
            
            System.out.print("\nEnter an option: ");
            try{
                in = Integer.parseInt(input.next());
            }
            catch(Exception e){
                System.out.println("Invalid input. Please try again!");
                continue;
            }
            
            switch(in){
                case 1: System.out.println("Enter API call:");
                        String call = input.next();
                        
                        String response = parseCall(call);
                        
                        System.out.println(response);
                        break;
                        
                case 2: help();
                        break;
                        
                case 3: System.out.println("Shutting Down");
                        es.close();
                        rs.close();
                        choice = false;
                        break;
                        
                default: System.out.println("Invalid Input, Please try again!!!");
                         break;
            }
            
        }
        
    }
    
    private static String parseCall(String call){
        String request = call.split("/")[0].toLowerCase();
        
        if(request.equals("get"))
            return rb.Get("GET", call.substring(3));
        else if(request.equals("delete"))
            return rb.Get("DELETE", call.substring(6));
        else if(request.equals("put"))
            return rb.Get("PUT", call.substring(3));
        else if(request.equals("post")){
            String name;
            String number;
            String address;
            System.out.println("Enter contact name");
            name = input.next();
            System.out.println("Enter number");
            number = input.next();
            System.out.println("Enter address");
            address = input.next();
            Contact contact = new Contact(name, number, address);
            return rb.Post("POST", call.substring(4), contact);
        }
        else
            return "Invalid Request, Please try again!!!";
        
        
    }
}
