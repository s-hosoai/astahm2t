package jp.swest.ledcamp.generator

import com.change_vision.jude.api.inf.exception.InvalidUsingException
import com.change_vision.jude.api.inf.exception.ProjectNotFoundException
import java.awt.HeadlessException
import java.io.IOException
import java.util.HashMap
import javax.swing.JOptionPane
import jp.swest.ledcamp.setting.SettingManager
import jp.swest.ledcamp.setting.TemplateType

class CodeGenerator {
    static def generate() throws ClassNotFoundException, ProjectNotFoundException, IOException, HeadlessException, InvalidUsingException {
        // code generate
        val generator = new GroovyGenerator
        val settingManager = SettingManager.getInstance
        val setting = settingManager.get("test")
        val map = new HashMap<String, Object>
        val utils = new GeneratorUtils
        for(iClass : utils.classes){
            utils.iclass = iClass
            utils.statemachine = utils.statemachines.get(iClass)
            println(utils.statemachine==null)
            map.put("u", utils)
            for(mapping : setting.mapping.filter[v|v.templateType==TemplateType::Default]){
                generator.doGenerate(map, setting.targetPath+iClass.name+"."+mapping.fileExtension, setting.templatePath+mapping.templateFile)
            }
        }
        JOptionPane.showMessageDialog(utils.frame, "Generate Finish")
    }
}
