package jp.swest.ledcamp.actions;

import com.change_vision.jude.api.inf.ui.IPluginActionDelegate;
import com.change_vision.jude.api.inf.ui.IWindow;
import jp.swest.ledcamp.generator.CodeGenerator;
import org.eclipse.xtext.xbase.lib.Exceptions;

@SuppressWarnings("all")
public class GenerateAction implements IPluginActionDelegate {
  public Object run(final IWindow arg0) throws IPluginActionDelegate.UnExpectedException {
    try {
      CodeGenerator.generate();
      return null;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
