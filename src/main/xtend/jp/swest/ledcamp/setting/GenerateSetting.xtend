package jp.swest.ledcamp.setting

import java.util.HashMap
import org.eclipse.xtend.lib.annotations.Accessors

class GenerateSetting {
    @Accessors String templateID
    @Accessors TemplateEngine templateEngine
    @Accessors String targetPath
    @Accessors String templatePath
    @Accessors HashMap<String, TemplateMap> mapping
    new(){
        mapping = new HashMap
    }
}
class TemplateMap {
        @Accessors String key
        @Accessors TemplateType generateType 
        @Accessors String templateFile
        @Accessors String fileName
        @Accessors String fileExtension
        @Accessors String stereotype
        new(){}
        static def newGlobalTemplateMap(String templateFile, String fileName){
            val globalTemplateMap = new TemplateMap
            globalTemplateMap.templateFile = templateFile
            globalTemplateMap.fileName = fileName
            return globalTemplateMap
        }
        static def newDefaultTemplateMap(String templateFile, String fileExtension){
            val globalTemplateMap = new TemplateMap
            globalTemplateMap.templateFile = templateFile
            globalTemplateMap.fileExtension = fileExtension
            return globalTemplateMap
        }
        static def newStereotypeTemplateMap(String templateFile, String fileExtension, String stereotype){
            val globalTemplateMap = new TemplateMap
            globalTemplateMap.stereotype = stereotype
            globalTemplateMap.templateFile = templateFile
            globalTemplateMap.fileExtension = fileExtension
            return globalTemplateMap
        }
}

enum TemplateType{
    Stereotype, Default, Global
}
enum TemplateEngine{
    Groovy //, Velocity
}
