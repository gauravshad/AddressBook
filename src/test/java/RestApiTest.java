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

import com.eai.addressbook.client.RestRequestBuilder;
import com.eai.addressbook.model.Contact;
import com.eai.addressbook.model.JSONconverter;
import com.eai.addressbook.server.ElasticSearch;
import com.eai.addressbook.server.RestServer;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author gaurav shad
 */
public class RestApiTest {
    ElasticSearch es;
    RestServer rs;
    RestRequestBuilder rb;
    
    public RestApiTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        es = ElasticSearch.getInstance();
        es.SetupServer("9300");
        
        rs = new RestServer();
        rs.setupServer(8080);
        
        rb = RestRequestBuilder.getInstance();
        rb.setupBuilder(8080);
        
        String call = "POST/contact";
        Contact contact = new Contact("mike", "1234567890", "Orange Street");
        String resp = rb.Post("POST", call.substring(4), contact);
    }
    
    @After
    public void tearDown() {
        es.close();
        rs.close();
    }

    @Test
    public void PostTest(){
        System.out.println("    Running Post Test");
        String call = "POST/contact";
        Contact contact = new Contact("mike", "1234567890", "Orange Street");
        String resp = rb.Post("POST", call.substring(4), contact);
        assertNotNull(resp);
        assert(resp.contains("Contact successfully created"));
        
    }
    
    @Test
    public void GetTest(){
        System.out.println("    Running Get Test");
        String call = "GET/contact/mike";
        String resp = rb.Get("GET", call.substring(3));
        assertNotNull(resp);
        assert(resp.contains("mike"));
    
    }
    
    @Test
    public void PutTest(){
        System.out.println("    Running Put Test");
        String call = "PUT/contact/mike";
        String resp = rb.Get("PUT", call.substring(3));
        assertNotNull(resp);
        assert(resp.contains("Contact successfully updated"));
        
    }
    
}
