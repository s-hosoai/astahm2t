package jp.swest.ledcamp.setting;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class SettingDialogTest extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private JTextField templatePath;
	private final Action action = new SwingAction();
	private JPanel templatePanel;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			SettingDialogTest dialog = new SettingDialogTest();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public SettingDialogTest() {
		setBounds(100, 100, 600, 600);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[]{0, 0, 0, 0};
		gbl_contentPanel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0};
		gbl_contentPanel.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_contentPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		contentPanel.setLayout(gbl_contentPanel);
		{
			JComboBox<String> comboBox = new JComboBox<String>();
			GridBagConstraints gbc_comboBox = new GridBagConstraints();
			gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
			gbc_comboBox.anchor = GridBagConstraints.ABOVE_BASELINE;
			gbc_comboBox.insets = new Insets(0, 0, 5, 5);
			gbc_comboBox.gridx = 0;
			gbc_comboBox.gridy = 0;
			contentPanel.add(comboBox, gbc_comboBox);
		}
		{
			JButton btnNewButton = new JButton("Add");
			GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
			gbc_btnNewButton.anchor = GridBagConstraints.WEST;
			gbc_btnNewButton.insets = new Insets(0, 0, 5, 5);
			gbc_btnNewButton.gridx = 1;
			gbc_btnNewButton.gridy = 0;
			contentPanel.add(btnNewButton, gbc_btnNewButton);
		}
		{
			JLabel lblNewLabel = new JLabel("Template Engine");
			GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
			gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
			gbc_lblNewLabel.insets = new Insets(0, 10, 5, 10);
			gbc_lblNewLabel.gridx = 0;
			gbc_lblNewLabel.gridy = 1;
			contentPanel.add(lblNewLabel, gbc_lblNewLabel);
		}
		{
			JComboBox<TemplateEngine> comboBox = new JComboBox<TemplateEngine>();
			for(TemplateEngine te : TemplateEngine.values()){
				comboBox.addItem(te);				
			}
			GridBagConstraints gbc_comboBox = new GridBagConstraints();
			gbc_comboBox.insets = new Insets(0, 10, 5, 10);
			gbc_comboBox.anchor = GridBagConstraints.WEST;
			gbc_comboBox.gridx = 1;
			gbc_comboBox.gridy = 1;
			contentPanel.add(comboBox, gbc_comboBox);
		}
		{
			JLabel lblNewLabel_1 = new JLabel("Destination Path");
			GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
			gbc_lblNewLabel_1.anchor = GridBagConstraints.EAST;
			gbc_lblNewLabel_1.insets = new Insets(0, 10, 5, 10);
			gbc_lblNewLabel_1.gridx = 0;
			gbc_lblNewLabel_1.gridy = 2;
			contentPanel.add(lblNewLabel_1, gbc_lblNewLabel_1);
		}
		{
			textField = new JTextField();
			GridBagConstraints gbc_textField = new GridBagConstraints();
			gbc_textField.insets = new Insets(0, 0, 5, 5);
			gbc_textField.fill = GridBagConstraints.HORIZONTAL;
			gbc_textField.gridx = 1;
			gbc_textField.gridy = 2;
			contentPanel.add(textField, gbc_textField);
			textField.setColumns(10);
		}
		{
			JButton button = new JButton("...");
			GridBagConstraints gbc_button = new GridBagConstraints();
			gbc_button.insets = new Insets(0, 0, 5, 0);
			gbc_button.gridx = 2;
			gbc_button.gridy = 2;
			contentPanel.add(button, gbc_button);
		}
		{
			JLabel lblTemplatePath = new JLabel("Template Path");
			GridBagConstraints gbc_lblTemplatePath = new GridBagConstraints();
			gbc_lblTemplatePath.anchor = GridBagConstraints.EAST;
			gbc_lblTemplatePath.insets = new Insets(0, 10, 5, 10);
			gbc_lblTemplatePath.gridx = 0;
			gbc_lblTemplatePath.gridy = 3;
			contentPanel.add(lblTemplatePath, gbc_lblTemplatePath);
		}
		{
			templatePath = new JTextField();
			GridBagConstraints gbc_templatePath = new GridBagConstraints();
			gbc_templatePath.insets = new Insets(0, 0, 5, 5);
			gbc_templatePath.fill = GridBagConstraints.HORIZONTAL;
			gbc_templatePath.gridx = 1;
			gbc_templatePath.gridy = 3;
			contentPanel.add(templatePath, gbc_templatePath);
			templatePath.setColumns(10);
		}
		{
			JButton button = new JButton("...");
			GridBagConstraints gbc_button = new GridBagConstraints();
			gbc_button.insets = new Insets(0, 0, 5, 0);
			gbc_button.gridx = 2;
			gbc_button.gridy = 3;
			contentPanel.add(button, gbc_button);
		}
		{
			JScrollPane scrollPane = new JScrollPane();
			GridBagConstraints gbc_scrollPane = new GridBagConstraints();
			gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
			gbc_scrollPane.gridwidth = 3;
			gbc_scrollPane.fill = GridBagConstraints.BOTH;
			gbc_scrollPane.gridx = 0;
			gbc_scrollPane.gridy = 4;
			contentPanel.add(scrollPane, gbc_scrollPane);
			{
				templatePanel = new JPanel();
				templatePanel.setSize(new Dimension());
				scrollPane.setViewportView(templatePanel);
				templatePanel.setLayout(new BoxLayout(templatePanel, BoxLayout.Y_AXIS));
			}
		}
		{
			JButton btnAddTemplate = new JButton("add Template");
			btnAddTemplate.setAction(action);
			GridBagConstraints gbc_btnAddTemplate = new GridBagConstraints();
			gbc_btnAddTemplate.insets = new Insets(0, 0, 0, 5);
			gbc_btnAddTemplate.gridx = 0;
			gbc_btnAddTemplate.gridy = 5;
			contentPanel.add(btnAddTemplate, gbc_btnAddTemplate);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

	private class SwingAction extends AbstractAction {
		public SwingAction() {
			putValue(NAME, "Add Template");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
			TemplatePanel panel = new TemplatePanel();
			templatePanel.add(panel);
			templatePanel.revalidate();
		}
	}
	private class TemplatePanel extends JPanel{
		private TemplateType type;
		private JComboBox<TemplateType> combo_type;
		private JTextField templateFile;
		TemplatePanel(){
			final JPanel owner = this;
			setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
			setMaximumSize(new Dimension(templatePanel.getWidth(), 30));
			
			combo_type = new JComboBox<TemplateType>();
			combo_type.addItem(TemplateType.Global);
			combo_type.addItem(TemplateType.Default);
			combo_type.addItem(TemplateType.Stereotype);
			add(combo_type);
			
			templateFile = new JTextField("-- template file -- ");
			templateFile.setForeground(Color.GRAY);
			templateFile.addFocusListener(new FocusAdapter() {				
				boolean first = true;
				@Override
				public void focusGained(FocusEvent e) {
					if(first){
						JFileChooser fileChooser;
						if(templatePath.getText().length()>0){
							fileChooser = new JFileChooser(templatePath.getText());
						}else{
							fileChooser = new JFileChooser();
						}
						if(JFileChooser.APPROVE_OPTION == fileChooser.showOpenDialog(owner)){
							templateFile.setText(fileChooser.getSelectedFile().getName());
							templateFile.setForeground(Color.GREEN);
						}
						first = false;
					}
				}
				@Override
				public void focusLost(FocusEvent e) {
					first = true;
				}
			});
			add(templateFile);
			
			JButton removeButton = new JButton("x");
			removeButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Container parent = owner.getParent();
					parent.remove(owner);
					parent.revalidate();
					parent.repaint();
				}
			});
			add(removeButton);
		}
	}
}
