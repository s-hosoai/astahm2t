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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.function.BiConsumer;
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
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.Pure;

@SuppressWarnings("all")
public class SettingDialog extends JDialog {
  public static class TemplatePanel extends JPanel {
    private JComboBox<TemplateType> comboType;
    
    private JPanel cardPane;
    
    private CardLayout typeCardLayout;
    
    private JTextField templateFile;
    
    private JTextField fileName;
    
    private JTextField fileExtension;
    
    private JTextField stereotype;
    
    public TemplatePanel(final SettingDialog settingDialog) {
      this.initComponent(settingDialog);
    }
    
    public TemplatePanel(final SettingDialog settingDialog, final TemplateMap map) {
      this(settingDialog);
      TemplateType _generateType = map.getGenerateType();
      this.comboType.setSelectedItem(_generateType);
      String _templateFile = map.getTemplateFile();
      this.templateFile.setText(_templateFile);
      TemplateType _generateType_1 = map.getGenerateType();
      if (_generateType_1 != null) {
        switch (_generateType_1) {
          case Global:
            String _fileName = map.getFileName();
            this.fileName.setText(_fileName);
            break;
          case Default:
            String _fileExtension = map.getFileExtension();
            this.fileExtension.setText(_fileExtension);
            break;
          case Stereotype:
            String _stereotype = map.getStereotype();
            this.stereotype.setText(_stereotype);
            String _fileExtension_1 = map.getFileExtension();
            this.fileExtension.setText(_fileExtension_1);
            break;
          default:
            break;
        }
      }
    }
    
    public void initComponent(final SettingDialog settingDialog) {
      int _width = settingDialog.contentPanel.getWidth();
      InputOutput.<Integer>println(Integer.valueOf(_width));
      BorderLayout _borderLayout = new BorderLayout();
      this.setLayout(_borderLayout);
      int _width_1 = settingDialog.contentPanel.getWidth();
      Dimension _dimension = new Dimension(_width_1, 30);
      this.setMaximumSize(_dimension);
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
            TemplateType _itemAt = TemplatePanel.this.comboType.getItemAt(_selectedIndex);
            String _name = _itemAt.name();
            TemplatePanel.this.typeCardLayout.show(TemplatePanel.this.cardPane, _name);
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
            this.fileName.setForeground(Color.GRAY);
            FocusAdapter _clearField = this.clearField(this.fileName);
            this.fileName.addFocusListener(_clearField);
            globalCard.add(this.fileName);
          }
          {
            JTextField _jTextField = new JTextField("template file path");
            this.templateFile = _jTextField;
            this.templateFile.setForeground(Color.GRAY);
            String _text = settingDialog.textTemplateDir.getText();
            FocusAdapter _browseFile = this.browseFile(_text, this.templateFile);
            this.templateFile.addFocusListener(_browseFile);
            globalCard.add(this.templateFile);
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
            this.fileExtension = _jTextField;
            this.fileExtension.setForeground(Color.GRAY);
            FocusAdapter _clearField = this.clearField(this.fileExtension);
            this.fileExtension.addFocusListener(_clearField);
            defaultCard.add(this.fileExtension);
          }
          {
            JTextField _jTextField = new JTextField("template file path");
            this.templateFile = _jTextField;
            this.templateFile.setForeground(Color.GRAY);
            String _text = settingDialog.textTemplateDir.getText();
            FocusAdapter _browseFile = this.browseFile(_text, this.templateFile);
            this.templateFile.addFocusListener(_browseFile);
            defaultCard.add(this.templateFile);
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
            this.stereotype.setForeground(Color.GRAY);
            FocusAdapter _clearField = this.clearField(this.stereotype);
            this.stereotype.addFocusListener(_clearField);
            stereotypeCard.add(this.stereotype);
          }
          {
            JTextField _jTextField = new JTextField("file extension");
            this.fileExtension = _jTextField;
            this.fileExtension.setForeground(Color.GRAY);
            FocusAdapter _clearField = this.clearField(this.fileExtension);
            this.fileExtension.addFocusListener(_clearField);
            stereotypeCard.add(this.fileExtension);
          }
          {
            JTextField _jTextField = new JTextField("template file path");
            this.templateFile = _jTextField;
            this.templateFile.setForeground(Color.GRAY);
            String _text = settingDialog.textTemplateDir.getText();
            FocusAdapter _browseFile = this.browseFile(_text, this.templateFile);
            this.templateFile.addFocusListener(_browseFile);
            stereotypeCard.add(this.templateFile);
          }
        }
        this.comboType.setSelectedItem(TemplateType.Default);
      }
      {
        final JButton btnRemove = new JButton("x");
        final SettingDialog.TemplatePanel thisPanel = this;
        final ActionListener _function = new ActionListener() {
          public void actionPerformed(final ActionEvent it) {
            final Container owner = thisPanel.getParent();
            owner.remove(thisPanel);
            owner.revalidate();
            owner.repaint();
          }
        };
        btnRemove.addActionListener(_function);
        this.add(btnRemove, BorderLayout.EAST);
      }
    }
    
    public TemplateMap getMapping() {
      TemplateMap map = null;
      int _selectedIndex = this.comboType.getSelectedIndex();
      TemplateType _itemAt = this.comboType.getItemAt(_selectedIndex);
      if (_itemAt != null) {
        switch (_itemAt) {
          case Global:
            String _text = this.templateFile.getText();
            String _text_1 = this.fileName.getText();
            TemplateMap _newGlobalTemplateMap = TemplateMap.newGlobalTemplateMap(_text, _text_1);
            map = _newGlobalTemplateMap;
            break;
          case Default:
            String _text_2 = this.templateFile.getText();
            String _text_3 = this.fileExtension.getText();
            TemplateMap _newDefaultTemplateMap = TemplateMap.newDefaultTemplateMap(_text_2, _text_3);
            map = _newDefaultTemplateMap;
            break;
          case Stereotype:
            String _text_4 = this.templateFile.getText();
            String _text_5 = this.fileExtension.getText();
            String _text_6 = this.stereotype.getText();
            TemplateMap _newStereotypeTemplateMap = TemplateMap.newStereotypeTemplateMap(_text_4, _text_5, _text_6);
            map = _newStereotypeTemplateMap;
            break;
          default:
            break;
        }
      }
      return map;
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
            final File dir = new File(path);
            boolean _exists = dir.exists();
            if (_exists) {
              fileChooser.setCurrentDirectory(dir);
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
      GridBagLayout gbl = new GridBagLayout();
      gbl.columnWeights = new double[] { 0, 0, 0, 0 };
      gbl.columnWeights = new double[] { 0.0, 1.0, 0.0, Double.MIN_VALUE };
      gbl.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0 };
      gbl.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE };
      this.contentPanel.setLayout(gbl);
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
        Insets _insets = new Insets(0, 0, 5, 5);
        gbc.insets = _insets;
        gbc.gridx = 0;
        gbc.gridy = 0;
        this.contentPanel.add(this.combo_templateSet, gbc);
      }
      {
        JButton _jButton = new JButton("Add");
        this.btnAddSet = _jButton;
        final ActionListener _function = new ActionListener() {
          public void actionPerformed(final ActionEvent it) {
            String setName = JOptionPane.showInputDialog(SettingDialog.this, "please input templateSet name");
            boolean _notEquals = (!Objects.equal(setName, null));
            if (_notEquals) {
              SettingDialog.this.combo_templateSet.addItem(setName);
              SettingDialog.this.combo_templateSet.setSelectedItem(setName);
              final GenerateSetting generateSetting = new GenerateSetting();
              SettingDialog.this.manager.put(setName, generateSetting);
              SettingDialog.this.manager.setCurrentSetting(generateSetting);
              SettingDialog.this.enableAll();
            }
          }
        };
        this.btnAddSet.addActionListener(_function);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        Insets _insets = new Insets(0, 0, 5, 5);
        gbc.insets = _insets;
        gbc.gridx = 1;
        gbc.gridy = 0;
        this.contentPanel.add(this.btnAddSet, gbc);
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
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        Insets _insets = new Insets(0, 0, 5, 5);
        gbc.insets = _insets;
        gbc.gridx = 2;
        gbc.gridy = 0;
        this.contentPanel.add(btnRemoveSet, gbc);
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
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        Insets _insets = new Insets(0, 0, 5, 5);
        gbc.insets = _insets;
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
        Insets _insets = new Insets(0, 0, 5, 5);
        gbc.insets = _insets;
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
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        Insets _insets = new Insets(0, 0, 5, 5);
        gbc.insets = _insets;
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
        Insets _insets = new Insets(0, 0, 5, 5);
        gbc.insets = _insets;
        gbc.gridx = 2;
        gbc.gridy = 3;
        this.contentPanel.add(btnDestBrowse, gbc);
      }
      {
        final JScrollPane scrollPane = new JScrollPane();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = 3;
        Insets _insets = new Insets(0, 0, 5, 5);
        gbc.insets = _insets;
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
            final SettingDialog.TemplatePanel template = new SettingDialog.TemplatePanel(SettingDialog.this);
            SettingDialog.this.templatePanel.add(template);
            SettingDialog.this.templatePanel.revalidate();
          }
        };
        btnAddTemplate.addActionListener(_function);
        GridBagConstraints gbc = new GridBagConstraints();
        Insets _insets = new Insets(0, 0, 5, 5);
        gbc.insets = _insets;
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
    final GenerateSetting c = this.manager.get(templateSet);
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
    HashMap<String, TemplateMap> _mapping = null;
    if (c!=null) {
      _mapping=c.getMapping();
    }
    if (_mapping!=null) {
      final BiConsumer<String, TemplateMap> _function = new BiConsumer<String, TemplateMap>() {
        public void accept(final String key, final TemplateMap map) {
          SettingDialog.TemplatePanel _templatePanel = new SettingDialog.TemplatePanel(SettingDialog.this, map);
          SettingDialog.this.templatePanel.add(_templatePanel);
        }
      };
      _mapping.forEach(_function);
    }
  }
  
  private void browseDirectory(final String path, final JTextField field) {
    final File pluginPath = new File(path);
    final JFileChooser dirChooser = new JFileChooser();
    boolean _exists = pluginPath.exists();
    if (_exists) {
      dirChooser.setCurrentDirectory(pluginPath);
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
