package jp.swest.ledcamp.generator

import com.change_vision.jude.api.inf.AstahAPI
import com.change_vision.jude.api.inf.exception.InvalidUsingException
import com.change_vision.jude.api.inf.exception.ProjectNotFoundException
import com.change_vision.jude.api.inf.model.IClass
import com.change_vision.jude.api.inf.model.IStateMachine
import com.change_vision.jude.api.inf.model.IStateMachineDiagram
import java.awt.HeadlessException
import java.io.IOException
import java.util.ArrayList
import java.util.HashMap
import java.util.Hashtable
import javax.swing.JOptionPane
import jp.swest.ledcamp.setting.GenerateType
import jp.swest.ledcamp.setting.SettingManager

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
            for(mapping : setting.mapping.filter[k,v|v.generateType==GenerateType.Default].values){
                generator.doGenerate(map, setting.targetPath+iClass.name+"."+mapping.fileExtension, setting.templatePath+mapping.templateFile)
            }
        }
        JOptionPane.showMessageDialog(utils.frame, "Generate Finish")
    }
}
