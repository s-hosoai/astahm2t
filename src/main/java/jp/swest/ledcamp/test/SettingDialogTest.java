package jp.swest.ledcamp.test;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import javax.swing.JButton;
import javax.swing.JFrame;
import jp.swest.ledcamp.setting.GenerateSetting;
import jp.swest.ledcamp.setting.SettingDialog;
import jp.swest.ledcamp.setting.SettingManager;
import jp.swest.ledcamp.setting.TemplateEngine;
import jp.swest.ledcamp.setting.TemplateMap;
import jp.swest.ledcamp.setting.TemplateType;

@SuppressWarnings("all")
public class SettingDialogTest {
  public static void main(final String[] args) {
    final SettingManager manager = SettingManager.getInstance();
    final GenerateSetting setting = new GenerateSetting();
    final TemplateMap map = new TemplateMap();
    manager.put("sample", setting);
    setting.setTemplateID("sample");
    setting.setTargetPath("target path");
    setting.setTemplateEngine(TemplateEngine.Groovy);
    setting.setTemplatePath("template path");
    HashSet<TemplateMap> _mapping = setting.getMapping();
    _mapping.add(map);
    map.setFileExtension("h");
    map.setTemplateType(TemplateType.Stereotype);
    map.setKey("h");
    map.setStereotype("MyStereotype");
    map.setTemplateFile("header.template");
    final JFrame frame = new JFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(500, 500);
    final BorderLayout layout = new BorderLayout();
    final JButton btn = new JButton("open dialog");
    final ActionListener _function = new ActionListener() {
      public void actionPerformed(final ActionEvent it) {
        final SettingDialog dialog = new SettingDialog(frame);
        dialog.setVisible(true);
      }
    };
    btn.addActionListener(_function);
    frame.add(btn, BorderLayout.CENTER);
    frame.pack();
    frame.setLayout(layout);
    frame.setVisible(true);
  }
}
