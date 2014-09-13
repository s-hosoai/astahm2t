package jp.swest.ledcamp.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JComboBox;
import javax.swing.JTextField;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JTable;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Set;

import jp.swest.ledcamp.FileMapping;
import jp.swest.ledcamp.GeneratorType;
import jp.swest.ledcamp.SettingManager;

public class GeneratorSettingDialog extends JPanel {
	private static final long serialVersionUID = 5098273628201593688L;
	
	private SettingManager settings;
	private String currentType = null;
	/* UI Components */
	private final JPanel contentPanel = new JPanel();
	private JTextField text_ModelCollectorPath;
	private JTextField text_FileGeneratorPath;
	private JTable table_Templates;
	private final JPanel parent;
	private final JComboBox<String> combo_GeneratorType;
	/**
	 * Create the dialog.
	 */
	public GeneratorSettingDialog() {
		parent = this;
		settings = SettingManager.getInstance();
		setBounds(100, 100, 450, 300);
		setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[]{123, 28, 53, 96, 0};
		gbl_contentPanel.rowHeights = new int[]{21, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPanel.columnWeights = new double[]{1.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_contentPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPanel.setLayout(gbl_contentPanel);
		{
			combo_GeneratorType = new JComboBox<String>(settings.getGeneratorTypeNames().toArray(new String[]{}));
			combo_GeneratorType.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					currentType = (String)combo_GeneratorType.getSelectedItem();
					changeGenerator(currentType);
				}
			});
			GridBagConstraints gbc_combo_GeneratorType = new GridBagConstraints();
			gbc_combo_GeneratorType.fill = GridBagConstraints.HORIZONTAL;
			gbc_combo_GeneratorType.insets = new Insets(0, 0, 5, 5);
			gbc_combo_GeneratorType.gridx = 0;
			gbc_combo_GeneratorType.gridy = 0;
			contentPanel.add(combo_GeneratorType, gbc_combo_GeneratorType);
		}
		{
			JButton button_AddGeneratorType = new JButton("new");
			button_AddGeneratorType.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					AddGeneratorDialog dialog = new AddGeneratorDialog();
					if(JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(parent, dialog)){
//TODO					addGeneratorType();
					}
				}
			});
			GridBagConstraints gbc_button_AddGeneratorType = new GridBagConstraints();
			gbc_button_AddGeneratorType.anchor = GridBagConstraints.NORTHWEST;
			gbc_button_AddGeneratorType.insets = new Insets(0, 0, 5, 5);
			gbc_button_AddGeneratorType.gridx = 1;
			gbc_button_AddGeneratorType.gridy = 0;
			contentPanel.add(button_AddGeneratorType, gbc_button_AddGeneratorType);
		}
		{
			JButton btnGeneralSettings = new JButton("general settings");
			GridBagConstraints gbc_btnGeneralSettings = new GridBagConstraints();
			gbc_btnGeneralSettings.insets = new Insets(0, 0, 5, 0);
			gbc_btnGeneralSettings.gridx = 3;
			gbc_btnGeneralSettings.gridy = 0;
			contentPanel.add(btnGeneralSettings, gbc_btnGeneralSettings);
		}
		{
			JLabel lblModelCollector = new JLabel("Model Collector");
			GridBagConstraints gbc_lblModelGenerator = new GridBagConstraints();
			gbc_lblModelGenerator.insets = new Insets(0, 0, 5, 5);
			gbc_lblModelGenerator.anchor = GridBagConstraints.EAST;
			gbc_lblModelGenerator.gridx = 0;
			gbc_lblModelGenerator.gridy = 1;
			contentPanel.add(lblModelCollector, gbc_lblModelGenerator);
		}
		{
			text_ModelCollectorPath = new JTextField();
			GridBagConstraints gbc_text_ModelGeneratorPath = new GridBagConstraints();
			gbc_text_ModelGeneratorPath.fill = GridBagConstraints.HORIZONTAL;
			gbc_text_ModelGeneratorPath.gridwidth = 2;
			gbc_text_ModelGeneratorPath.insets = new Insets(0, 0, 5, 5);
			gbc_text_ModelGeneratorPath.gridx = 1;
			gbc_text_ModelGeneratorPath.gridy = 1;
			contentPanel.add(text_ModelCollectorPath, gbc_text_ModelGeneratorPath);
			text_ModelCollectorPath.setColumns(10);
			text_ModelCollectorPath.addFocusListener(new FocusListener() {
				@Override
				public void focusLost(FocusEvent e) {
					if(currentType!=null && !settings.getGeneratorType(currentType).getModelCollectorPath().equals(text_ModelCollectorPath.getText())){
						GeneratorType modifiedGeneratorType = settings.getGeneratorType(currentType);
						modifiedGeneratorType.setModelCollectorPath(text_ModelCollectorPath.getText());
						settings.addGeneratorType(modifiedGeneratorType);
					}
				}
				
				@Override
				public void focusGained(FocusEvent e) {
				}
			});
		}
		{
			JButton button_BrowseModelCollector = new JButton("...");
			button_BrowseModelCollector.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JFileChooser chooser = new JFileChooser();
					if(JFileChooser.APPROVE_OPTION == chooser.showDialog(parent, "OK")){
						text_ModelCollectorPath.setText(chooser.getSelectedFile().getAbsolutePath());
					}
				}
			});
			GridBagConstraints gbc_button_BrowseModelCollector = new GridBagConstraints();
			gbc_button_BrowseModelCollector.insets = new Insets(0, 0, 5, 0);
			gbc_button_BrowseModelCollector.gridx = 3;
			gbc_button_BrowseModelCollector.gridy = 1;
			contentPanel.add(button_BrowseModelCollector, gbc_button_BrowseModelCollector);
		}
		{
			JLabel lblFilegenerator = new JLabel("File Generator");
			GridBagConstraints gbc_lblFilegenerator = new GridBagConstraints();
			gbc_lblFilegenerator.anchor = GridBagConstraints.EAST;
			gbc_lblFilegenerator.insets = new Insets(0, 0, 5, 5);
			gbc_lblFilegenerator.gridx = 0;
			gbc_lblFilegenerator.gridy = 2;
			contentPanel.add(lblFilegenerator, gbc_lblFilegenerator);
		}
		{
			text_FileGeneratorPath = new JTextField();
			GridBagConstraints gbc_text_FileGeneratorPath = new GridBagConstraints();
			gbc_text_FileGeneratorPath.gridwidth = 2;
			gbc_text_FileGeneratorPath.insets = new Insets(0, 0, 5, 5);
			gbc_text_FileGeneratorPath.fill = GridBagConstraints.HORIZONTAL;
			gbc_text_FileGeneratorPath.gridx = 1;
			gbc_text_FileGeneratorPath.gridy = 2;
			contentPanel.add(text_FileGeneratorPath, gbc_text_FileGeneratorPath);
			text_FileGeneratorPath.addFocusListener(new FocusListener() {
				@Override
				public void focusLost(FocusEvent e) {
					if(currentType!=null && !settings.getGeneratorType(currentType).getFileGeneratorPath().equals(text_FileGeneratorPath.getText())){
						GeneratorType modifiedGeneratorType = settings.getGeneratorType(currentType);
						modifiedGeneratorType.setFileGeneratorPath(text_FileGeneratorPath.getText());
						settings.addGeneratorType(modifiedGeneratorType);
					}					
				}
				@Override
				public void focusGained(FocusEvent e) {
				}
			});
			text_FileGeneratorPath.addPropertyChangeListener(new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
				}
			});
			text_FileGeneratorPath.setColumns(10);
		}
		{
			JButton button_BrowseFileGenerator = new JButton("...");
			button_BrowseFileGenerator.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JFileChooser chooser = new JFileChooser();
					if(JFileChooser.APPROVE_OPTION == chooser.showDialog(parent, "OK")){
						text_FileGeneratorPath.setText(chooser.getSelectedFile().getAbsolutePath());
					}
				}
			});
			GridBagConstraints gbc_button_BrowseFileGenerator = new GridBagConstraints();
			gbc_button_BrowseFileGenerator.insets = new Insets(0, 0, 5, 0);
			gbc_button_BrowseFileGenerator.gridx = 3;
			gbc_button_BrowseFileGenerator.gridy = 2;
			contentPanel.add(button_BrowseFileGenerator, gbc_button_BrowseFileGenerator);
		}
		{
			JButton button_AddTemplate = new JButton("add new template");
			button_AddTemplate.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
//TODO 				addTemplte
				}
			});
			GridBagConstraints gbc_button_AddTemplate = new GridBagConstraints();
			gbc_button_AddTemplate.insets = new Insets(0, 0, 5, 5);
			gbc_button_AddTemplate.gridx = 0;
			gbc_button_AddTemplate.gridy = 3;
			contentPanel.add(button_AddTemplate, gbc_button_AddTemplate);
		}
		{
			JButton btnRemoveTemplate = new JButton("remove template");
			GridBagConstraints gbc_btnRemoveTemplate = new GridBagConstraints();
			gbc_btnRemoveTemplate.insets = new Insets(0, 0, 5, 5);
			gbc_btnRemoveTemplate.gridx = 1;
			gbc_btnRemoveTemplate.gridy = 3;
			contentPanel.add(btnRemoveTemplate, gbc_btnRemoveTemplate);
		}
		{
			String[] columns = {"check","name","ext","path","description"};
			Set<String> templateNames = settings.getTemplatesNames();
			Object[][] tableData = new Object[templateNames.size()][5];
			int i = 0;
			for(String key : templateNames){
				FileMapping map = settings.getTemplate(key);
				tableData[i][0] = new JCheckBox();
				tableData[i][1] = map.getName();
				tableData[i][2] = map.getExt();
				tableData[i][3] = map.getPath();
				tableData[i][4] = map.getDescription();
				i++;
			}
			table_Templates = new JTable(tableData, columns);
			
			GridBagConstraints gbc_table_Templates = new GridBagConstraints();
			gbc_table_Templates.gridheight = 4;
			gbc_table_Templates.gridwidth = 4;
			gbc_table_Templates.fill = GridBagConstraints.BOTH;
			gbc_table_Templates.gridx = 0;
			gbc_table_Templates.gridy = 4;
			contentPanel.add(table_Templates, gbc_table_Templates);
		}
		currentType = (String)combo_GeneratorType.getSelectedItem();
		changeGenerator(currentType);		
	}
	
	public void changeGenerator(String typeName){
		GeneratorType type = settings.getGeneratorType(typeName);
		this.text_ModelCollectorPath.setText(type.getModelCollectorPath());
		this.text_FileGeneratorPath.setText(type.getFileGeneratorPath());
	}
}
