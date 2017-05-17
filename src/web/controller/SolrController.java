package web.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import service.solrclient.SolrService;

@RestController
@RequestMapping("{core}")
public class SolrController extends BaseController {
	
    @Override
    @ApiOperation(value="获取SolrDocument", notes="获取一个默认SolrDocument，一般为solr查询第1个")
	@ApiImplicitParam(name = "core",value = "core", dataType = "String", paramType = "path")
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public Object get(@PathVariable("core") String core) {
    	
    	if(CheckCore(core)){
        	SolrService solrService = new SolrService(core);    	
            return solrService.GetModel();    		
    	}else{
    		return null;
    	}
    }

    @Override
	@ApiOperation(value="获取SolrDocument", notes="根据ID获取对应SolrDocument")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "core",value = "core", dataType = "String", paramType = "path"),
    	@ApiImplicitParam(name = "id",value = "id", dataType = "Long", paramType = "path")
	})
	@RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public Object getById(@PathVariable("core") String core, @PathVariable("id") Long id) {
    	
    	if(CheckCore(core)){
	    	SolrService solrService = new SolrService(core);    	
	        return solrService.GetModel(id);
    	}else{
    		return null;
    	}
    }

    @Override
	@ApiOperation(value="获取SolrDocument", notes="获取一组默认SolrDocument，一般为solr查询前10个")
	@ApiImplicitParam(name = "core",value = "core", dataType = "String", paramType = "path")
	@RequestMapping(value = "/getlst", method = RequestMethod.GET)
    public List<Object> getLst(@PathVariable("core") String core) {
    	if(CheckCore(core)){
	    	SolrService solrService = new SolrService(core);    	
	        return solrService.GetModels();
    	}else{
    		return null;
    	}
    }

    @Override
	@ApiOperation(value="获取SolrDocument", notes="获取分页SolrDocument")
    @ApiImplicitParams({
		@ApiImplicitParam(dataType = "String", name = "core",value = "core", paramType = "path"),
        @ApiImplicitParam(dataType = "Int", name = "pagenum", value = "页码", required = false, paramType = "query"),
        @ApiImplicitParam(dataType = "Int", name = "pagesize", value = "页容量", required = false, paramType = "query")
    })
	@RequestMapping(value = "/getpage", method = RequestMethod.GET)
    public List<Object> getPage(@PathVariable("core") String core
    		, @RequestParam(value="pagenum", defaultValue="1") int page_num
    		, @RequestParam(value = "pagesize", defaultValue="10") int page_size) {
    	if(CheckCore(core)){    	
	    	SolrService solrService = new SolrService(core);    	
	        return solrService.GetModelsPage(page_num, page_size);
    	}else{
    		return null;
    	}
    }

    @SuppressWarnings("unchecked")
	@Override
	@ApiOperation(value="获取SolrDocument", notes="获取分页SolrDocument,并可提交Json格式查询参数,不包含{}")
    @ApiImplicitParams({
		@ApiImplicitParam(dataType = "String", name = "core",value = "core", paramType = "path"),
        @ApiImplicitParam(dataType = "Int", name = "pagenum", value = "页码", required = false, paramType = "query"),
        @ApiImplicitParam(dataType = "Int", name = "pagesize", value = "页容量", required = false, paramType = "query"),
        @ApiImplicitParam(dataType = "String", name = "parameters", value = "查询参数", required = false, paramType = "query")
    })
	@RequestMapping(value = "/getLstByParas", method = RequestMethod.GET)
    public List<Object> getLstByParas(@PathVariable("core") String core
    		, @RequestParam(value="pagenum", defaultValue="1") int page_num
    		, @RequestParam(value = "pagesize", defaultValue="10") int page_size
    		, @RequestParam(value = "parameters", defaultValue="") String parameters) {

    	if(CheckCore(core)){
	    	SolrService solrService = new SolrService(core);
	    	
			Map<String, String> parameters_map = new HashMap<>();
			if(!parameters.isEmpty()){
				ObjectMapper mapper = new ObjectMapper();
				try {
					parameters_map = mapper.readValue('{'+parameters+'}', Map.class);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	    	return solrService.GetModels(page_num, page_size, parameters_map);
    	}else{
    		return null;
    	}
    }

    @Override
	@ApiOperation(value="更新SolrDocument", notes="传入SolrDocument model更新对应id的SolrDocument")
    @ApiImplicitParams({
		@ApiImplicitParam(dataType = "String", name = "core",value = "core", paramType = "path"),
    	@ApiImplicitParam(dataType = "Object", name = "model", value = "SolrDocument", required = true, paramType = "body")
    })
	@RequestMapping(value = "/update", method = RequestMethod.PUT)
    public void update(@PathVariable("core") String core, @RequestBody Object model) {
    	if(CheckCore(core)){
	    	SolrService solrService = new SolrService(core);
	    	
	    	solrService.UpdateModel((LinkedHashMap<?, ?>)model);
    	}
    }

    @SuppressWarnings("unchecked")
	@Override
	@ApiOperation(value="更新SolrDocument", notes="传入SolrDocument更新字段更新对应id的SolrDocument")
    @ApiImplicitParams({
		@ApiImplicitParam(dataType = "String", name = "core",value = "core", paramType = "path"),
        @ApiImplicitParam(dataType = "Long", name = "id", value = "id", required = true, paramType = "path"),
        @ApiImplicitParam(dataType = "String", name = "fields", value = "更新字段", required = true, paramType = "body")
    })
	@RequestMapping(value = "/updatefield/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable("core") String core
    		, @PathVariable("id") Long id
    		, @RequestBody String fields) {
    	if(CheckCore(core)){
	    	SolrService solrService = new SolrService(core);
			Map<String, Object> field = new HashMap<>();
			ObjectMapper mapper = new ObjectMapper();
			try {
				field = mapper.readValue(fields, Map.class);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			LinkedHashMap<String, Object> object = (LinkedHashMap<String, Object>)solrService.GetModel(id);
			for(String key : field.keySet()){
				//这里待修改
				object.remove(key);
				object.put(key, field.get(key));//.replace(key, object.get(key), field.get(key));
			}
			
			solrService.UpdateModel(object);
    	}
    }

    @Override
	@ApiOperation(value="删除SolrDocument", notes="传入SolrDocument model删除对应id的SolrDocument")
    @ApiImplicitParams({
		@ApiImplicitParam(dataType = "String", name = "core",value = "core", paramType = "path"),
    	@ApiImplicitParam(dataType = "Object", name = "model", value = "SolrDocument", required = true, paramType = "body")
    })
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public void delete(@PathVariable("core") String core, @RequestBody Object model) {
    	if(CheckCore(core)){
	    	SolrService solrService = new SolrService(core);    	
	    	
	    	solrService.DeleteModel((LinkedHashMap<?,?>)model);
    	}
    }

    @Override
	@ApiOperation(value="新增SolrDocument", notes="传入SolrDocument model新增SolrDocument")
    @ApiImplicitParams({
		@ApiImplicitParam(dataType = "String", name = "core",value = "core", paramType = "path"),
    	@ApiImplicitParam(dataType = "Object", name = "model", value = "SolrDocument", required = true, paramType = "body")
    })
	@RequestMapping(value = "/add", method = RequestMethod.POST)
    public void add(@PathVariable("core") String core, @RequestBody Object model) {
    	if(CheckCore(core)){
	    	SolrService solrService = new SolrService(core); 
	    	
	    	solrService.AddModel((LinkedHashMap<?,?>)model);
    	}
    }
}
