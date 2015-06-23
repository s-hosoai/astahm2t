package jp.swest.ledcamp.setting

import java.util.HashSet
import org.eclipse.xtend.lib.annotations.Accessors

class GenerateSetting {
    @Accessors String templateID
    @Accessors TemplateEngine templateEngine
    @Accessors String targetPath
    @Accessors String templatePath
    @Accessors HashSet<TemplateMap> mapping
    new(){
        mapping = new HashSet
    }
}
class TemplateMap {
        @Accessors String key
        @Accessors TemplateType templateType 
        @Accessors String templateFile
        @Accessors String fileName
        @Accessors String fileExtension
        @Accessors String stereotype
        new(){}
        static def newGlobalTemplateMap(String templateFile, String fileName){
            val globalTemplateMap = new TemplateMap
            globalTemplateMap.templateType = TemplateType.Global
            globalTemplateMap.templateFile = templateFile
            globalTemplateMap.fileName = fileName
            return globalTemplateMap
        }
        static def newDefaultTemplateMap(String templateFile, String fileExtension){
            val defaultTemplateMap = new TemplateMap
            defaultTemplateMap.templateType = TemplateType.Default
            defaultTemplateMap.templateFile = templateFile
            defaultTemplateMap.fileExtension = fileExtension
            return defaultTemplateMap
        }
        static def newStereotypeTemplateMap(String templateFile, String fileExtension, String stereotype){
            val stereotypeTemplateMap = new TemplateMap
            stereotypeTemplateMap.templateType = TemplateType.Stereotype
            stereotypeTemplateMap.stereotype = stereotype
            stereotypeTemplateMap.templateFile = templateFile
            stereotypeTemplateMap.fileExtension = fileExtension
            return stereotypeTemplateMap
        }
}

enum TemplateType{
    Stereotype, Default, Global
}
enum TemplateEngine{
    Groovy //, Velocity
}
