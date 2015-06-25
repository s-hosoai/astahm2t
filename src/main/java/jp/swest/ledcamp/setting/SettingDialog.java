package jp.swest.ledcamp.setting;

import com.google.common.base.Objects;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.function.Consumer;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import jp.swest.ledcamp.setting.GenerateSetting;
import jp.swest.ledcamp.setting.SettingManager;
import jp.swest.ledcamp.setting.TemplateEngine;
import jp.swest.ledcamp.setting.TemplateMap;
import jp.swest.ledcamp.setting.TemplateType;
import jp.swest.ledcamp.setting.TextBinding;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.StringExtensions;

@SuppressWarnings("all")
public class SettingDialog extends JDialog {
  public static class TemplatePanel extends JPanel {
    private SettingDialog settingDialog;
    
    private TemplateMap map;
    
    private JComboBox<TemplateType> comboType;
    
    private JPanel cardPane;
    
    private CardLayout typeCardLayout;
    
    private JTextField templateFile_G;
    
    private JTextField templateFile_D;
    
    private JTextField templateFile_S;
    
    private JTextField fileName;
    
    private JTextField fileExtension_D;
    
    private JTextField fileExtension_S;
    
    private JTextField stereotype;
    
    public TemplatePanel(final SettingDialog settingDialog, final TemplateMap map) {
      this.settingDialog = settingDialog;
      this.map = map;
      this.initComponent(settingDialog);
      TemplateType _templateType = map.getTemplateType();
      boolean _notEquals = (!Objects.equal(_templateType, null));
      if (_notEquals) {
        TemplateType _templateType_1 = map.getTemplateType();
        this.comboType.setSelectedItem(_templateType_1);
      } else {
        this.comboType.setSelectedItem(TemplateType.Default);
      }
      TemplateType _templateType_2 = map.getTemplateType();
      if (_templateType_2 != null) {
        switch (_templateType_2) {
          case Global:
            String _templateFile = map.getTemplateFile();
            this.setField(this.templateFile_G, _templateFile);
            String _fileName = map.getFileName();
            this.setField(this.fileName, _fileName);
            break;
          case Default:
            String _templateFile_1 = map.getTemplateFile();
            this.setField(this.templateFile_D, _templateFile_1);
            String _fileExtension = map.getFileExtension();
            this.setField(this.fileExtension_D, _fileExtension);
            break;
          case Stereotype:
            String _templateFile_2 = map.getTemplateFile();
            this.setField(this.templateFile_S, _templateFile_2);
            String _fileExtension_1 = map.getFileExtension();
            this.setField(this.fileExtension_S, _fileExtension_1);
            String _stereotype = map.getStereotype();
            this.setField(this.stereotype, _stereotype);
            break;
          default:
            InputOutput.<String>println("select default");
            break;
        }
      } else {
        InputOutput.<String>println("select default");
      }
    }
    
    public Dimension getPreferredSize() {
      return new Dimension((this.settingDialog.templatePanel.getSize().width - 10), 30);
    }
    
    public Dimension getMaximumSize() {
      Dimension _size = this.settingDialog.templatePanel.getSize();
      return new Dimension(_size.width, 30);
    }
    
    public Dimension getMinimumSize() {
      JPanel _templatePanel = null;
      if (this.settingDialog!=null) {
        _templatePanel=this.settingDialog.templatePanel;
      }
      Dimension _size = null;
      if (_templatePanel!=null) {
        _size=_templatePanel.getSize();
      }
      return new Dimension(_size.width, 30);
    }
    
    public void initComponent(final SettingDialog settingDialog) {
      BorderLayout _borderLayout = new BorderLayout();
      this.setLayout(_borderLayout);
      {
        JComboBox<TemplateType> _jComboBox = new JComboBox<TemplateType>();
        this.comboType = _jComboBox;
        CardLayout _cardLayout = new CardLayout();
        this.typeCardLayout = _cardLayout;
        TemplateType[] _values = TemplateType.values();
        final Consumer<TemplateType> _function = new Consumer<TemplateType>() {
          public void accept(final TemplateType it) {
            TemplatePanel.this.comboType.addItem(it);
          }
        };
        ((List<TemplateType>)Conversions.doWrapArray(_values)).forEach(_function);
        final ActionListener _function_1 = new ActionListener() {
          public void actionPerformed(final ActionEvent it) {
            int _selectedIndex = TemplatePanel.this.comboType.getSelectedIndex();
            boolean _notEquals = (_selectedIndex != (-1));
            if (_notEquals) {
              int _selectedIndex_1 = TemplatePanel.this.comboType.getSelectedIndex();
              final TemplateType item = TemplatePanel.this.comboType.getItemAt(_selectedIndex_1);
              String _name = item.name();
              TemplatePanel.this.typeCardLayout.show(TemplatePanel.this.cardPane, _name);
              TemplatePanel.this.map.setTemplateType(item);
            }
          }
        };
        this.comboType.addActionListener(_function_1);
        this.add(this.comboType, BorderLayout.WEST);
      }
      {
        JPanel _jPanel = new JPanel();
        this.cardPane = _jPanel;
        this.cardPane.setLayout(this.typeCardLayout);
        this.add(this.cardPane, BorderLayout.CENTER);
        {
          final JPanel globalCard = new JPanel();
          GridLayout _gridLayout = new GridLayout(1, 2);
          globalCard.setLayout(_gridLayout);
          String _name = TemplateType.Global.name();
          this.cardPane.add(globalCard, _name);
          {
            JTextField _jTextField = new JTextField("file name");
            this.fileName = _jTextField;
            final jp.swest.ledcamp.xtendhelper.Consumer<String> _function = new jp.swest.ledcamp.xtendhelper.Consumer<String>() {
              public void accespt(final String it) {
                TemplatePanel.this.map.setFileName(it);
              }
            };
            new TextBinding(this.fileName, _function);
            this.fileName.setForeground(Color.GRAY);
            FocusAdapter _clearField = this.clearField(this.fileName);
            this.fileName.addFocusListener(_clearField);
            globalCard.add(this.fileName);
          }
          {
            JTextField _jTextField = new JTextField("template file path");
            this.templateFile_G = _jTextField;
            final jp.swest.ledcamp.xtendhelper.Consumer<String> _function = new jp.swest.ledcamp.xtendhelper.Consumer<String>() {
              public void accespt(final String it) {
                TemplatePanel.this.map.setTemplateFile(it);
              }
            };
            new TextBinding(this.templateFile_G, _function);
            this.templateFile_G.setForeground(Color.GRAY);
            String _text = settingDialog.textTemplateDir.getText();
            FocusAdapter _browseFile = this.browseFile(_text, this.templateFile_G);
            this.templateFile_G.addFocusListener(_browseFile);
            globalCard.add(this.templateFile_G);
          }
        }
        {
          final JPanel defaultCard = new JPanel();
          GridLayout _gridLayout = new GridLayout(1, 2);
          defaultCard.setLayout(_gridLayout);
          String _name = TemplateType.Default.name();
          this.cardPane.add(defaultCard, _name);
          {
            JTextField _jTextField = new JTextField("file extension");
            this.fileExtension_D = _jTextField;
            final jp.swest.ledcamp.xtendhelper.Consumer<String> _function = new jp.swest.ledcamp.xtendhelper.Consumer<String>() {
              public void accespt(final String it) {
                TemplatePanel.this.map.setFileExtension(it);
              }
            };
            new TextBinding(this.fileExtension_D, _function);
            this.fileExtension_D.setForeground(Color.GRAY);
            FocusAdapter _clearField = this.clearField(this.fileExtension_D);
            this.fileExtension_D.addFocusListener(_clearField);
            defaultCard.add(this.fileExtension_D);
          }
          {
            JTextField _jTextField = new JTextField("template file path");
            this.templateFile_D = _jTextField;
            final jp.swest.ledcamp.xtendhelper.Consumer<String> _function = new jp.swest.ledcamp.xtendhelper.Consumer<String>() {
              public void accespt(final String it) {
                TemplatePanel.this.map.setTemplateFile(it);
              }
            };
            new TextBinding(this.templateFile_D, _function);
            this.templateFile_D.setForeground(Color.GRAY);
            String _text = settingDialog.textTemplateDir.getText();
            FocusAdapter _browseFile = this.browseFile(_text, this.templateFile_D);
            this.templateFile_D.addFocusListener(_browseFile);
            defaultCard.add(this.templateFile_D);
          }
        }
        {
          final JPanel stereotypeCard = new JPanel();
          GridLayout _gridLayout = new GridLayout(1, 3);
          stereotypeCard.setLayout(_gridLayout);
          String _name = TemplateType.Stereotype.name();
          this.cardPane.add(stereotypeCard, _name);
          {
            JTextField _jTextField = new JTextField("stereotype");
            this.stereotype = _jTextField;
            final jp.swest.ledcamp.xtendhelper.Consumer<String> _function = new jp.swest.ledcamp.xtendhelper.Consumer<String>() {
              public void accespt(final String it) {
                TemplatePanel.this.map.setStereotype(it);
              }
            };
            new TextBinding(this.stereotype, _function);
            this.stereotype.setForeground(Color.GRAY);
            FocusAdapter _clearField = this.clearField(this.stereotype);
            this.stereotype.addFocusListener(_clearField);
            stereotypeCard.add(this.stereotype);
          }
          {
            JTextField _jTextField = new JTextField("file extension");
            this.fileExtension_S = _jTextField;
            final jp.swest.ledcamp.xtendhelper.Consumer<String> _function = new jp.swest.ledcamp.xtendhelper.Consumer<String>() {
              public void accespt(final String it) {
                TemplatePanel.this.map.setFileExtension(it);
              }
            };
            new TextBinding(this.fileExtension_S, _function);
            this.fileExtension_S.setForeground(Color.GRAY);
            FocusAdapter _clearField = this.clearField(this.fileExtension_S);
            this.fileExtension_S.addFocusListener(_clearField);
            stereotypeCard.add(this.fileExtension_S);
          }
          {
            JTextField _jTextField = new JTextField("template file path");
            this.templateFile_S = _jTextField;
            final jp.swest.ledcamp.xtendhelper.Consumer<String> _function = new jp.swest.ledcamp.xtendhelper.Consumer<String>() {
              public void accespt(final String it) {
                TemplatePanel.this.map.setTemplateFile(it);
              }
            };
            new TextBinding(this.templateFile_S, _function);
            this.templateFile_S.setForeground(Color.GRAY);
            String _text = settingDialog.textTemplateDir.getText();
            FocusAdapter _browseFile = this.browseFile(_text, this.templateFile_S);
            this.templateFile_S.addFocusListener(_browseFile);
            stereotypeCard.add(this.templateFile_S);
          }
        }
      }
      {
        final JButton btnRemove = new JButton("x");
        final SettingDialog.TemplatePanel thisPanel = this;
        final ActionListener _function = new ActionListener() {
          public void actionPerformed(final ActionEvent it) {
            final Container owner = thisPanel.getParent();
            GenerateSetting _currentSetting = settingDialog.manager.getCurrentSetting();
            HashSet<TemplateMap> _mapping = _currentSetting.getMapping();
            _mapping.remove(thisPanel.map);
            owner.remove(thisPanel);
            owner.revalidate();
            owner.repaint();
          }
        };
        btnRemove.addActionListener(_function);
        this.add(btnRemove, BorderLayout.EAST);
      }
    }
    
    private void setField(final JTextField field, final String text) {
      boolean _isNullOrEmpty = StringExtensions.isNullOrEmpty(text);
      boolean _not = (!_isNullOrEmpty);
      if (_not) {
        field.setText(text);
        field.setForeground(Color.BLACK);
      }
    }
    
    private FocusAdapter browseFile(final String path, final JTextField field) {
      abstract class __TemplatePanel_1 extends FocusAdapter {
        boolean first;
      }
      
      return new __TemplatePanel_1() {
        {
          first = true;
        }
        public void focusGained(final FocusEvent e) {
          if (this.first) {
            final JFileChooser fileChooser = new JFileChooser();
            String _text = field.getText();
            String _plus = ((path + "/") + _text);
            final File file = new File(_plus);
            final File dir = new File(path);
            boolean _exists = file.exists();
            if (_exists) {
              fileChooser.setCurrentDirectory(file);
            } else {
              boolean _exists_1 = dir.exists();
              if (_exists_1) {
                fileChooser.setCurrentDirectory(dir);
              }
            }
            Container _parent = TemplatePanel.this.getParent();
            int _showOpenDialog = fileChooser.showOpenDialog(_parent);
            boolean _equals = (JFileChooser.APPROVE_OPTION == _showOpenDialog);
            if (_equals) {
              File _selectedFile = fileChooser.getSelectedFile();
              String _name = _selectedFile.getName();
              field.setText(_name);
              field.setForeground(Color.BLACK);
            }
            this.first = false;
          }
        }
        
        public void focusLost(final FocusEvent arg0) {
          this.first = true;
        }
      };
    }
    
    private FocusAdapter clearField(final JTextField field) {
      return new FocusAdapter() {
        public void focusGained(final FocusEvent arg0) {
          Color _foreground = field.getForeground();
          boolean _notEquals = (!Objects.equal(_foreground, Color.BLACK));
          if (_notEquals) {
            field.setForeground(Color.BLACK);
            field.setText("");
          }
        }
        
        public void focusLost(final FocusEvent arg0) {
        }
      };
    }
  }
  
  private SettingManager manager = SettingManager.getInstance();
  
  private JPanel contentPanel = new JPanel();
  
  private JComboBox<String> combo_templateSet;
  
  private JTextField textDestinationPath;
  
  private JComboBox<TemplateEngine> combo_templateEngine;
  
  private JButton btnAddSet;
  
  @Accessors
  private JTextField textTemplateDir;
  
  @Accessors
  private JPanel templatePanel;
  
  public SettingDialog(final JFrame parent) {
    super(parent, "Generator Settings", true);
    this.initComponent();
    this.load();
  }
  
  private Component initComponent() {
    Component _xblockexpression = null;
    {
      this.setBounds(100, 100, 800, 600);
      final Container root = this.getContentPane();
      BorderLayout _borderLayout = new BorderLayout();
      root.setLayout(_borderLayout);
      EmptyBorder _emptyBorder = new EmptyBorder(5, 5, 5, 5);
      this.contentPanel.setBorder(_emptyBorder);
      root.add(this.contentPanel, BorderLayout.CENTER);
      final GridBagLayout gbl = new GridBagLayout();
      gbl.columnWeights = new double[] { 0, 0, 0, 0 };
      gbl.columnWeights = new double[] { 0.0, 1.0, 0.0, Double.MIN_VALUE };
      gbl.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0 };
      gbl.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE };
      this.contentPanel.setLayout(gbl);
      final Insets insets = new Insets(0, 0, 5, 5);
      {
        JComboBox<String> _jComboBox = new JComboBox<String>();
        this.combo_templateSet = _jComboBox;
        final ActionListener _function = new ActionListener() {
          public void actionPerformed(final ActionEvent it) {
            SettingDialog.this.changeTemplateSet();
          }
        };
        this.combo_templateSet.addActionListener(_function);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.ABOVE_BASELINE;
        gbc.insets = insets;
        gbc.gridx = 0;
        gbc.gridy = 0;
        this.contentPanel.add(this.combo_templateSet, gbc);
      }
      {
        JPanel paneButton = new JPanel();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.insets = insets;
        gbc.anchor = GridBagConstraints.WEST;
        this.contentPanel.add(paneButton, gbc);
        {
          JButton _jButton = new JButton("Add");
          this.btnAddSet = _jButton;
          final ActionListener _function = new ActionListener() {
            public void actionPerformed(final ActionEvent it) {
              final String setName = JOptionPane.showInputDialog(SettingDialog.this, "please input templateSet name");
              boolean _notEquals = (!Objects.equal(setName, null));
              if (_notEquals) {
                final GenerateSetting generateSetting = new GenerateSetting();
                SettingDialog.this.manager.put(setName, generateSetting);
                SettingDialog.this.manager.setCurrentSetting(generateSetting);
                GenerateSetting _currentSetting = SettingDialog.this.manager.getCurrentSetting();
                _currentSetting.setTemplateID(setName);
                SettingDialog.this.combo_templateSet.addItem(setName);
                SettingDialog.this.combo_templateSet.setSelectedItem(setName);
                SettingDialog.this.enableAll();
              }
            }
          };
          this.btnAddSet.addActionListener(_function);
          GridBagConstraints _gridBagConstraints = new GridBagConstraints();
          gbc = _gridBagConstraints;
          gbc.anchor = GridBagConstraints.WEST;
          gbc.insets = insets;
          gbc.gridx = 1;
          gbc.gridy = 0;
          paneButton.add(this.btnAddSet, gbc);
        }
        {
          final JButton btnRemoveSet = new JButton("Remove");
          final ActionListener _function = new ActionListener() {
            public void actionPerformed(final ActionEvent it) {
              final Object selectedSet = SettingDialog.this.combo_templateSet.getSelectedItem();
              SettingDialog.this.combo_templateSet.removeItem(selectedSet);
              SettingDialog.this.manager.remove(selectedSet);
              final Object afterSelectedItem = SettingDialog.this.combo_templateSet.getSelectedItem();
              boolean _notEquals = (!Objects.equal(afterSelectedItem, null));
              if (_notEquals) {
                Object _selectedItem = SettingDialog.this.combo_templateSet.getSelectedItem();
                GenerateSetting _get = SettingDialog.this.manager.get(_selectedItem);
                SettingDialog.this.manager.setCurrentSetting(_get);
              } else {
                SettingDialog.this.manager.setCurrentSetting(null);
                SettingDialog.this.disableAll();
              }
            }
          };
          btnRemoveSet.addActionListener(_function);
          GridBagConstraints _gridBagConstraints = new GridBagConstraints();
          gbc = _gridBagConstraints;
          gbc.anchor = GridBagConstraints.WEST;
          gbc.insets = insets;
          gbc.gridx = 2;
          gbc.gridy = 0;
          paneButton.add(btnRemoveSet, gbc);
        }
      }
      {
        final JLabel lblTemplateEngine = new JLabel("Template Engine");
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.EAST;
        Insets _insets = new Insets(0, 10, 5, 10);
        gbc.insets = _insets;
        gbc.gridx = 0;
        gbc.gridy = 1;
        this.contentPanel.add(lblTemplateEngine, gbc);
      }
      {
        JComboBox<TemplateEngine> _jComboBox = new JComboBox<TemplateEngine>();
        this.combo_templateEngine = _jComboBox;
        TemplateEngine[] _values = TemplateEngine.values();
        final Consumer<TemplateEngine> _function = new Consumer<TemplateEngine>() {
          public void accept(final TemplateEngine it) {
            SettingDialog.this.combo_templateEngine.addItem(it);
          }
        };
        ((List<TemplateEngine>)Conversions.doWrapArray(_values)).forEach(_function);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        Insets _insets = new Insets(0, 10, 5, 10);
        gbc.insets = _insets;
        gbc.gridx = 1;
        gbc.gridy = 1;
        this.contentPanel.add(this.combo_templateEngine, gbc);
      }
      {
        final JLabel lblTemplateDir = new JLabel("Template Dir");
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.EAST;
        Insets _insets = new Insets(0, 10, 5, 10);
        gbc.insets = _insets;
        gbc.gridx = 0;
        gbc.gridy = 2;
        this.contentPanel.add(lblTemplateDir, gbc);
      }
      {
        JTextField _jTextField = new JTextField();
        this.textTemplateDir = _jTextField;
        final jp.swest.ledcamp.xtendhelper.Consumer<String> _function = new jp.swest.ledcamp.xtendhelper.Consumer<String>() {
          public void accespt(final String it) {
            GenerateSetting _currentSetting = SettingDialog.this.manager.getCurrentSetting();
            _currentSetting.setTemplatePath(it);
          }
        };
        new TextBinding(this.textTemplateDir, _function);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = insets;
        gbc.gridx = 1;
        gbc.gridy = 2;
        this.contentPanel.add(this.textTemplateDir, gbc);
      }
      {
        final JButton btnTempDirBrowse = new JButton("...");
        final ActionListener _function = new ActionListener() {
          public void actionPerformed(final ActionEvent it) {
            String _property = System.getProperty("user.home");
            String _plus = (_property + "/.astah/plugins/m2t/");
            SettingDialog.this.browseDirectory(_plus, SettingDialog.this.textTemplateDir);
          }
        };
        btnTempDirBrowse.addActionListener(_function);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = insets;
        gbc.gridx = 2;
        gbc.gridy = 2;
        this.contentPanel.add(btnTempDirBrowse, gbc);
      }
      {
        final JLabel lblDestinationPath = new JLabel("Destination Path");
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.EAST;
        Insets _insets = new Insets(0, 10, 5, 10);
        gbc.insets = _insets;
        gbc.gridx = 0;
        gbc.gridy = 3;
        this.contentPanel.add(lblDestinationPath, gbc);
      }
      {
        JTextField _jTextField = new JTextField();
        this.textDestinationPath = _jTextField;
        final jp.swest.ledcamp.xtendhelper.Consumer<String> _function = new jp.swest.ledcamp.xtendhelper.Consumer<String>() {
          public void accespt(final String it) {
            GenerateSetting _currentSetting = SettingDialog.this.manager.getCurrentSetting();
            _currentSetting.setTargetPath(it);
          }
        };
        new TextBinding(this.textDestinationPath, _function);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = insets;
        gbc.gridx = 1;
        gbc.gridy = 3;
        this.contentPanel.add(this.textDestinationPath, gbc);
      }
      {
        final JButton btnDestBrowse = new JButton("...");
        final ActionListener _function = new ActionListener() {
          public void actionPerformed(final ActionEvent it) {
            SettingDialog.this.browseDirectory("", SettingDialog.this.textDestinationPath);
          }
        };
        btnDestBrowse.addActionListener(_function);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = insets;
        gbc.gridx = 2;
        gbc.gridy = 3;
        this.contentPanel.add(btnDestBrowse, gbc);
      }
      {
        final JScrollPane scrollPane = new JScrollPane();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = 3;
        gbc.insets = insets;
        gbc.gridx = 0;
        gbc.gridy = 4;
        this.contentPanel.add(scrollPane, gbc);
        {
          JPanel _jPanel = new JPanel();
          this.templatePanel = _jPanel;
          BoxLayout _boxLayout = new BoxLayout(this.templatePanel, BoxLayout.Y_AXIS);
          this.templatePanel.setLayout(_boxLayout);
          scrollPane.setViewportView(this.templatePanel);
        }
      }
      {
        final JButton btnAddTemplate = new JButton("Add template");
        final ActionListener _function = new ActionListener() {
          public void actionPerformed(final ActionEvent it) {
            final TemplateMap map = new TemplateMap();
            final SettingDialog.TemplatePanel template = new SettingDialog.TemplatePanel(SettingDialog.this, map);
            GenerateSetting _currentSetting = SettingDialog.this.manager.getCurrentSetting();
            HashSet<TemplateMap> _mapping = _currentSetting.getMapping();
            _mapping.add(map);
            SettingDialog.this.templatePanel.add(template);
            SettingDialog.this.templatePanel.revalidate();
          }
        };
        btnAddTemplate.addActionListener(_function);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = insets;
        gbc.gridx = 0;
        gbc.gridy = 5;
        this.contentPanel.add(btnAddTemplate, gbc);
      }
      Component _xblockexpression_1 = null;
      {
        final JPanel buttonPane = new JPanel();
        FlowLayout _flowLayout = new FlowLayout(FlowLayout.RIGHT);
        buttonPane.setLayout(_flowLayout);
        root.add(buttonPane, BorderLayout.SOUTH);
        {
          final JButton btnOk = new JButton("OK");
          btnOk.setActionCommand("OK");
          final ActionListener _function = new ActionListener() {
            public void actionPerformed(final ActionEvent it) {
              SettingDialog.this.manager.save();
              SettingDialog.this.dispose();
            }
          };
          btnOk.addActionListener(_function);
          Locale _locale = this.getLocale();
          buttonPane.add(btnOk, _locale);
          JRootPane _rootPane = this.getRootPane();
          _rootPane.setDefaultButton(btnOk);
        }
        Component _xblockexpression_2 = null;
        {
          final JButton btnCancel = new JButton("Cancel");
          final ActionListener _function = new ActionListener() {
            public void actionPerformed(final ActionEvent it) {
              SettingDialog.this.dispose();
            }
          };
          btnCancel.addActionListener(_function);
          btnCancel.setActionCommand("Cancel");
          _xblockexpression_2 = buttonPane.add(btnCancel);
        }
        _xblockexpression_1 = _xblockexpression_2;
      }
      _xblockexpression = _xblockexpression_1;
    }
    return _xblockexpression;
  }
  
  private void load() {
    Set<String> _keySet = this.manager.keySet();
    final Consumer<String> _function = new Consumer<String>() {
      public void accept(final String it) {
        SettingDialog.this.combo_templateSet.addItem(it);
      }
    };
    _keySet.forEach(_function);
    int _itemCount = this.combo_templateSet.getItemCount();
    boolean _equals = (_itemCount == 0);
    if (_equals) {
      this.disableAll();
    }
  }
  
  private void disableAll() {
    Component[] _components = this.contentPanel.getComponents();
    final Consumer<Component> _function = new Consumer<Component>() {
      public void accept(final Component it) {
        it.setEnabled(false);
      }
    };
    ((List<Component>)Conversions.doWrapArray(_components)).forEach(_function);
    this.btnAddSet.setEnabled(true);
  }
  
  private void enableAll() {
    Component[] _components = this.contentPanel.getComponents();
    final Consumer<Component> _function = new Consumer<Component>() {
      public void accept(final Component it) {
        it.setEnabled(true);
      }
    };
    ((List<Component>)Conversions.doWrapArray(_components)).forEach(_function);
  }
  
  private void changeTemplateSet() {
    final Object templateSet = this.combo_templateSet.getSelectedItem();
    boolean _equals = Objects.equal(templateSet, null);
    if (_equals) {
      return;
    }
    final GenerateSetting c = this.manager.get(((String) templateSet));
    this.manager.setCurrentSetting(c);
    String _templatePath = null;
    if (c!=null) {
      _templatePath=c.getTemplatePath();
    }
    this.textTemplateDir.setText(_templatePath);
    String _targetPath = null;
    if (c!=null) {
      _targetPath=c.getTargetPath();
    }
    this.textDestinationPath.setText(_targetPath);
    this.templatePanel.removeAll();
    this.templatePanel.revalidate();
    this.templatePanel.repaint();
    HashSet<TemplateMap> _mapping = null;
    if (c!=null) {
      _mapping=c.getMapping();
    }
    if (_mapping!=null) {
      final Consumer<TemplateMap> _function = new Consumer<TemplateMap>() {
        public void accept(final TemplateMap map) {
          SettingDialog.TemplatePanel _templatePanel = new SettingDialog.TemplatePanel(SettingDialog.this, map);
          SettingDialog.this.templatePanel.add(_templatePanel);
          SettingDialog.this.templatePanel.revalidate();
          SettingDialog.this.templatePanel.repaint();
        }
      };
      _mapping.forEach(_function);
    }
  }
  
  private void browseDirectory(final String path, final JTextField field) {
    final File pluginPath = new File(path);
    String _text = field.getText();
    final File file = new File(_text);
    final JFileChooser dirChooser = new JFileChooser();
    boolean _exists = file.exists();
    if (_exists) {
      dirChooser.setCurrentDirectory(file);
    } else {
      boolean _exists_1 = pluginPath.exists();
      if (_exists_1) {
        dirChooser.setCurrentDirectory(pluginPath);
      }
    }
    dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    Container _parent = this.getParent();
    int _showOpenDialog = dirChooser.showOpenDialog(_parent);
    boolean _equals = (JFileChooser.APPROVE_OPTION == _showOpenDialog);
    if (_equals) {
      File _selectedFile = dirChooser.getSelectedFile();
      String _absolutePath = _selectedFile.getAbsolutePath();
      field.setText(_absolutePath);
    }
  }
  
  @Pure
  public JTextField getTextTemplateDir() {
    return this.textTemplateDir;
  }
  
  public void setTextTemplateDir(final JTextField textTemplateDir) {
    this.textTemplateDir = textTemplateDir;
  }
  
  @Pure
  public JPanel getTemplatePanel() {
    return this.templatePanel;
  }
  
  public void setTemplatePanel(final JPanel templatePanel) {
    this.templatePanel = templatePanel;
  }
}
