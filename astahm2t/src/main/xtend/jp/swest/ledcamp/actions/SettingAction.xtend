package jp.swest.ledcamp.actions

import com.change_vision.jude.api.inf.ui.IPluginActionDelegate
import com.change_vision.jude.api.inf.ui.IWindow
import com.change_vision.jude.api.inf.ui.IPluginActionDelegate.UnExpectedException
import jp.swest.ledcamp.view.GeneratorSettingDialog
import javax.swing.JOptionPane

class SettingAction implements IPluginActionDelegate{
    override run(IWindow window) throws UnExpectedException {
        var dialog = new GeneratorSettingDialog
        var result = JOptionPane.showConfirmDialog(window.getParent(), dialog, "Generator Setting", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE)
        if(result==JOptionPane.OK_OPTION){
            // setting
        }
        return null;
    }
}