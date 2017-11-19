package jp.swest.ledcamp.generator;

import java.nio.file.Path;
import java.util.Map;
import jp.swest.ledcamp.generator.ITemplateEngine;

@SuppressWarnings("all")
public class XtendGenerator implements ITemplateEngine {
  @Override
  public void doGenerate(final Map<String, Object> map, final Path output, final Path templateFile) {
  }
}
