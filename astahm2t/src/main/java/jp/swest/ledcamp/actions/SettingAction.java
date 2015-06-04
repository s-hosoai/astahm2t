package jp.swest.ledcamp.actions;

import javax.swing.JOptionPane;

import jp.swest.ledcamp.view.GeneratorSettingDialog;

import com.change_vision.jude.api.inf.ui.IPluginActionDelegate;
import com.change_vision.jude.api.inf.ui.IWindow;

public class SettingAction implements IPluginActionDelegate{
	@Override
	public Object run(IWindow window) throws UnExpectedException {
		GeneratorSettingDialog dialog = new GeneratorSettingDialog();
		int result = JOptionPane.showConfirmDialog(window.getParent(), dialog, "Generator Setting", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
		if(result==JOptionPane.OK_OPTION){
			// setting
		}
		return null;
	}
}
