package jp.swest.ledcamp.actions;

import com.change_vision.jude.api.inf.AstahAPI;
import com.change_vision.jude.api.inf.ui.IPluginActionDelegate;
import com.change_vision.jude.api.inf.ui.IWindow;
import javax.swing.JFrame;
import jp.swest.ledcamp.setting.SettingDialog;
import org.eclipse.xtext.xbase.lib.Exceptions;

@SuppressWarnings("all")
public class SettingAction implements IPluginActionDelegate {
  @Override
  public Object run(final IWindow window) throws IPluginActionDelegate.UnExpectedException {
    try {
      JFrame _mainFrame = AstahAPI.getAstahAPI().getViewManager().getMainFrame();
      SettingDialog dialog = new SettingDialog(_mainFrame);
      dialog.setVisible(true);
      return null;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
