package jp.swest.ledcamp.generator

import java.util.Map
import java.io.FileWriter
import groovy.text.SimpleTemplateEngine
import java.nio.file.Path
import static jp.swest.ledcamp.xtendhelper.Using.*
import groovy.lang.MissingPropertyException
import javax.swing.JOptionPane
import jp.swest.ledcamp.exception.GenerationException

class GroovyGenerator implements ITemplateEngine {
    private SimpleTemplateEngine engine

    new() {
        engine = new SimpleTemplateEngine
    }

    override doGenerate(Map<String, Object> map, Path output, Path templateFile) {
        val utils = map.get('u') as GeneratorUtils
        val iClass = utils.iclass
        try {
            val template = engine.createTemplate(templateFile.toFile)
            val generatedCode = template.make(map)
            using(new FileWriter(output.toFile)) [
                generatedCode.writeTo(it)
            ]

        } catch (Exception e) {
            GenerationException::getInstance().addException(e)
            switch (e) {
                case MissingPropertyException:
                    JOptionPane.showMessageDialog(utils.frame,
                        "Cannot found property :" + e.message + ". model : " + iClass.name)
                default:
                    JOptionPane.showMessageDialog(utils.frame, e.message + ".\n in model : " + iClass.name)
            }
        }
    }
}
