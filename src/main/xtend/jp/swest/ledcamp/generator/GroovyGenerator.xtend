package jp.swest.ledcamp.generator

import java.util.Map
import java.io.FileWriter
import groovy.text.SimpleTemplateEngine
import java.io.File

class GroovyGenerator implements ITemplateEngine {
    private SimpleTemplateEngine engine
    new(){
        engine = new SimpleTemplateEngine
    }
    
    override doGenerate(Map<String, Object> map, String output, String templateFile) {
        var template = engine.createTemplate(new File(templateFile))
        var writer = new FileWriter(output)
        template.make(map).writeTo(writer)
        writer.close()
    }
}
