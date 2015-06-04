package jp.swest.ledcamp.setting;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class SettingManager {
	private final String generatorPrefix = "generator_";
	private final String templatePrefix = "template_";
	private final String targetDirKey = "targetDir";
	private final String currentProjectKey = "currentProject";
		
	private static SettingManager instance; // singleton
	private Properties properties;
	private String m2tPluginFolderPath;
	private String propertiesFile;
	
//	private String scriptPath;
//	private String modelCollector;
//	private String fileGenerator;
//	private HashMap<String, GeneratorType> generators;
//	private HashMap<String, FileMapping> templates;
	
	public static SettingManager getInstance(){
		if(instance==null){
			instance = new SettingManager();
		}
		return instance;
	}
	
	private SettingManager(){
		String userFolder = System.getProperty("user.home");
		m2tPluginFolderPath = userFolder+"/.astah/plugins/m2t/";
		propertiesFile = m2tPluginFolderPath+"m2t.properties";
		File asgenPluginFolder = new File(m2tPluginFolderPath);
		if(!asgenPluginFolder.exists()){
			try{
				asgenPluginFolder.getParentFile().mkdirs();
				File zipFile = File.createTempFile("astahm2t", "templeatezip");
				BufferedInputStream bis = null;
				BufferedOutputStream bos = null;
				try{
					bis  = new BufferedInputStream(getClass().getClassLoader().getResourceAsStream("m2t.zip"));
					bos = new BufferedOutputStream(new FileOutputStream(zipFile));
					writeFile(bis, bos);
				}finally{
					bis.close();
					bos.close();
				}
				unzip(new ZipFile(zipFile), asgenPluginFolder.getParent());
			}catch(IOException ioe){
				System.err.println("cannot create template folders.");
			}
		}

		properties = new Properties();
		if(new File(propertiesFile).exists()){
			try {
				properties.load(new FileInputStream(propertiesFile));
			} catch (FileNotFoundException e) {
				System.err.println("load property: cannot found properties file");
			} catch (IOException e) {
				System.err.println("load property: IOException");
			}			
		}else{ // not found properties file, create it;
			saveProperties();
		}		
	}
	
	public void saveProperties(){
		try {
			properties.store(new FileOutputStream(propertiesFile), "setting file");
		} catch (FileNotFoundException e) {
			System.err.println("save Property : cannot found properties file");
		} catch (IOException e) {
			System.err.println("save Property : IOException in save properties file");
		}			
	}

/*
 * Getters and Setters
 */
	public GeneratorType getGeneratorType(String type){
		return new GeneratorType(properties.getProperty(generatorPrefix+type));
//		return generators.get(type);
	}
	public void addGeneratorType(GeneratorType type){
//		generators.put(type.getName(), type);
		properties.put(generatorPrefix+type.getName(), type.toString());
	}
	public Set<String> getGeneratorTypeNames(){
		Set<String> result = new HashSet<String>();
		for(Object o: properties.keySet()){
			if(o instanceof String){
				String key = (String)o;
				if(key.startsWith(generatorPrefix)){
					result.add(key.substring(key.indexOf('_')+1));
				}
			}
		}
		return result;
	}

	public Set<String> getTemplatesNames(){
		properties.keySet();
		HashSet<String> keys = new HashSet<String>();
		for(Object keyObj: properties.keySet()){
			String key = (String)keyObj;
			if(key.startsWith(templatePrefix)){
				keys.add(key.substring(key.indexOf('_')+1));
			}
		}
		return keys;
	}
	
	public FileMapping getTemplate(String key) {
		return new FileMapping(properties.getProperty(templatePrefix+key));
	}

	public void addTemplate(FileMapping mapping){
		properties.put(templatePrefix+mapping.getName(), mapping.toString());
	}
	
	public String getTargetDirectory(){
		return properties.getProperty(targetDirKey);
	}
	public void setTargetDirectory(String dir){
		properties.setProperty(targetDirKey, dir);
	}
	public String getCurrentProject(){
		return properties.getProperty(currentProjectKey);
	}
	public void setCurrentProject(String dir){
		properties.setProperty(currentProjectKey, dir);
	}
	
	public String getScriptPath(){
		return m2tPluginFolderPath;
	}
	public String getTemplatePath(){
		return m2tPluginFolderPath+"/templates/";
	}
	
	private void unzip(ZipFile zipFile, String path) throws IOException{
		Enumeration<? extends ZipEntry> entries = zipFile.entries();
		while(entries.hasMoreElements()){
			ZipEntry entry = entries.nextElement();
			File extractFile = new File(path, entry.getName());
			if(entry.isDirectory()){
				extractFile.mkdirs();
			}else{
				BufferedInputStream bis = null;
				BufferedOutputStream bos = null;
				try{
					bis = new BufferedInputStream(zipFile.getInputStream(entry));
					bos = new BufferedOutputStream(new FileOutputStream(extractFile));
					writeFile(bis, bos);
				}finally{
					if(bis!=null){bis.close();}
					if(bos!=null){bos.close();}
				}
			}
		}
	}
	
	private void writeFile(BufferedInputStream bis, BufferedOutputStream bos) throws IOException{
		int available;
		while((available = bis.available())>0){
			byte[] bs = new byte[available];
			bis.read(bs);
			bos.write(bs);
		}		
	}
}
