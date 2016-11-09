package jp.swest.ledcamp.actions;

import com.change_vision.jude.api.inf.ui.IPluginActionDelegate;
import com.change_vision.jude.api.inf.ui.IWindow;
import java.awt.Window;
import java.util.List;
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
      Window _parent = window.getParent();
      JOptionPane.showMessageDialog(_parent, "Code Generation is Complete.");
      CodeGenerator.transferToCompilerServer();
    } catch (final Throwable _t) {
      if (_t instanceof GenerationException) {
        final GenerationException ge = (GenerationException)_t;
        Window _parent_1 = window.getParent();
        List<Exception> _excetpions = ge.getExcetpions();
        final Function1<Exception, String> _function = new Function1<Exception, String>() {
          @Override
          public String apply(final Exception e) {
            return e.getMessage();
          }
        };
        List<String> _map = ListExtensions.<Exception, String>map(_excetpions, _function);
        String _join = IterableExtensions.join(_map, "\n");
        String _plus = ("Code Generation is Failed.\n" + _join);
        JOptionPane.showMessageDialog(_parent_1, _plus);
        List<Exception> _excetpions_1 = ge.getExcetpions();
        final Consumer<Exception> _function_1 = new Consumer<Exception>() {
          @Override
          public void accept(final Exception e) {
            e.printStackTrace();
          }
        };
        _excetpions_1.forEach(_function_1);
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    }
    return null;
  }
}
