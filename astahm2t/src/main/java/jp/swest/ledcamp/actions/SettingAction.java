package jp.swest.ledcamp.actions;

import com.change_vision.jude.api.inf.ui.IPluginActionDelegate;
import com.change_vision.jude.api.inf.ui.IWindow;
import java.awt.Window;
import javax.swing.JOptionPane;
import jp.swest.ledcamp.view.GeneratorSettingDialog;

@SuppressWarnings("all")
public class SettingAction implements IPluginActionDelegate {
  public Object run(final IWindow window) throws IPluginActionDelegate.UnExpectedException {
    GeneratorSettingDialog dialog = new GeneratorSettingDialog();
    Window _parent = window.getParent();
    int result = JOptionPane.showConfirmDialog(_parent, dialog, "Generator Setting", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
    if ((result == JOptionPane.OK_OPTION)) {
    }
    return null;
  }
}
