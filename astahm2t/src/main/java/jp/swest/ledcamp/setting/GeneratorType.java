package jp.swest.ledcamp.setting;

import java.util.HashSet;
import java.util.Set;

public class GeneratorType {
	private String name;
	private String modelCollectorPath;
	private String fileGeneratorPath;
	private Set<String> templates;

	public GeneratorType(){
		templates = new HashSet<String>();
	}
	public GeneratorType(String name, String modelCollectorPath, String fileGeneratorPath){
		this();
		this.name = name;
		this.modelCollectorPath = modelCollectorPath;
		this.fileGeneratorPath = fileGeneratorPath;
	}
	public GeneratorType(String serialize){
		this();
		String[] temp= serialize.split(",");
		this.name = temp[0];
		this.modelCollectorPath = temp[1];
		this.fileGeneratorPath = temp[2];
		for(int i=4;i<temp.length;i++){
			this.templates.add(temp[i]);
		}
	}
	@Override
	public String toString(){
		String result = this.name+","+this.modelCollectorPath+","+this.fileGeneratorPath;
		for(String template : templates){
			result=result+","+template;
		}
		return result;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getModelCollectorPath() {
		return modelCollectorPath;
	}
	public void setModelCollectorPath(String modelCollectorPath) {
		this.modelCollectorPath = modelCollectorPath;
	}
	public String getFileGeneratorPath() {
		return fileGeneratorPath;
	}
	public void setFileGeneratorPath(String fileGeneratorPath) {
		this.fileGeneratorPath = fileGeneratorPath;
	}
	
	public Set<String> getTemplates() {
		return templates;
	}
	public void addTemplate(String template) {
		this.templates.add(template);
	}
}
