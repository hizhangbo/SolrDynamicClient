package container.xml;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import container.file.FileHelper;

public class XmlHelper {
	
	private String Core_Config_Path;

	public XmlHelper(){
		Core_Config_Path = this.getClass().getResource("/config/core/").getPath();
	}
	
	public List<String> getAllCore(){
		List<String> cores = new ArrayList<>();
        File file_dir = new File(Core_Config_Path);

        if(file_dir.exists()){
            File[] files = file_dir.listFiles();
            for (File file : files){
                if(!file.isDirectory()){
                	cores.add(file.getName());
                }
            }
        }

        return cores;
	}
	
	
	public File getConfig(String core){
        File file_dir = new File(Core_Config_Path);

        if(file_dir.exists()){
            File[] files = file_dir.listFiles();
            for (File file : files){
                if(!file.isDirectory() && file.getName().equals(core)){
                	return file;
                }
            }
        }
        return null;
		
	}

	
	public Document parse(File file) throws DocumentException{
        SAXReader reader = new SAXReader();
        Document document = reader.read(file);
        return document;
	}

	
	public List<Element> getNodesByName(Document doc, String name){
        Element root = doc.getRootElement();

        List<Element> elements = new ArrayList<>();
        for(Iterator<?> i = root.elementIterator(name); i.hasNext();){
            elements.add((Element) i.next());
        }
        return elements;
	}

	
	public Map<String, Class<?>> getProperties(List<Element> fields, List<Element> field_types) throws ClassNotFoundException{
        Map<String, Class<?>> properties_map = new HashMap<>();
        for (Element field : fields){
            for (Element type : field_types) {
                if(field.attribute("type").getValue().equals(type.attribute("name").getValue())){
                    properties_map.put(field.attribute("name").getValue(), Class.forName(type.attribute("value").getValue()));
                }
            }
        }
        return properties_map;
	}
}
