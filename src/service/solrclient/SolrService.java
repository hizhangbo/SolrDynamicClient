package service.solrclient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import container.reflect.ReflectHelper;

@Repository
public class SolrService {
	
	
	private SolrDynamicClient solrProxyClient;
	private ReflectHelper reflectHelper;
	
	@Autowired
	public SolrService(String core){
		solrProxyClient = new SolrDynamicClient(core);
		reflectHelper = new ReflectHelper(solrProxyClient.coreProperties);
	}
	
    public Object GetModel() {
        SolrQuery solrQuery = new SolrQuery();
        solrQuery.set("q","*:*");
        solrQuery.setRows(10);

        QueryResponse queryResponse = null;
		try {
			queryResponse = solrProxyClient.solrClient.query(solrQuery);
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        SolrDocumentList response = queryResponse.getResults();

        List<Object> objects = new ArrayList<>();
        for (SolrDocument item : response)
        {
            Object object = BuildModel(item);
            objects.add(object);
        }

        Object result = null;
        if(objects != null && objects.size()>0){
            result = objects.get(0);
        }
        return result;
    }

    public Object GetModel(Long id) {
        SolrQuery solrQuery = new SolrQuery();
        solrQuery.set("q","id:"+id);

        QueryResponse queryResponse = null;
		try {
			queryResponse = solrProxyClient.solrClient.query(solrQuery);
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        SolrDocumentList response = queryResponse.getResults();

        List<Object> objects = new ArrayList<Object>();
        for (SolrDocument item : response)
        {
        	Object object = BuildModel(item);
        	objects.add(object);
        }

        Object result = null;
        if(objects!=null && objects.size()>0){
            result = objects.get(0);
        }
        return result;
    }

    public List<Object> GetModels() {
        SolrQuery solrQuery = new SolrQuery();
        solrQuery.set("q","*:*");
        solrQuery.setRows(10);

        QueryResponse queryResponse = null;
		try {
			queryResponse = solrProxyClient.solrClient.query(solrQuery);
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        SolrDocumentList response = queryResponse.getResults();

        List<Object> objects = new ArrayList<Object>();
        for (SolrDocument item : response)
        {
        	Object object = BuildModel(item);
        	objects.add(object);
        }

        return objects;
    }

    public List<Object> GetModelsPage(Integer pageNumber, Integer pageSize) {
    	int startIdx = 0;
    	if(pageNumber > 1){
    		startIdx = (pageNumber - 1) * pageSize;
    	}
    	
        SolrQuery solrQuery = new SolrQuery();
        solrQuery.set("q","*:*");
        solrQuery.setRows(pageSize);
        solrQuery.setStart(startIdx);

        QueryResponse queryResponse = null;
		try {
			queryResponse = solrProxyClient.solrClient.query(solrQuery);
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        SolrDocumentList response = queryResponse.getResults();

        List<Object> objects = new ArrayList<Object>();
        for (SolrDocument item : response)
        {
        	Object object = new Object();
        	object = BuildModel(item);
        	objects.add(object);
        }

        return objects;
    }

    public List<Object> GetModels(Integer pageNumber, Integer pageSize, Map<String, String> parameters) {
    	int startIdx = 0;
    	if(pageNumber > 1){
    		startIdx = (pageNumber - 1) * pageSize;
    	}
    	
        SolrQuery solrQuery = new SolrQuery();
        solrQuery.set("q","*:*");
        solrQuery.setRows(pageSize);
        solrQuery.setStart(startIdx);
        for(Map.Entry<String,String> entry : parameters.entrySet()){
        	solrQuery.setFilterQueries(entry.getKey()+':'+ entry.getValue());
        	
            //solrQuery.setQuery(entry.getKey()+':'+ entry.getValue());
        }

        QueryResponse queryResponse = null;
		try {
			queryResponse = solrProxyClient.solrClient.query(solrQuery);
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        SolrDocumentList response = queryResponse.getResults();

        List<Object> objects = new ArrayList<Object>();
        Object object = null;
        for (SolrDocument item : response)
        {
        	object = new Object();
        	object = BuildModel(item);
        	objects.add(object);
        }

        return objects;
    }

    public void UpdateModel(LinkedHashMap<?, ?> model) {

        Map<String, Object> fields = reflectHelper.getFieldValue(solrProxyClient.coreProperties, model);
        SolrInputDocument doc = new SolrInputDocument();

        doc.addField("id", fields.get("id"));
        for (Map.Entry<String, Object> entry : fields.entrySet()){
            if(!entry.getKey().equals("id")){
                HashMap<String, Object> hash = new HashMap<String, Object>();
                hash.put("set", fields.get(entry.getKey()));

                doc.addField(entry.getKey(), hash);
            }
        }

        try {
        	solrProxyClient.solrClient.add(doc);
        	solrProxyClient.solrClient.commit();
	        //UpdateResponse response = solrClient.commit();
	        //int result = response.getStatus();
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

//    public void UpdateModel(Long id, Map<String,Object> field) {
//
//        SolrInputDocument doc = new SolrInputDocument();
//        Map<String, Object> fields = new HashMap<>();
//        for(String key : solrProxyClient.coreProperties.keySet()){
//        	fields.put(key, field.get(key));
//        }        
//        
//        doc.addField("id", id);
//        for (Map.Entry<String, Object> entry : fields.entrySet()){
//            if(!entry.getKey().equals("id")){
//                HashMap<String, Object> hash = new HashMap<String, Object>();
//                hash.put("set", fields.get(entry.getKey()));
//
//                doc.addField(entry.getKey(), hash);
//            }
//        }
//
//        try {
//        	solrProxyClient.solrClient.add(doc);
//        	solrProxyClient.solrClient.commit();
//	        //UpdateResponse response = solrClient.commit();
//	        //int result = response.getStatus();
//		} catch (SolrServerException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//    }

    public void DeleteModel(LinkedHashMap<?,?> model) {
        try {
        	solrProxyClient.solrClient.deleteById(model.get("id").toString());
        	solrProxyClient.solrClient.commit();
	        //UpdateResponse response = solrClient.commit();
	        //int result = response.getStatus();
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public void AddModel(LinkedHashMap<?,?> model) {
        Map<String, Object> fields = new HashMap<>();
        
        //这里需要增加默认值逻辑
		for(String key : solrProxyClient.coreProperties.keySet()){
			fields.put(key, model.get(key));
		}

        SolrInputDocument doc = new SolrInputDocument();
        for (Map.Entry<String, Object> entry:fields.entrySet()){
            doc.addField(entry.getKey(), entry.getValue());
        }
        try {
        	solrProxyClient.solrClient.add(doc);
        	solrProxyClient.solrClient.commit();
	        //UpdateResponse response = solrClient.commit();
	        //int result = response.getStatus();
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public Object BuildModel(SolrDocument solrDocument) {
        Object object = reflectHelper.getObject();
        for (String key : solrProxyClient.coreProperties.keySet()){
            reflectHelper.setValue(key, solrDocument.getFirstValue(key));
        }
        object = reflectHelper.object;
        return object;
    }

    public void RollBack() {

    }
}
