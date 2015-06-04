package jp.swest.ledcamp.actions;

import java.awt.HeadlessException;
import java.io.IOException;

import javax.swing.JOptionPane;

import jp.swest.ledcamp.generator.CodeGenerator;
import jp.swest.ledcamp.setting.GeneratorType;

import com.change_vision.jude.api.inf.exception.InvalidUsingException;
import com.change_vision.jude.api.inf.exception.ProjectNotFoundException;
import com.change_vision.jude.api.inf.ui.IPluginActionDelegate;
import com.change_vision.jude.api.inf.ui.IWindow;

public class GenerateAction implements IPluginActionDelegate{
	@Override
	public Object run(IWindow window) throws UnExpectedException {
		GeneratorType type = null;
		try {
			CodeGenerator.generate();
		} catch (ClassNotFoundException e) {
			JOptionPane.showMessageDialog(window.getParent(), "Class Not Found Exception");
		} catch (ProjectNotFoundException e) {
			JOptionPane.showMessageDialog(window.getParent(), "Project Not Found Exception");
		} catch (IOException e) {
			JOptionPane.showMessageDialog(window.getParent(), "IO Exception");
		} catch (HeadlessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidUsingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
