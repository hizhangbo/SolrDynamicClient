package container.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileHelper {

	public static void PrintTextFile(String fileName, String text){
		try{
			File file = new File(fileName);
			if(!file.exists()){
				file.createNewFile();
			}
			
			FileWriter fw = new FileWriter(file.getName(), true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(text);
			bw.newLine();
			
			bw.close();
			fw.close();
		}catch(IOException ex){
			ex.printStackTrace();
		}
	}
	
}
