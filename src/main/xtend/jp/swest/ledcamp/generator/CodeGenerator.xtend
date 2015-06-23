package jp.swest.ledcamp.generator

import com.change_vision.jude.api.inf.exception.InvalidUsingException
import com.change_vision.jude.api.inf.exception.ProjectNotFoundException
import java.awt.HeadlessException
import java.io.IOException
import java.util.HashMap
import javax.swing.JOptionPane
import jp.swest.ledcamp.setting.SettingManager
import jp.swest.ledcamp.setting.TemplateType
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.LinkOption

class CodeGenerator {
    static def generate() throws ClassNotFoundException, ProjectNotFoundException, IOException, HeadlessException, InvalidUsingException {
        // code generate
        val generator = new GroovyGenerator
        val settingManager = SettingManager.getInstance
        val setting = settingManager.currentSetting
        val map = new HashMap<String, Object>
        val utils = new GeneratorUtils
        val templatePath = Paths.get(setting.templatePath)
        val targetPath = Paths.get(setting.targetPath)
        if(Files.exists(targetPath, LinkOption.NOFOLLOW_LINKS)){
            Files.createDirectories(targetPath)
        }
        for(iClass : utils.classes){
            utils.iclass = iClass
            utils.statemachine = utils.statemachines.get(iClass)
            map.put("u", utils)
            for(mapping : setting.mapping.filter[v|v.templateType==TemplateType::Default]){
                generator.doGenerate(map, targetPath.resolve(iClass.name+"."+mapping.fileExtension), templatePath.resolve(mapping.templateFile))
            }
        }
        for(mapping : setting.mapping.filter[v|v.templateType==TemplateType::Global]){
            map.put("u", utils)
            generator.doGenerate(map, targetPath.resolve(mapping.fileName), templatePath.resolve(mapping.templateFile))
        }
        JOptionPane.showMessageDialog(utils.frame, "Generate Finish")
    }
}
