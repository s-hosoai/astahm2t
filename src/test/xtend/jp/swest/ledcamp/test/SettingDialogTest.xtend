package jp.swest.ledcamp.test

import javax.swing.JFrame
import java.awt.BorderLayout
import javax.swing.JButton
import jp.swest.ledcamp.setting.SettingDialog
import jp.swest.ledcamp.setting.SettingManager
import jp.swest.ledcamp.setting.GenerateSetting
import jp.swest.ledcamp.setting.TemplateEngine
import jp.swest.ledcamp.setting.TemplateMap
import jp.swest.ledcamp.setting.TemplateType

class SettingDialogTest {
    def static void main(String[] args) {
    	
    	val manager = SettingManager.getInstance
    	val setting = new GenerateSetting
    	val map = new TemplateMap
    	manager.put("sample", setting)
    	setting.templateID = "sample"
    	setting.targetPath = "target path"
    	setting.templateEngine = TemplateEngine.Groovy
    	setting.templatePath = "template path"
    	setting.mapping.add(map)
    	map.fileExtension = "h"
    	map.templateType = TemplateType.Stereotype
    	map.key = "h"
    	map.stereotype = "MyStereotype"
    	map.templateFile = "header.template"
    	
        val frame = new JFrame()
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.setSize(500, 500)
        val layout = new BorderLayout
        val btn = new JButton("open dialog")
        btn.addActionListener
        [
            val dialog = new SettingDialog(frame)
            dialog.visible = true
        ]
        frame.add(btn, BorderLayout.CENTER)
        frame.pack
        frame.layout = layout
        frame.visible = true
    }    
}