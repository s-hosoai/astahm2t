package jp.swest.ledcamp.actions

import com.change_vision.jude.api.inf.ui.IPluginActionDelegate
import com.change_vision.jude.api.inf.ui.IWindow
import com.change_vision.jude.api.inf.ui.IPluginActionDelegate.UnExpectedException
import jp.swest.ledcamp.setting.SettingDialog
import com.change_vision.jude.api.inf.AstahAPI

class SettingAction implements IPluginActionDelegate {
    override run(IWindow window) throws UnExpectedException {
        var dialog = new SettingDialog(AstahAPI.getAstahAPI.viewManager.mainFrame)
        dialog.visible = true
        return null
    }
}
