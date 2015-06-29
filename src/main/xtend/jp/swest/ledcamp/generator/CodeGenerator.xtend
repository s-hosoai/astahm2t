package jp.swest.ledcamp.generator

import com.change_vision.jude.api.inf.exception.InvalidUsingException
import com.change_vision.jude.api.inf.exception.ProjectNotFoundException
import java.awt.HeadlessException
import java.io.IOException
import java.util.HashMap
import javax.swing.JOptionPane
import jp.swest.ledcamp.setting.SettingManager
import jp.swest.ledcamp.setting.TemplateType
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.LinkOption
import groovy.util.ScriptException
import groovy.lang.MissingPropertyException

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
        if (!Files.exists(targetPath, LinkOption.NOFOLLOW_LINKS)) {
            Files.createDirectories(targetPath)
        }
        for (iClass : utils.classes) {
            utils.iclass = iClass
            utils.statemachine = utils.statemachines.get(iClass)
            map.put("u", utils)
            if (iClass.stereotypes.size == 0) { // Defualt generate
                for (mapping : setting.mapping.filter[it.templateType == TemplateType::Default]) {
                    try {
                        generator.doGenerate(map, targetPath.resolve(iClass.name + "." + mapping.fileExtension),
                            templatePath.resolve(mapping.templateFile))
                    } catch (Exception e) {
                        switch (e) {
                            case MissingPropertyException:
                                JOptionPane.showMessageDialog(utils.frame,
                                    "Cannot found property :" + e.message + ". model : " + iClass.name)
                            default:
                                JOptionPane.showMessageDialog(utils.frame,
                                    e.message + ".\n in model : " + iClass.name)
                        }
                        return
                    }
                }
            } else { // stereotype Generate
                for (stereotype : iClass.stereotypes) {
                    for (mapping : setting.mapping.filter[it.templateType == TemplateType::Stereotype && it.stereotype.equals(stereotype)]) {
                        try {
                            generator.doGenerate(map, targetPath.resolve(iClass.name + "." + mapping.fileExtension),
                                templatePath.resolve(mapping.templateFile))
                        } catch (Exception e) {
                            switch (e) {
                                case MissingPropertyException:
                                    JOptionPane.showMessageDialog(utils.frame,
                                        "Cannot found property :" + e.message + ". model : " + iClass.name)
                                default:
                                    JOptionPane.showMessageDialog(utils.frame,
                                        e.message + ".\n in model : " + iClass.name)
                            }
                            return
                        }
                    }
                }
            }
        }
        for (mapping : setting.mapping.filter[v|v.templateType == TemplateType::Global]) {
            map.put("u", utils)
            generator.doGenerate(map, targetPath.resolve(mapping.fileName), templatePath.resolve(mapping.templateFile))
        }
        JOptionPane.showMessageDialog(utils.frame, "Generate Finish")
    }
}
