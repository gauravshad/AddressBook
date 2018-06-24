/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
 * @author gaura
 */
public class ElasticSearchTest {
    ElasticSearch api;
    RestServer server;
    
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
        server = new RestServer();
        server.setupServer("1234");
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void basic(){
        
        Contact gaurav = new Contact("gaurav", 123, "Orange Street");
        
        boolean status = api.createContact(gaurav);
        System.out.println(status);
        
        Contact resp = api.getContact("gaurav");
        
        assertNotNull(resp);
        
        if(resp!=null)
            System.out.println(resp.toString());
        
        try{
        Thread.sleep(1000000);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        
        assert(api.deleteContact("gaurav") == true);
        
        
        
        api.close();
    }
}
