package jp.swest.ledcamp.sandbox;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.Properties;

import jp.swest.ledcamp.setting.FileMapping;
import jp.swest.ledcamp.setting.GeneratorType;
import jp.swest.ledcamp.setting.SettingManager_;

public class AccessProperties {
	public static void main(String[] args) throws Exception {
		AccessProperties main = new AccessProperties();
//		main.run();
		
		SettingManager_ manager = SettingManager_.getInstance();
		String userFolder = System.getProperty("user.home");
		String m2tPluginFolderPath = userFolder+"/.astah/plugins/m2t/";
		manager.addGeneratorType(new GeneratorType("ArduinoCreate", "ModelCollector.groovy", "FileGenerator.groovy"));
		manager.addGeneratorType(new GeneratorType("Java",  "ModelCollector.groovy", "FileGenerator.groovy"));
		manager.addGeneratorType(new GeneratorType("C++", "ModelCollector.groovy", "FileGenerator.groovy"));
		manager.addTemplate(new FileMapping("cpp", "cpp","cpp.template"));
		manager.addTemplate(new FileMapping("arduino", "ino","arduino.template"));
		manager.addTemplate(new FileMapping("header", "h","header.template"));
		manager.saveProperties();
	}
	
	void run() throws FileNotFoundException, IOException{		
		String userFolder = System.getProperty("user.home");
		String m2tPluginFolderPath = userFolder+"/.astah/plugins/m2t/";
		String propertiesFile = m2tPluginFolderPath+"m2t.properties";
		
		File asgenPluginFolder = new File(m2tPluginFolderPath+"templates");
		if(!asgenPluginFolder.exists()){
			System.out.println("folder not found. create it!");
			if(!asgenPluginFolder.mkdirs()){
				throw new IOException(); //can not create folder;
			}
		}
		
		Properties properties = new Properties();
		if(new File(propertiesFile).exists()){
			properties.load(new FileInputStream(m2tPluginFolderPath + propertiesFile));			
		}
		
		String scriptPath = (String)properties.getProperty("ScriptPath", m2tPluginFolderPath);
		String modelCollector = (String)properties.getProperty("ModelCollector", "ModelCollector.groovy");
		String fileGenerator = (String)properties.getProperty("FileGenerator", "FileGenerator.groovy");
		if(!new File(propertiesFile).exists()){
			properties.setProperty("ScriptPath", scriptPath);
			properties.getProperty("ModelCollector", modelCollector);
			properties.getProperty("FileGenerator", fileGenerator);
		}
		properties.store(new FileOutputStream(propertiesFile), "settings");
		
		for(Entry<Object,Object> prop : properties.entrySet()){
			System.out.println(prop.getKey()+"::"+prop.getValue());
		}
	}
}
