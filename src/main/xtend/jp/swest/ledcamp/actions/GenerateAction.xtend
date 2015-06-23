package jp.swest.ledcamp.actions

import com.change_vision.jude.api.inf.ui.IPluginActionDelegate
import com.change_vision.jude.api.inf.ui.IWindow
import com.change_vision.jude.api.inf.ui.IPluginActionDelegate.UnExpectedException
import jp.swest.ledcamp.generator.CodeGenerator

class GenerateAction implements IPluginActionDelegate{
    
    override run(IWindow arg0) throws UnExpectedException {
        CodeGenerator::generate()
        return null
    }
}