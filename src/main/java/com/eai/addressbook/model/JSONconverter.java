/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eai.addressbook.model;

/**
 *
 * @author gaura
 */

import com.google.gson.Gson;
import java.io.InputStream;
import java.io.InputStreamReader;

public class JSONconverter {
    private static Gson gson = new Gson();
    
    public static String toJson(Object obj){
        return gson.toJson(obj);
    }
    
    public static Object toObject(String json, Class cls){
        return gson.fromJson(json, cls);
    }
    
    public static String toString(InputStream input){
        StringBuffer sb = new StringBuffer();
        InputStreamReader sr = new InputStreamReader(input);
        char[] buf = new char[1024];
        int len;
        try{
            while ((len = sr.read(buf)) > 0) {
                sb.append(buf, 0, len);
            }
        }
        catch(Exception e){
            
        }
        return sb.toString();
    }
}
