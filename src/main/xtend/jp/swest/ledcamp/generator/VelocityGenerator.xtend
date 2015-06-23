package jp.swest.ledcamp.generator

import java.util.Map
import java.util.Properties
import org.apache.velocity.app.Velocity
import org.apache.velocity.VelocityContext
import java.io.FileWriter
import java.nio.file.Path

class VelocityGenerator implements ITemplateEngine {
    override doGenerate(Map<String, Object> mapping, Path output, Path templatePath) {
        var p = new Properties()
        p.setProperty("input.encoding", "UTF-8")
        p.setProperty("file.resource.loader.path", templatePath.parent.toAbsolutePath.toString)
        Velocity.init(p)
        val ctx = new VelocityContext()
        mapping.forEach[k,v|ctx.put(k,v)]

        var template = Velocity.getTemplate(templatePath.fileName.toString)
        var writer = new FileWriter(output.toFile)
        template.merge(ctx, writer)
        writer.flush()
        writer.close()
    }
}
