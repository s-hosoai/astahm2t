package jp.swest.ledcamp.setting

import java.util.HashMap
import org.eclipse.xtend.lib.annotations.Accessors

class GenerateSetting {
    @Accessors String templateID
    @Accessors String templateEngine
    @Accessors String targetPath
    @Accessors String templatePath
    @Accessors HashMap<String, TemplateMap> mapping
    new(){
        mapping = new HashMap
    }
    
}
class TemplateMap {
        @Accessors String key
        @Accessors GenerateType generateType // enum : stereotype, global, default
        @Accessors String templateFile
        @Accessors String fileName // if filename is null, {Classname}+fileExtension
        @Accessors String fileExtension
        new(){}
        new (String key, GenerateType generateType, String templateFile, String fileName, String fileExtension){
            this.key = key
            this.generateType = generateType
            this.templateFile = templateFile
            this.fileName = fileName
            this.fileExtension = fileExtension
        }
}

enum GenerateType{
    Stereotype, Default, Global
}
/* persist sample

<templates>
 <template name=id, engine=engine, targetPath=path, templatePath=path>
  <map key=key, generateType=type, templateFile=file, filename=name, fileExtension=name />
 </template>
<templates>
 */