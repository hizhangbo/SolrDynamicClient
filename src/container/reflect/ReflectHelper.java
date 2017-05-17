package container.reflect;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import net.sf.cglib.beans.BeanGenerator;
import net.sf.cglib.beans.BeanMap;

/**
 * Created by zhangbo on 2017/5/8.
 */
public class ReflectHelper {
	
	public Object object;

	private Map<String, Class<?>> propertyMap;
	
    public ReflectHelper(Map<String, Class<?>> propertyMap){
    	
    	this.propertyMap = propertyMap;
    }

    public Object getObject(){
    	Object newObj = generateBean(propertyMap);
    	this.object = newObj;
    	return newObj;
    }
    
    public BeanMap getBeanMap(){
    	return BeanMap.create(this.object);
    }
    
    public void setValue(String property, Object value){
        this.getBeanMap().put(property, value);
    }

//    public void setValue(Object object, String property, Object value){
//    	object = generateBean(propertyMap);
//        beanMap.put(property, value);
//    }

    public Object getValue(String property){
        return this.getBeanMap().get(property);
    }
    
    public Map<String, Object> getFieldValue(Map<String, Class<?>> propertyMap, LinkedHashMap<?, ?> object){
    	Map<String, Object> result = new HashMap<>();
        Set<String> keySet = propertyMap.keySet();
        for (Iterator<String> i = keySet.iterator(); i.hasNext();) {
            String key = (String) i.next();
            result.put(key, object.get(key));
        }
        return result;
    }
    
    private Object generateBean(Map<String, Class<?>> propertyMap) {
        BeanGenerator generator = new BeanGenerator();
        Set<String> keySet = propertyMap.keySet();
        for (Iterator<String> i = keySet.iterator(); i.hasNext();) {
            String key = (String) i.next();
            generator.addProperty(key, (Class<?>) propertyMap.get(key));
        }
        return generator.create();
    }

}
