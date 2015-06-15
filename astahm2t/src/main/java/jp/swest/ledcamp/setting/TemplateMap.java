package jp.swest.ledcamp.setting;

import jp.swest.ledcamp.setting.GenerateType;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtext.xbase.lib.Pure;

@SuppressWarnings("all")
public class TemplateMap {
  @Accessors
  private String key;
  
  @Accessors
  private GenerateType generateType;
  
  @Accessors
  private String templateFile;
  
  @Accessors
  private String fileName;
  
  @Accessors
  private String fileExtension;
  
  public TemplateMap() {
  }
  
  public TemplateMap(final String key, final GenerateType generateType, final String templateFile, final String fileName, final String fileExtension) {
    this.key = key;
    this.generateType = generateType;
    this.templateFile = templateFile;
    this.fileName = fileName;
    this.fileExtension = fileExtension;
  }
  
  @Pure
  public String getKey() {
    return this.key;
  }
  
  public void setKey(final String key) {
    this.key = key;
  }
  
  @Pure
  public GenerateType getGenerateType() {
    return this.generateType;
  }
  
  public void setGenerateType(final GenerateType generateType) {
    this.generateType = generateType;
  }
  
  @Pure
  public String getTemplateFile() {
    return this.templateFile;
  }
  
  public void setTemplateFile(final String templateFile) {
    this.templateFile = templateFile;
  }
  
  @Pure
  public String getFileName() {
    return this.fileName;
  }
  
  public void setFileName(final String fileName) {
    this.fileName = fileName;
  }
  
  @Pure
  public String getFileExtension() {
    return this.fileExtension;
  }
  
  public void setFileExtension(final String fileExtension) {
    this.fileExtension = fileExtension;
  }
}
