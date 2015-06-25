package jp.swest.ledcamp.generator;

import groovy.lang.Writable;
import groovy.text.SimpleTemplateEngine;
import groovy.text.Template;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.nio.file.Path;
import java.util.Map;
import jp.swest.ledcamp.xtendhelper.Using;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;

@SuppressWarnings("all")
public class GroovyGenerator {
  private SimpleTemplateEngine engine;
  
  public GroovyGenerator() {
    SimpleTemplateEngine _simpleTemplateEngine = new SimpleTemplateEngine();
    this.engine = _simpleTemplateEngine;
  }
  
  public Writer doGenerate(final Map<String, Object> map, final Path output, final Path templateFile) throws Exception {
    Writer _xblockexpression = null;
    {
      File _file = templateFile.toFile();
      final Template template = this.engine.createTemplate(_file);
      File _file_1 = output.toFile();
      FileWriter _fileWriter = new FileWriter(_file_1);
      final Function1<FileWriter, Writer> _function = new Function1<FileWriter, Writer>() {
        public Writer apply(final FileWriter it) {
          try {
            Writable _make = template.make(map);
            return _make.writeTo(it);
          } catch (Throwable _e) {
            throw Exceptions.sneakyThrow(_e);
          }
        }
      };
      _xblockexpression = Using.<FileWriter, Writer>using(_fileWriter, _function);
    }
    return _xblockexpression;
  }
}
