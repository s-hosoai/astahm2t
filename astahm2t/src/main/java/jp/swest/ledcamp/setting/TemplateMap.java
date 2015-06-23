package jp.swest.ledcamp.setting;

import jp.swest.ledcamp.setting.TemplateType;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtext.xbase.lib.Pure;

@SuppressWarnings("all")
public class TemplateMap {
  @Accessors
  private String key;
  
  @Accessors
  private TemplateType generateType;
  
  @Accessors
  private String templateFile;
  
  @Accessors
  private String fileName;
  
  @Accessors
  private String fileExtension;
  
  @Accessors
  private String stereotype;
  
  public TemplateMap() {
  }
  
  /**
   * new (String key, TemplateType generateType, String templateFile, String fileName, String fileExtension, String stereotype){
   * this.key = key
   * this.generateType = generateType
   * this.templateFile = templateFile
   * this.fileName = fileName
   * this.fileExtension = fileExtension
   * this.stereotype = stereotype
   * }
   */
  public static TemplateMap newGlobalTemplateMap(final String templateFile, final String fileName) {
    final TemplateMap globalTemplateMap = new TemplateMap();
    globalTemplateMap.templateFile = templateFile;
    globalTemplateMap.fileName = fileName;
    return globalTemplateMap;
  }
  
  public static TemplateMap newDefaultTemplateMap(final String templateFile, final String fileExtension) {
    final TemplateMap globalTemplateMap = new TemplateMap();
    globalTemplateMap.templateFile = templateFile;
    globalTemplateMap.fileExtension = fileExtension;
    return globalTemplateMap;
  }
  
  public static TemplateMap newStereotypeTemplateMap(final String templateFile, final String fileExtension, final String stereotype) {
    final TemplateMap globalTemplateMap = new TemplateMap();
    globalTemplateMap.stereotype = stereotype;
    globalTemplateMap.templateFile = templateFile;
    globalTemplateMap.fileExtension = fileExtension;
    return globalTemplateMap;
  }
  
  @Pure
  public String getKey() {
    return this.key;
  }
  
  public void setKey(final String key) {
    this.key = key;
  }
  
  @Pure
  public TemplateType getGenerateType() {
    return this.generateType;
  }
  
  public void setGenerateType(final TemplateType generateType) {
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
  
  @Pure
  public String getStereotype() {
    return this.stereotype;
  }
  
  public void setStereotype(final String stereotype) {
    this.stereotype = stereotype;
  }
}
