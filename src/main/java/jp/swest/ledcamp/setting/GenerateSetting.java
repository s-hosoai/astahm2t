package jp.swest.ledcamp.setting;

import java.util.HashSet;
import jp.swest.ledcamp.setting.TemplateEngine;
import jp.swest.ledcamp.setting.TemplateMap;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtext.xbase.lib.Pure;

@SuppressWarnings("all")
public class GenerateSetting {
  @Accessors
  private String templateID;
  
  @Accessors
  private TemplateEngine templateEngine;
  
  @Accessors
  private String targetPath;
  
  @Accessors
  private String templatePath;
  
  @Accessors
  private HashSet<TemplateMap> mapping;
  
  public GenerateSetting() {
    HashSet<TemplateMap> _hashSet = new HashSet<TemplateMap>();
    this.mapping = _hashSet;
  }
  
  @Pure
  public String getTemplateID() {
    return this.templateID;
  }
  
  public void setTemplateID(final String templateID) {
    this.templateID = templateID;
  }
  
  @Pure
  public TemplateEngine getTemplateEngine() {
    return this.templateEngine;
  }
  
  public void setTemplateEngine(final TemplateEngine templateEngine) {
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
  public HashSet<TemplateMap> getMapping() {
    return this.mapping;
  }
  
  public void setMapping(final HashSet<TemplateMap> mapping) {
    this.mapping = mapping;
  }
}
