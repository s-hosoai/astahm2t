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
        @Accessors TemplateType generateType // enum : stereotype, global, default
        @Accessors String templateFile
        @Accessors String fileName // if filename is null, {Classname}+fileExtension
        @Accessors String fileExtension
        @Accessors String stereotype
        new(){}
/*        new (String key, TemplateType generateType, String templateFile, String fileName, String fileExtension, String stereotype){
            this.key = key
            this.generateType = generateType
            this.templateFile = templateFile
            this.fileName = fileName
            this.fileExtension = fileExtension
            this.stereotype = stereotype
        }*/
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
    Groovy, Velocity
}
/* persist sample

<templates>
 <template name=id, engine=engine, targetPath=path, templatePath=path>
  <map key=key, generateType=type, templateFile=file, filename=name, fileExtension=name />
 </template>
<templates>
 */