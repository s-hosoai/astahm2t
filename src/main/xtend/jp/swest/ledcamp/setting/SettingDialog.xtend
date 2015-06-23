package jp.swest.ledcamp.setting

import java.awt.BorderLayout
import java.awt.CardLayout
import java.awt.Color
import java.awt.Dimension
import java.awt.FlowLayout
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.Insets
import java.awt.event.FocusAdapter
import java.awt.event.FocusEvent
import java.io.File
import javax.swing.BoxLayout
import javax.swing.JButton
import javax.swing.JComboBox
import javax.swing.JDialog
import javax.swing.JFileChooser
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JTextField
import javax.swing.border.EmptyBorder
import org.eclipse.xtend.lib.annotations.Accessors
import java.awt.GridLayout
import javax.swing.JOptionPane

class SettingDialog extends JDialog {
	private SettingManager manager = SettingManager.getInstance
	private JPanel contentPanel = new JPanel();
	private JComboBox<String> combo_templateSet
	private JTextField textDestinationPath;
	private JComboBox<TemplateEngine> combo_templateEngine
	private JButton btnAddSet
	@Accessors private JTextField textTemplateDir;
	@Accessors private JPanel templatePanel;

	new(JFrame parent) {
		super(parent, "Generator Settings", true)
		initComponent
		load()
	}

	private def initComponent() {
		setBounds(100, 100, 800, 600)
		val root = getContentPane()
		root.layout = new BorderLayout
		contentPanel.border = new EmptyBorder(5, 5, 5, 5)
		root.add(contentPanel, BorderLayout.CENTER)
		val gbl = new GridBagLayout
		gbl.columnWeights = #[0, 0, 0, 0]
		gbl.columnWeights = #[0.0, 1.0, 0.0, Double.MIN_VALUE]
		gbl.rowHeights = #[0, 0, 0, 0, 0, 0, 0]
		gbl.rowWeights = #[0.0, 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE]
		contentPanel.layout = gbl
		val insets = new Insets(0, 0, 5, 5);
		{
			combo_templateSet = new JComboBox<String>
			combo_templateSet.addActionListener[changeTemplateSet]
			var gbc = new GridBagConstraints
			gbc.fill = GridBagConstraints.HORIZONTAL
			gbc.anchor = GridBagConstraints.ABOVE_BASELINE
			gbc.insets = insets
			gbc.gridx = 0
			gbc.gridy = 0
			contentPanel.add(combo_templateSet, gbc)
		}
		{
			var paneButton = new JPanel
			var gbc = new GridBagConstraints
			gbc.gridx = 1
			gbc.gridy = 0
			gbc.insets = insets
			gbc.anchor = GridBagConstraints.WEST
			contentPanel.add(paneButton, gbc)

			{
				btnAddSet = new JButton("Add")
				btnAddSet.addActionListener [
					var setName = JOptionPane.showInputDialog(this, "please input templateSet name")
					if (setName != null) {
						combo_templateSet.addItem(setName)
						combo_templateSet.selectedItem = setName
						val generateSetting = new GenerateSetting
						manager.put(setName, generateSetting)
						manager.currentSetting = generateSetting
						enableAll
					}
				]
				gbc = new GridBagConstraints
				gbc.anchor = GridBagConstraints.WEST
				gbc.insets = insets
				gbc.gridx = 1
				gbc.gridy = 0
				paneButton.add(btnAddSet, gbc)
			}
			{
				val btnRemoveSet = new JButton("Remove")
				btnRemoveSet.addActionListener [
					val selectedSet = combo_templateSet.selectedItem
					combo_templateSet.removeItem(selectedSet)
					manager.remove(selectedSet)
					val afterSelectedItem = combo_templateSet.selectedItem
					if (afterSelectedItem != null) {
						manager.currentSetting = manager.get(combo_templateSet.selectedItem)
					} else {
						manager.currentSetting = null
						disableAll
					}
				]
				gbc = new GridBagConstraints
				gbc.anchor = GridBagConstraints.WEST
				gbc.insets = insets
				gbc.gridx = 2
				gbc.gridy = 0
				paneButton.add(btnRemoveSet, gbc)
			}
		}
		{
			val lblTemplateEngine = new JLabel("Template Engine")
			var gbc = new GridBagConstraints
			gbc.anchor = GridBagConstraints.EAST
			gbc.insets = new Insets(0, 10, 5, 10)
			gbc.gridx = 0
			gbc.gridy = 1
			contentPanel.add(lblTemplateEngine, gbc)
		}
		{
			combo_templateEngine = new JComboBox<TemplateEngine>
			TemplateEngine.values.forEach[combo_templateEngine.addItem(it)]
			var gbc = new GridBagConstraints
			gbc.anchor = GridBagConstraints.WEST
			gbc.insets = new Insets(0, 10, 5, 10)
			gbc.gridx = 1
			gbc.gridy = 1
			contentPanel.add(combo_templateEngine, gbc)
		}
		{
			val lblTemplateDir = new JLabel("Template Dir")
			var gbc = new GridBagConstraints
			gbc.anchor = GridBagConstraints.EAST
			gbc.insets = new Insets(0, 10, 5, 10)
			gbc.gridx = 0
			gbc.gridy = 2
			contentPanel.add(lblTemplateDir, gbc)
		}
		{
			textTemplateDir = new JTextField
			var gbc = new GridBagConstraints
			gbc.fill = GridBagConstraints.HORIZONTAL
			gbc.insets = insets
			gbc.gridx = 1
			gbc.gridy = 2
			contentPanel.add(textTemplateDir, gbc)
		}
		{
			val btnTempDirBrowse = new JButton("...")
			btnTempDirBrowse.addActionListener[
				browseDirectory(System.getProperty("user.home") + "/.astah/plugins/m2t/", textTemplateDir)]
			var gbc = new GridBagConstraints
			gbc.insets = insets
			gbc.gridx = 2
			gbc.gridy = 2
			contentPanel.add(btnTempDirBrowse, gbc)
		}
		{
			val lblDestinationPath = new JLabel("Destination Path")
			var gbc = new GridBagConstraints
			gbc.anchor = GridBagConstraints.EAST
			gbc.insets = new Insets(0, 10, 5, 10)
			gbc.gridx = 0
			gbc.gridy = 3
			contentPanel.add(lblDestinationPath, gbc)
		}
		{
			textDestinationPath = new JTextField
			var gbc = new GridBagConstraints
			gbc.fill = GridBagConstraints.HORIZONTAL
			gbc.insets = insets
			gbc.gridx = 1
			gbc.gridy = 3
			contentPanel.add(textDestinationPath, gbc)
		}
		{
			val btnDestBrowse = new JButton("...")
			btnDestBrowse.addActionListener[browseDirectory("", textDestinationPath)]
			var gbc = new GridBagConstraints
			gbc.insets = insets
			gbc.gridx = 2
			gbc.gridy = 3
			contentPanel.add(btnDestBrowse, gbc)
		}
		{
			val scrollPane = new JScrollPane
			var gbc = new GridBagConstraints
			gbc.fill = GridBagConstraints.BOTH
			gbc.gridwidth = 3
			gbc.insets = insets
			gbc.gridx = 0
			gbc.gridy = 4
			contentPanel.add(scrollPane, gbc)
			{
				templatePanel = new JPanel
				templatePanel.layout = new BoxLayout(templatePanel, BoxLayout.Y_AXIS)
				scrollPane.viewportView = templatePanel
			}
		}
		{
			val btnAddTemplate = new JButton("Add template")
			btnAddTemplate.addActionListener(
				[
					val template = new TemplatePanel(this)
					templatePanel.add(template)
					templatePanel.revalidate
				])
			var gbc = new GridBagConstraints
			gbc.insets = insets
			gbc.gridx = 0
			gbc.gridy = 5
			contentPanel.add(btnAddTemplate, gbc)
		}
		{
			val buttonPane = new JPanel
			buttonPane.layout = new FlowLayout(FlowLayout.RIGHT)
			root.add(buttonPane, BorderLayout.SOUTH)
			{
				val btnOk = new JButton("OK")
				btnOk.setActionCommand("OK")
				btnOk.addActionListener [
					//manager.save
					dispose
				]
				buttonPane.add(btnOk, locale)
				getRootPane.defaultButton = btnOk
			}
			{
				val btnCancel = new JButton("Cancel")
				btnCancel.addActionListener [
					dispose
				]
				btnCancel.setActionCommand("Cancel")
				buttonPane.add(btnCancel)
			}
		}
	}

	private def load() {
		manager.keySet.forEach[combo_templateSet.addItem(it)]
		if (combo_templateSet.itemCount == 0) {
			disableAll
		}
	}

	private def disableAll() {
		contentPanel.components.forEach[it.enabled = false]
		btnAddSet.enabled = true
	}

	private def enableAll() {
		contentPanel.components.forEach[it.enabled = true]
	}

	private def changeTemplateSet() {
		val templateSet = combo_templateSet.selectedItem
		if (templateSet == null) {
			return
		}
		val c = manager.get(templateSet)
		manager.currentSetting = c
		textTemplateDir.text = c?.templatePath
		textDestinationPath.text = c?.targetPath
		c?.mapping?.forEach [ key, map |
			templatePanel.add(new TemplatePanel(this, map))
		]
	}

	private def browseDirectory(String path, JTextField field) {
		val pluginPath = new File(path)
		val dirChooser = new JFileChooser
		if (pluginPath.exists) {
			dirChooser.currentDirectory = pluginPath
		}
		dirChooser.fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
		if (JFileChooser.APPROVE_OPTION == dirChooser.showOpenDialog(parent)) {
			field.text = dirChooser.selectedFile.absolutePath
		}
	}

	static class TemplatePanel extends JPanel {
		private JComboBox<TemplateType> comboType
		private JPanel cardPane
		private CardLayout typeCardLayout
		private JTextField templateFile
		private JTextField fileName
		private JTextField fileExtension
		private JTextField stereotype

		new(SettingDialog settingDialog) {
			initComponent(settingDialog)
		}

		new(SettingDialog settingDialog, TemplateMap map) {
			this(settingDialog)
			comboType.selectedItem = map.generateType
			templateFile.text = map.templateFile
			switch (map.generateType) {
				case TemplateType.Global:
					fileName.text = map.fileName
				case TemplateType.Default:
					fileExtension.text = map.fileExtension
				case Stereotype: {
					stereotype.text = map.stereotype
					fileExtension.text = map.fileExtension
				}
			}
		}

		def initComponent(SettingDialog settingDialog) {
			println(settingDialog.contentPanel.width)
			this.layout = new BorderLayout()
			this.maximumSize = new Dimension(settingDialog.contentPanel.width, 30);
			{
				comboType = new JComboBox
				typeCardLayout = new CardLayout
				TemplateType.values.forEach[comboType.addItem(it)]
				comboType.addActionListener [
					typeCardLayout.show(cardPane, comboType.getItemAt(comboType.selectedIndex).name)
				]
				add(comboType, BorderLayout.WEST)
			}
			{
				cardPane = new JPanel
				cardPane.layout = typeCardLayout
				add(cardPane, BorderLayout.CENTER)
				{ // Global Card
					val globalCard = new JPanel
					globalCard.layout = new GridLayout(1, 2)
					cardPane.add(globalCard, TemplateType.Global.name)
					{
						fileName = new JTextField("file name")
						fileName.foreground = Color.GRAY
						fileName.addFocusListener(clearField(fileName))
						globalCard.add(fileName)
					}
					{
						templateFile = new JTextField("template file path")
						templateFile.foreground = Color.GRAY
						templateFile.addFocusListener(browseFile(settingDialog.textTemplateDir.getText(), templateFile))
						globalCard.add(templateFile)
					}
				}
				{ // Default Card
					val defaultCard = new JPanel
					defaultCard.layout = new GridLayout(1, 2)

					//new BoxLayout(defaultCard, BoxLayout.X_AXIS)
					cardPane.add(defaultCard, TemplateType.Default.name)
					{
						fileExtension = new JTextField("file extension")
						fileExtension.foreground = Color.GRAY
						fileExtension.addFocusListener(clearField(fileExtension))
						defaultCard.add(fileExtension)
					}
					{
						templateFile = new JTextField("template file path")
						templateFile.foreground = Color.GRAY
						templateFile.addFocusListener(browseFile(settingDialog.textTemplateDir.getText(), templateFile))
						defaultCard.add(templateFile)
					}

				}
				{ // Stereotype Card                    
					val stereotypeCard = new JPanel
					stereotypeCard.layout = new GridLayout(1, 3)

					//new BoxLayout(stereotypeCard, BoxLayout.X_AXIS)
					cardPane.add(stereotypeCard, TemplateType.Stereotype.name)
					{
						stereotype = new JTextField("stereotype")
						stereotype.foreground = Color.GRAY
						stereotype.addFocusListener(clearField(stereotype))
						stereotypeCard.add(stereotype)
					}
					{
						fileExtension = new JTextField("file extension")
						fileExtension.foreground = Color.GRAY
						fileExtension.addFocusListener(clearField(fileExtension))
						stereotypeCard.add(fileExtension)
					}
					{
						templateFile = new JTextField("template file path")
						templateFile.foreground = Color.GRAY
						templateFile.addFocusListener(browseFile(settingDialog.textTemplateDir.getText(), templateFile))
						stereotypeCard.add(templateFile)
					}
				}
				comboType.selectedItem = TemplateType.Default
			}
			{
				val btnRemove = new JButton("x")
				val thisPanel = this
				btnRemove.addActionListener(
					[
						val owner = thisPanel.parent
						owner.remove(thisPanel)
						owner.revalidate
						owner.repaint
					])
				add(btnRemove, BorderLayout.EAST)
			}
		}

		def getMapping() {
			var TemplateMap map = null
			switch (comboType.getItemAt(comboType.selectedIndex)) {
				case Global:
					map = TemplateMap.newGlobalTemplateMap(templateFile.text, fileName.text)
				case Default:
					map = TemplateMap.newDefaultTemplateMap(templateFile.text, fileExtension.text)
				case Stereotype:
					map = TemplateMap.newStereotypeTemplateMap(templateFile.text,
						fileExtension.text, stereotype.text)
			}
			return map
		}

		private def browseFile(String path, JTextField field) {
			new FocusAdapter() {
				boolean first = true;

				override focusGained(FocusEvent e) {
					if (first) {
						val fileChooser = new JFileChooser()
						val dir = new File(path)
						if (dir.exists) {
							fileChooser.currentDirectory = dir
						}
						if (JFileChooser.APPROVE_OPTION == fileChooser.showOpenDialog(parent)) {
							field.text = fileChooser.selectedFile.name
							field.foreground = Color.BLACK
						}
						first = false
					}
				}

				override focusLost(FocusEvent arg0) {
					first = true
				}
			}
		}

		private def clearField(JTextField field) {
			new FocusAdapter() {
				override focusGained(FocusEvent arg0) {
					if (field.foreground != Color.BLACK) {
						field.foreground = Color.BLACK
						field.text = ""
					}
				}

				override focusLost(FocusEvent arg0) {
					// validation
				}
			}
		}
	}
}
