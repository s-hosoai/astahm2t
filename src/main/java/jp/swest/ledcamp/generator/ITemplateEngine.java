package jp.swest.ledcamp.generator;

import java.nio.file.Path;
import java.util.Map;

@SuppressWarnings("all")
public interface ITemplateEngine {
  public abstract void doGenerate(final Map<String, Object> map, final Path output, final Path templateFile);
}
