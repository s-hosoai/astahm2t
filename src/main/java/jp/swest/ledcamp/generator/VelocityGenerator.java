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
      p.setProperty("file.resource.loader.path", templatePath.getParent().toAbsolutePath().toString());
      Velocity.init(p);
      final VelocityContext ctx = new VelocityContext();
      final BiConsumer<String, Object> _function = (String k, Object v) -> {
        ctx.put(k, v);
      };
      mapping.forEach(_function);
      Template template = Velocity.getTemplate(templatePath.getFileName().toString());
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
