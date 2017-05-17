package web.controller;

import java.util.List;

import container.xml.XmlHelper;

public abstract class BaseController {

    public abstract Object get(String core);

    public abstract Object getById(String core, Long id);

    public abstract List<Object> getLst(String core);

    public abstract List<Object> getPage(String core, int page_num, int page_size);

    public abstract List<Object> getLstByParas(String core, int page_num, int page_size, String parameters);

    public abstract void update(String core, Object model);

    public abstract void update(String core, Long id, String fields);

    public abstract void delete(String core, Object model);

    public abstract void add(String core, Object model);
    
    public Boolean CheckCore(String core){
        XmlHelper xmlHelper = new XmlHelper();
        List<String> cores = xmlHelper.getAllCore();
        return cores.contains(core);
    }
}
