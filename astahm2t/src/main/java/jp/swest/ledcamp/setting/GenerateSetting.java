package jp.swest.ledcamp.setting;

import java.util.HashMap;
import jp.swest.ledcamp.setting.TemplateMap;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtext.xbase.lib.Pure;

@SuppressWarnings("all")
public class GenerateSetting {
  @Accessors
  private String templateID;
  
  @Accessors
  private String templateEngine;
  
  @Accessors
  private String targetPath;
  
  @Accessors
  private String templatePath;
  
  @Accessors
  private HashMap<String, TemplateMap> mapping;
  
  public GenerateSetting() {
    HashMap<String, TemplateMap> _hashMap = new HashMap<String, TemplateMap>();
    this.mapping = _hashMap;
  }
  
  @Pure
  public String getTemplateID() {
    return this.templateID;
  }
  
  public void setTemplateID(final String templateID) {
    this.templateID = templateID;
  }
  
  @Pure
  public String getTemplateEngine() {
    return this.templateEngine;
  }
  
  public void setTemplateEngine(final String templateEngine) {
    this.templateEngine = templateEngine;
  }
  
  @Pure
  public String getTargetPath() {
    return this.targetPath;
  }
  
  public void setTargetPath(final String targetPath) {
    this.targetPath = targetPath;
  }
  
  @Pure
  public String getTemplatePath() {
    return this.templatePath;
  }
  
  public void setTemplatePath(final String templatePath) {
    this.templatePath = templatePath;
  }
  
  @Pure
  public HashMap<String, TemplateMap> getMapping() {
    return this.mapping;
  }
  
  public void setMapping(final HashMap<String, TemplateMap> mapping) {
    this.mapping = mapping;
  }
}
