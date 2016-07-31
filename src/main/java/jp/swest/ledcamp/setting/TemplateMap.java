package jp.swest.ledcamp.setting;

import jp.swest.ledcamp.setting.TemplateType;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtext.xbase.lib.Pure;

@SuppressWarnings("all")
public class TemplateMap {
  @Accessors
  private String key;
  
  @Accessors
  private TemplateType templateType;
  
  @Accessors
  private String templateFile;
  
  @Accessors
  private String fileName;
  
  @Accessors
  private String fileExtension;
  
  @Accessors
  private String stereotype;
  
  @Accessors
  private String helper;
  
  public TemplateMap() {
  }
  
  public static TemplateMap newGlobalTemplateMap(final String templateFile, final String fileName) {
    final TemplateMap globalTemplateMap = new TemplateMap();
    globalTemplateMap.templateType = TemplateType.Global;
    globalTemplateMap.templateFile = templateFile;
    globalTemplateMap.fileName = fileName;
    return globalTemplateMap;
  }
  
  public static TemplateMap newDefaultTemplateMap(final String templateFile, final String fileExtension) {
    final TemplateMap defaultTemplateMap = new TemplateMap();
    defaultTemplateMap.templateType = TemplateType.Default;
    defaultTemplateMap.templateFile = templateFile;
    defaultTemplateMap.fileExtension = fileExtension;
    return defaultTemplateMap;
  }
  
  public static TemplateMap newStereotypeTemplateMap(final String templateFile, final String fileExtension, final String stereotype) {
    final TemplateMap stereotypeTemplateMap = new TemplateMap();
    stereotypeTemplateMap.templateType = TemplateType.Stereotype;
    stereotypeTemplateMap.stereotype = stereotype;
    stereotypeTemplateMap.templateFile = templateFile;
    stereotypeTemplateMap.fileExtension = fileExtension;
    return stereotypeTemplateMap;
  }
  
  public static TemplateMap newHelperTemplateMap(final String helperFile) {
    final TemplateMap helperMap = new TemplateMap();
    helperMap.helper = helperFile;
    return helperMap;
  }
  
  @Pure
  public String getKey() {
    return this.key;
  }
  
  public void setKey(final String key) {
    this.key = key;
  }
  
  @Pure
  public TemplateType getTemplateType() {
    return this.templateType;
  }
  
  public void setTemplateType(final TemplateType templateType) {
    this.templateType = templateType;
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
  
  @Pure
  public String getHelper() {
    return this.helper;
  }
  
  public void setHelper(final String helper) {
    this.helper = helper;
  }
}
