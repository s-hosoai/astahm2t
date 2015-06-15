package jp.swest.ledcamp.generator

import java.util.Map
import java.util.Properties
import org.apache.velocity.app.Velocity
import org.apache.velocity.VelocityContext
import java.io.FileWriter

class VelocityGenerator implements ITemplateEngine {
    override doGenerate(Map<String, Object> mapping, String output, String templateFile) {
        var p = new Properties()
        p.setProperty("input.encoding", "UTF-8")
        p.setProperty("file.resource.loader.path", "C:/Users/hosoai/git/astahm2t-github/astahm2t/template/")
        Velocity.init(p)
        val ctx = new VelocityContext()
        mapping.forEach[k,v|ctx.put(k,v)]

        var template = Velocity.getTemplate(templateFile)
        var writer = new FileWriter(output)
        template.merge(ctx, writer)
        writer.flush()
        writer.close()
    }
}
