package jp.swest.ledcamp.generator;

import java.io.FileWriter;
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
  public void doGenerate(final Map<String, Object> mapping, final String output, final String templateFile) {
    try {
      Properties p = new Properties();
      p.setProperty("input.encoding", "UTF-8");
      p.setProperty("file.resource.loader.path", "C:/Users/hosoai/git/astahm2t-github/astahm2t/template/");
      Velocity.init(p);
      final VelocityContext ctx = new VelocityContext();
      final BiConsumer<String, Object> _function = new BiConsumer<String, Object>() {
        public void accept(final String k, final Object v) {
          ctx.put(k, v);
        }
      };
      mapping.forEach(_function);
      Template template = Velocity.getTemplate(templateFile);
      FileWriter writer = new FileWriter(output);
      template.merge(ctx, writer);
      writer.flush();
      writer.close();
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
