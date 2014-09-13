package jp.swest.ledcamp.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JLabel;
import java.awt.Insets;

public class AddGeneratorDialog extends JDialog {
	private static final long serialVersionUID = -3641427986233657814L;
	private final JPanel contentPanel = new JPanel();
	private JTextField textField;

	/**
	 * Create the dialog.
	 */
	public AddGeneratorDialog() {
		setBounds(100, 100, 450, 123);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[]{169, 96, 0};
		gbl_contentPanel.rowHeights = new int[]{19, 0, 0};
		gbl_contentPanel.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gbl_contentPanel.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		contentPanel.setLayout(gbl_contentPanel);
		{
			JLabel lblGeneratorName = new JLabel("Generator Name");
			GridBagConstraints gbc_lblGeneratorName = new GridBagConstraints();
			gbc_lblGeneratorName.anchor = GridBagConstraints.WEST;
			gbc_lblGeneratorName.insets = new Insets(0, 0, 5, 5);
			gbc_lblGeneratorName.gridx = 0;
			gbc_lblGeneratorName.gridy = 0;
			contentPanel.add(lblGeneratorName, gbc_lblGeneratorName);
		}
		{
			textField = new JTextField();
			GridBagConstraints gbc_textField = new GridBagConstraints();
			gbc_textField.gridwidth = 2;
			gbc_textField.fill = GridBagConstraints.HORIZONTAL;
			gbc_textField.anchor = GridBagConstraints.NORTH;
			gbc_textField.gridx = 0;
			gbc_textField.gridy = 1;
			contentPanel.add(textField, gbc_textField);
			textField.setColumns(10);
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

}
