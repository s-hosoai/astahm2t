package jp.swest.ledcamp.generator;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Path;
import java.util.Map;
import java.util.Properties;
import java.util.function.BiConsumer;
import jp.swest.ledcamp.generator.ITemplateEngine;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.eclipse.xtext.xbase.lib.Exceptions;

@SuppressWarnings("all")
public class VelocityGenerator implements ITemplateEngine {
  @Override
  public void doGenerate(final Map<String, Object> mapping, final Path output, final Path templatePath) {
    try {
      Properties p = new Properties();
      p.setProperty("input.encoding", "UTF-8");
      Path _parent = templatePath.getParent();
      Path _absolutePath = _parent.toAbsolutePath();
      String _string = _absolutePath.toString();
      p.setProperty("file.resource.loader.path", _string);
      Velocity.init(p);
      final VelocityContext ctx = new VelocityContext();
      final BiConsumer<String, Object> _function = new BiConsumer<String, Object>() {
        @Override
        public void accept(final String k, final Object v) {
          ctx.put(k, v);
        }
      };
      mapping.forEach(_function);
      Path _fileName = templatePath.getFileName();
      String _string_1 = _fileName.toString();
      Template template = Velocity.getTemplate(_string_1);
      File _file = output.toFile();
      FileWriter writer = new FileWriter(_file);
      template.merge(ctx, writer);
      writer.flush();
      writer.close();
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
