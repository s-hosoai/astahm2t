package jp.swest.ledcamp.actions;

import com.change_vision.jude.api.inf.ui.IPluginActionDelegate;
import com.change_vision.jude.api.inf.ui.IWindow;
import java.awt.Window;
import java.util.function.Consumer;
import javax.swing.JOptionPane;
import jp.swest.ledcamp.exception.GenerationException;
import jp.swest.ledcamp.generator.CodeGenerator;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;

@SuppressWarnings("all")
public class GenerateAction implements IPluginActionDelegate {
  @Override
  public Object run(final IWindow window) throws IPluginActionDelegate.UnExpectedException {
    try {
      CodeGenerator.generate();
      JOptionPane.showMessageDialog(window.getParent(), "Code Generation is Complete.");
    } catch (final Throwable _t) {
      if (_t instanceof GenerationException) {
        final GenerationException ge = (GenerationException)_t;
        Window _parent = window.getParent();
        final Function1<Exception, String> _function = (Exception e) -> {
          return e.getMessage();
        };
        String _join = IterableExtensions.join(ListExtensions.<Exception, String>map(ge.getExcetpions(), _function), "\n");
        String _plus = ("Code Generation is Failed.\n" + _join);
        JOptionPane.showMessageDialog(_parent, _plus);
        final Consumer<Exception> _function_1 = (Exception e) -> {
          e.printStackTrace();
        };
        ge.getExcetpions().forEach(_function_1);
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    }
    return null;
  }
}
