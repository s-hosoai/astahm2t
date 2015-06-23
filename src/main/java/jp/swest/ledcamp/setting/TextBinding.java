package jp.swest.ledcamp.setting;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import jp.swest.ledcamp.setting.Consumer;

@SuppressWarnings("all")
public class TextBinding implements DocumentListener {
  private Consumer setterFunction;
  
  private JTextField field;
  
  public TextBinding(final JTextField field, final Consumer setterFunction) {
    Document _document = field.getDocument();
    _document.addDocumentListener(this);
    this.field = field;
    this.setterFunction = setterFunction;
  }
  
  public void changedUpdate(final DocumentEvent e) {
  }
  
  public void insertUpdate(final DocumentEvent e) {
    String _text = this.field.getText();
    this.setterFunction.accespt(_text);
  }
  
  public void removeUpdate(final DocumentEvent e) {
    String _text = this.field.getText();
    this.setterFunction.accespt(_text);
  }
}
