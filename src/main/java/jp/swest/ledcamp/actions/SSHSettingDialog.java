package jp.swest.ledcamp.actions;

import com.google.common.base.Objects;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.Properties;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

@SuppressWarnings("all")
public class SSHSettingDialog extends JDialog {
  private boolean result = false;
  
  private JTextField host;
  
  private JTextField dest;
  
  private JTextField main;
  
  public SSHSettingDialog(final Properties properties) {
    this.setModal(true);
    final SSHSettingDialog dialog = this;
    JTextField _jTextField = new JTextField();
    this.host = _jTextField;
    Object _get = properties.get("host");
    boolean _notEquals = (!Objects.equal(_get, null));
    if (_notEquals) {
      Object _get_1 = properties.get("host");
      this.host.setText(((String) _get_1));
    }
    JTextField _jTextField_1 = new JTextField();
    this.dest = _jTextField_1;
    Object _get_2 = properties.get("dest");
    boolean _notEquals_1 = (!Objects.equal(_get_2, null));
    if (_notEquals_1) {
      Object _get_3 = properties.get("dest");
      this.dest.setText(((String) _get_3));
    }
    JTextField _jTextField_2 = new JTextField();
    this.main = _jTextField_2;
    Object _get_4 = properties.get("main");
    boolean _notEquals_2 = (!Objects.equal(_get_4, null));
    if (_notEquals_2) {
      Object _get_5 = properties.get("main");
      this.main.setText(((String) _get_5));
    }
    final GridBagLayout gridBagLayout = new GridBagLayout();
    Container _contentPane = this.getContentPane();
    _contentPane.setLayout(gridBagLayout);
    GridBagConstraints gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
    Insets _insets = new Insets(5, 5, 5, 5);
    gridBagConstraints.insets = _insets;
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.gridwidth = 5;
    Container _contentPane_1 = this.getContentPane();
    JLabel _jLabel = new JLabel("Raspberry Pi IP Address");
    _contentPane_1.add(_jLabel, gridBagConstraints);
    gridBagConstraints.gridy = 1;
    Container _contentPane_2 = this.getContentPane();
    _contentPane_2.add(this.host, gridBagConstraints);
    gridBagConstraints.gridy = 2;
    Container _contentPane_3 = this.getContentPane();
    JLabel _jLabel_1 = new JLabel("Destination Path");
    _contentPane_3.add(_jLabel_1, gridBagConstraints);
    gridBagConstraints.gridy = 3;
    Container _contentPane_4 = this.getContentPane();
    _contentPane_4.add(this.dest, gridBagConstraints);
    gridBagConstraints.gridy = 4;
    Container _contentPane_5 = this.getContentPane();
    JLabel _jLabel_2 = new JLabel("Main program file");
    _contentPane_5.add(_jLabel_2, gridBagConstraints);
    gridBagConstraints.gridy = 5;
    Container _contentPane_6 = this.getContentPane();
    _contentPane_6.add(this.main, gridBagConstraints);
    JButton okButton = new JButton("OK");
    okButton.addActionListener(new AbstractAction() {
      @Override
      public void actionPerformed(final ActionEvent e) {
        SSHSettingDialog.this.result = true;
        dialog.setVisible(false);
      }
    });
    JButton cancelButton = new JButton("Cancel");
    cancelButton.addActionListener(new AbstractAction() {
      @Override
      public void actionPerformed(final ActionEvent e) {
        SSHSettingDialog.this.result = false;
        dialog.dispose();
      }
    });
    gridBagConstraints.gridwidth = 1;
    gridBagConstraints.fill = GridBagConstraints.NONE;
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 6;
    Container _contentPane_7 = this.getContentPane();
    _contentPane_7.add(okButton, gridBagConstraints);
    gridBagConstraints.gridx = 2;
    Container _contentPane_8 = this.getContentPane();
    _contentPane_8.add(cancelButton, gridBagConstraints);
    this.pack();
  }
  
  public String getHost() {
    return this.host.getText();
  }
  
  public String getDest() {
    return this.dest.getText();
  }
  
  public String getMain() {
    return this.main.getText();
  }
  
  public boolean getResult() {
    return this.result;
  }
}
