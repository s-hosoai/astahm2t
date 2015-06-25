package jp.swest.ledcamp.generator

import java.util.Map
import java.io.FileWriter
import groovy.text.SimpleTemplateEngine
import java.nio.file.Path
import static jp.swest.ledcamp.xtendhelper.Using.*

class GroovyGenerator {
    private SimpleTemplateEngine engine
    new(){
        engine = new SimpleTemplateEngine
    }
    
    def doGenerate(Map<String, Object> map, Path output, Path templateFile) throws Exception{
        val template = engine.createTemplate(templateFile.toFile)
        using(new FileWriter(output.toFile))[
            template.make(map).writeTo(it)
        ]
    }
}
