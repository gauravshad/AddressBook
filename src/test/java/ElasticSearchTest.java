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

import com.eai.addressbook.model.Contact;
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
public class ElasticSearchTest {
    ElasticSearch api;
    
    public ElasticSearchTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        api = ElasticSearch.getInstance();
        api.SetupServer("9300");
    }
    
    @After
    public void tearDown() {
        api.close();
    }

    @Test
    public void CreateTest(){
        System.out.println("    .Running Create Test");
        Contact mike = new Contact("mike", "1234567890", "Orange Street");
        boolean status1 = api.createContact(mike);
        assert(status1 == true);
        
        Contact jersey = new Contact("jersey", "9999888822", "Washington");
        boolean status2 = api.createContact(jersey);
        assert(status2 == true);
    }
    
    @Test
    public void ReadTest(){
        System.out.println("    .Running Read Test");
        Contact mike = new Contact("mike", "1234567890", "Orange Street");
        boolean status1 = api.createContact(mike);
        
        Contact resp = api.getContact("mike");
        assertNotNull(resp);
        assert(resp.getName().equals("mike"));
    }
    
    @Test
    public void UpdateTest(){
        System.out.println("    .Running Update Test");
        try{
        boolean status1 = api.updateContact("mike");
        assert(status1 == true);
        }
        catch(Exception e){
            
        }
        
        try{
            boolean status2 = api.updateContact("randomName");
        }
        catch(Exception e){
            assert(e.getMessage().equals("Contact Not Found!!!"));
        }
    }
    
    @Test
    public void DeleteTest(){
        System.out.println("    .Running Delete Test");
        boolean status1 = api.deleteContact("mike");
        assert(status1 == true);
    }
}
