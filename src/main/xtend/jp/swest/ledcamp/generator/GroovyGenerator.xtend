package jp.swest.ledcamp.generator

import java.util.Map
import java.io.FileWriter
import groovy.text.SimpleTemplateEngine
import java.nio.file.Path

class GroovyGenerator implements ITemplateEngine {
    private SimpleTemplateEngine engine
    new(){
        engine = new SimpleTemplateEngine
    }
    
    override doGenerate(Map<String, Object> map, Path output, Path templateFile) {
        var template = engine.createTemplate(templateFile.toFile)
        var writer = new FileWriter(output.toFile)
        template.make(map).writeTo(writer)
        writer.close()
    }
}
