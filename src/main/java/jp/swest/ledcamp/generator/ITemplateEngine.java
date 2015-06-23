package jp.swest.ledcamp.generator;

import java.util.Map;

@SuppressWarnings("all")
public interface ITemplateEngine {
  public abstract void doGenerate(final Map<String, Object> map, final String output, final String templateFile);
}
