package jp.swest.ledcamp.setting;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import jp.swest.ledcamp.xtendhelper.Consumer;

@SuppressWarnings("all")
public class TextBinding implements DocumentListener {
  private Consumer<String> setterFunction;
  
  private JTextField field;
  
  public TextBinding(final JTextField field, final Consumer<String> setterFunction) {
    field.getDocument().addDocumentListener(this);
    this.field = field;
    this.setterFunction = setterFunction;
  }
  
  @Override
  public void changedUpdate(final DocumentEvent e) {
  }
  
  @Override
  public void insertUpdate(final DocumentEvent e) {
    this.setterFunction.accespt(this.field.getText());
  }
  
  @Override
  public void removeUpdate(final DocumentEvent e) {
    this.setterFunction.accespt(this.field.getText());
  }
}
