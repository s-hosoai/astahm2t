package jp.swest.ledcamp.test;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import jp.swest.ledcamp.setting.SettingDialog;
import jp.swest.ledcamp.setting.SettingManager;

@SuppressWarnings("all")
public class SettingDialogTest {
  public static void main(final String[] args) {
    final SettingManager manager = SettingManager.getInstance();
    manager.load();
    final JFrame frame = new JFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(500, 500);
    final BorderLayout layout = new BorderLayout();
    final JButton btn = new JButton("open dialog");
    final ActionListener _function = (ActionEvent it) -> {
      final SettingDialog dialog = new SettingDialog(frame);
      dialog.setVisible(true);
    };
    btn.addActionListener(_function);
    frame.add(btn, BorderLayout.CENTER);
    frame.pack();
    frame.setLayout(layout);
    frame.setVisible(true);
  }
}
