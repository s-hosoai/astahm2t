package jp.swest.ledcamp.view;

import groovy.util.ResourceException;
import groovy.util.ScriptException;

import java.awt.Component;

import javax.swing.JPanel;

import com.change_vision.jude.api.inf.AstahAPI;
import com.change_vision.jude.api.inf.exception.ProjectNotFoundException;
import com.change_vision.jude.api.inf.project.ProjectEvent;
import com.change_vision.jude.api.inf.project.ProjectEventListener;
import com.change_vision.jude.api.inf.ui.IPluginExtraTabView;
import com.change_vision.jude.api.inf.ui.ISelectionListener;

import java.awt.GridBagLayout;

import javax.swing.JComboBox;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import jp.swest.ledcamp.CodeGenerator;
import jp.swest.ledcamp.SettingManager;

public class GeneratorView extends JPanel implements IPluginExtraTabView, ProjectEventListener{	
	private static final long serialVersionUID = 7712896306186370181L;
	private JTextField text_TargetDir;
	private final JPanel parent;
	private final JComboBox<String> combo_GeneratorType;

	private final SettingManager settings;
	private final CodeGenerator generator;
	private JTextField text_ProjectName;

	/**
	 * Create the panel.
	 */
	public GeneratorView() {
		parent = this;
		settings = SettingManager.getInstance();
		generator = new CodeGenerator();
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JLabel lblNewLabel = new JLabel("Generator Type");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel.insets = new Insets(5, 5, 5, 5);
		gbc_lblNewLabel.gridx = 1;
		gbc_lblNewLabel.gridy = 1;
		add(lblNewLabel, gbc_lblNewLabel);
		
		String[] items = settings.getGeneratorTypeNames().toArray(new String[]{});
		combo_GeneratorType = new JComboBox<String>();
		for(String item : items){
			combo_GeneratorType.addItem(item);
		}
		GridBagConstraints gbc_combo_GeneratorType = new GridBagConstraints();
		gbc_combo_GeneratorType.gridwidth = 2;
		gbc_combo_GeneratorType.insets = new Insets(0, 0, 5, 5);
		gbc_combo_GeneratorType.fill = GridBagConstraints.HORIZONTAL;
		gbc_combo_GeneratorType.gridx = 1;
		gbc_combo_GeneratorType.gridy = 2;
		add(combo_GeneratorType, gbc_combo_GeneratorType);
		
		JButton btnNewButton_1 = new JButton("Generator Setting");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GeneratorSettingDialog dialog = new GeneratorSettingDialog(parent);
				if(JOptionPane.OK_OPTION == 
					JOptionPane.showConfirmDialog(parent, dialog, "Generator Setting", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE)){
					settings.saveProperties();
				}
			}
		});
		GridBagConstraints gbc_btnNewButton_1 = new GridBagConstraints();
		gbc_btnNewButton_1.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton_1.gridx = 3;
		gbc_btnNewButton_1.gridy = 2;
		add(btnNewButton_1, gbc_btnNewButton_1);
		
		JLabel lblNewLabel_2 = new JLabel("Project Name");
		GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
		gbc_lblNewLabel_2.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_2.insets = new Insets(5, 5, 5, 5);
		gbc_lblNewLabel_2.gridx = 1;
		gbc_lblNewLabel_2.gridy = 3;
		add(lblNewLabel_2, gbc_lblNewLabel_2);
		
		text_ProjectName = new JTextField();
		String projectName = settings.getCurrentProject();
		if(projectName!=null){
			text_ProjectName.setText(projectName);
		}
		GridBagConstraints gbc_text_ProjectName = new GridBagConstraints();
		gbc_text_ProjectName.insets = new Insets(0, 0, 5, 5);
		gbc_text_ProjectName.fill = GridBagConstraints.HORIZONTAL;
		gbc_text_ProjectName.gridx = 1;
		gbc_text_ProjectName.gridy = 4;
		add(text_ProjectName, gbc_text_ProjectName);
		text_ProjectName.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Target Directory");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_1.insets = new Insets(5, 5, 5, 5);
		gbc_lblNewLabel_1.gridx = 1;
		gbc_lblNewLabel_1.gridy = 5;
		add(lblNewLabel_1, gbc_lblNewLabel_1);
		
		text_TargetDir = new JTextField();
		String targetDir = settings.getTargetDirectory();
		if(targetDir!=null){
			text_TargetDir.setText(targetDir);
		}
		GridBagConstraints gbc_text_TargetDir = new GridBagConstraints();
		gbc_text_TargetDir.anchor = GridBagConstraints.NORTH;
		gbc_text_TargetDir.gridwidth = 2;
		gbc_text_TargetDir.insets = new Insets(0, 0, 5, 5);
		gbc_text_TargetDir.fill = GridBagConstraints.HORIZONTAL;
		gbc_text_TargetDir.gridx = 1;
		gbc_text_TargetDir.gridy = 6;
		add(text_TargetDir, gbc_text_TargetDir);
		text_TargetDir.setColumns(10);
		
		JButton button_BrowseTargetDir = new JButton("...");
		button_BrowseTargetDir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				if(JFileChooser.APPROVE_OPTION == chooser.showDialog(parent, "OK")){
					text_TargetDir.setText(chooser.getSelectedFile().getAbsolutePath());
				}
			}
		});
		GridBagConstraints gbc_button_BrowseTargetDir = new GridBagConstraints();
		gbc_button_BrowseTargetDir.insets = new Insets(0, 0, 5, 0);
		gbc_button_BrowseTargetDir.gridx = 3;
		gbc_button_BrowseTargetDir.gridy = 6;
		add(button_BrowseTargetDir, gbc_button_BrowseTargetDir);
		
		JButton btnGenerate = new JButton("Generate");
		GridBagConstraints gbc_btnGenerate = new GridBagConstraints();
		gbc_btnGenerate.insets = new Insets(0, 0, 0, 5);
		gbc_btnGenerate.gridx = 1;
		gbc_btnGenerate.gridy = 7;
		btnGenerate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					//TODO Check forms.
					String targetDir = text_TargetDir.getText().trim();
					String projectName = text_ProjectName.getText().trim();
					if("".equals(targetDir)){
						throw new IllegalArgumentException();
					}
					if("".equals(projectName)){
						throw new IllegalArgumentException();
					}
					
					//generate
					generator.generate(targetDir, projectName, settings.getGeneratorType((String)combo_GeneratorType.getSelectedItem()));
					JOptionPane.showMessageDialog(parent, "Generated in "+text_TargetDir.getText()+"/"+text_ProjectName.getText());
					
					// save targetDir
					settings.setTargetDirectory(targetDir);
					settings.setCurrentProject(projectName);
					settings.saveProperties();
				} catch (ClassNotFoundException e1) {
					JOptionPane.showMessageDialog(parent, "ClassNotFoundException in generate");
					e1.printStackTrace();
				} catch (ProjectNotFoundException e1) {
					JOptionPane.showMessageDialog(parent, "ProjectNotFoundException in generate");
					e1.printStackTrace();
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(parent, "IOException in generate");
					e1.printStackTrace();
				} catch (ResourceException e1) {
					JOptionPane.showMessageDialog(parent, "ResourceException in generate");
					e1.printStackTrace();
				} catch (ScriptException e1) {
					JOptionPane.showMessageDialog(parent, "ScriptException in generate");
					e1.printStackTrace();
				} catch (IllegalArgumentException iae){
					JOptionPane.showMessageDialog(parent, "Prease Enter forms.");
				}
			}
		});
		add(btnGenerate, gbc_btnGenerate);
		
		JButton btnNewButton_2 = new JButton("Preview");
		btnNewButton_2.setEnabled(false);
		GridBagConstraints gbc_btnNewButton_2 = new GridBagConstraints();
		gbc_btnNewButton_2.insets = new Insets(0, 0, 0, 5);
		gbc_btnNewButton_2.gridx = 2;
		gbc_btnNewButton_2.gridy = 7;
		add(btnNewButton_2, gbc_btnNewButton_2);
	}

	@Override
	public void addSelectionListener(ISelectionListener arg0) {
	}

	@Override
	public Component getComponent() {
		return this;
	}

	@Override
	public String getDescription() {
		return "AstahM2T Generator View";
	}

	@Override
	public String getTitle() {
		return "AstahM2T Generator View";
	}
	
	@Override
	public void activated() {
	}
	@Override
	public void deactivated() {
	}
	@Override
	public void projectChanged(ProjectEvent arg0) {
	}
	@Override
	public void projectClosed(ProjectEvent arg0) {		
	}
	@Override
	public void projectOpened(ProjectEvent arg0) {
	}
}
