package jp.swest.ledcamp.generator

import java.util.Map
import java.nio.file.Path

interface ITemplateEngine {
    def void doGenerate(Map<String, Object> map, Path output, Path templateFile)
}
