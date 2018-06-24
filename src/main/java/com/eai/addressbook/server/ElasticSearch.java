/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eai.addressbook.server;

/**
 *
 * @author gaura
 */

import com.eai.addressbook.model.Contact;
import com.eai.addressbook.model.JSONconverter;
import java.net.InetAddress;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.index.reindex.UpdateByQueryAction;
import org.elasticsearch.index.reindex.UpdateByQueryRequestBuilder;
import org.elasticsearch.node.Node;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
//import pl.allegro.tech.embeddedelasticsearch.EmbeddedElastic;
//import pl.allegro.tech.embeddedelasticsearch.PopularProperties;

public class ElasticSearch {
    private static ElasticSearch instance;
    private TransportClient client;
    private TransportAddress address;
    private String Index = "contacts";
    private String Type = "contact";
    private String Default = "name";
    //private EmbeddedElastic node;
    
    public static ElasticSearch getInstance(){
        if(instance == null)
            instance = new ElasticSearch();
        
        return instance;
    }
    
    private ElasticSearch(){ }
    
    public void SetupServer(String port){
        try{
            /*node = EmbeddedElastic.builder()
                    .withElasticVersion("5.6.0")
                    .withSetting(PopularProperties.HTTP_PORT, Integer.parseInt(port))
                    .build();
            node.start();*/
            address = new InetSocketTransportAddress(InetAddress.getByName("localhost"), Integer.parseInt(port));
            client = new PreBuiltTransportClient(Settings.EMPTY)
                    .addTransportAddress(address);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public boolean createContact(Contact contact){
        
        try{
            IndexResponse response = client.prepareIndex(Index, Type)
                    .setSource(JSONconverter.toJson(contact), XContentType.JSON)
                    .execute()
                    .actionGet();
            
            return true;
        }
        catch(Exception e){
            return false;
        }
    }
    
    public Contact getContact(String name){
        
        try{
        QueryBuilder query = queryStringQuery(name).defaultField(Default);
        
        SearchResponse response = client.prepareSearch()
                .setIndices(Index)
                .setTypes(Type)
                .setQuery(query)
                .execute()
                .actionGet();
        
        if(response.getHits().totalHits < 1)
            return null;
        else
            return (Contact) JSONconverter.toObject(response.getHits().getAt(0).getSourceAsString(), Contact.class);
        
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
    
    public boolean updateContact(String name) throws Exception{
        Contact contact = getContact(name);
        if(contact == null)
            throw new Exception("Contact Not Found!!!");
        
        try{
            UpdateByQueryRequestBuilder request = UpdateByQueryAction.INSTANCE.newRequestBuilder(client);
            BulkByScrollResponse response = request.source(Index)
                                                    .filter(QueryBuilders.matchQuery(Default, name))
                                                    .get();
            
            if(response.getUpdated() > 0)
                return true;
            else
                return false;
            
        }
        catch(Exception e){
            return false;
        }
    }
    
    public boolean deleteContact(String name){
        try{
            BulkByScrollResponse response = DeleteByQueryAction.INSTANCE.newRequestBuilder(client)
                    .filter(QueryBuilders.matchQuery(Default, name))
                    .source(Index)
                    .get();
            
            if(response.getDeleted()>0)
                return true;
            else
                return false;
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
    
    public void close(){
        client.close();
    }
}
