package jp.swest.ledcamp.actions

import com.change_vision.jude.api.inf.ui.IPluginActionDelegate
import com.change_vision.jude.api.inf.ui.IWindow
import com.change_vision.jude.api.inf.ui.IPluginActionDelegate.UnExpectedException
import jp.swest.ledcamp.generator.CodeGenerator
import javax.swing.JOptionPane
import jp.swest.ledcamp.exception.GenerationException

class GenerateAction implements IPluginActionDelegate{
    
    override run(IWindow window) throws UnExpectedException {
        try{
        CodeGenerator::generate()
	        JOptionPane.showMessageDialog(window.parent, "Code Generation is Complete.")    
        }catch(GenerationException ge){
	        JOptionPane.showMessageDialog(window.parent, "Code Generation is Failed.\n"+ge.excetpions.map[e|e.message].join("\n"))
	        ge.excetpions.forEach[e|
	        	e.printStackTrace()
	        ]
        }
        return null
    }
}