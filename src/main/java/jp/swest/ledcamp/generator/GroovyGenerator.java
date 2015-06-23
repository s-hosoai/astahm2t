package jp.swest.ledcamp.generator;

import groovy.lang.Writable;
import groovy.text.SimpleTemplateEngine;
import groovy.text.Template;
import java.io.File;
import java.io.FileWriter;
import java.util.Map;
import jp.swest.ledcamp.generator.ITemplateEngine;
import org.eclipse.xtext.xbase.lib.Exceptions;

@SuppressWarnings("all")
public class GroovyGenerator implements ITemplateEngine {
  private SimpleTemplateEngine engine;
  
  public GroovyGenerator() {
    SimpleTemplateEngine _simpleTemplateEngine = new SimpleTemplateEngine();
    this.engine = _simpleTemplateEngine;
  }
  
  public void doGenerate(final Map<String, Object> map, final String output, final String templateFile) {
    try {
      File _file = new File(templateFile);
      Template template = this.engine.createTemplate(_file);
      FileWriter writer = new FileWriter(output);
      Writable _make = template.make(map);
      _make.writeTo(writer);
      writer.close();
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
