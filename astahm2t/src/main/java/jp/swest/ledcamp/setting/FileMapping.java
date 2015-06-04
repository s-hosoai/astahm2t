package jp.swest.ledcamp.setting;

public class FileMapping {
	private String name;
	private String ext;
	private String path;
	private String description;
	public FileMapping(String name, String ext, String path){
		this.name = name;
		this.ext = ext;
		this.path = path;
	}
	public FileMapping(String serialize){
		String[] temp= serialize.split(",");
		this.name = temp[0];
		this.ext = temp[1];
		this.path = temp[2];
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getExt() {
		return ext;
	}
	public void setExt(String ext) {
		this.ext = ext;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String toString(){
		return this.name+","+this.ext+","+this.path;
	}
}
