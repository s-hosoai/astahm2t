package jp.swest.ledcamp.actions

import com.change_vision.jude.api.inf.ui.IPluginActionDelegate
import com.change_vision.jude.api.inf.ui.IWindow
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.Insets
import java.awt.event.ActionEvent
import java.util.Properties
import javax.swing.AbstractAction
import javax.swing.JButton
import javax.swing.JDialog
import javax.swing.JLabel
import javax.swing.JTextField
import jp.swest.ledcamp.setting.SettingManager
import javax.swing.Icon
import javax.swing.ImageIcon

class SSHSettingsAction implements IPluginActionDelegate {
    Properties sshSettings = SettingManager.instance.sshSettings

    override run(IWindow arg0) throws UnExpectedException {
        val settingDialog = new SSHSettingDialog(sshSettings)
        settingDialog.visible = true
        if (settingDialog.result) {
            if (!settingDialog.host?.isEmpty){
            	sshSettings.put("host", settingDialog.host)
            }
            if (!settingDialog.dest?.isEmpty){
                sshSettings.put("dest", settingDialog.dest)
            }
            if (!settingDialog.main?.isEmpty){
                sshSettings.put("main", settingDialog.main)
            }
            SettingManager.instance.save
        }
        return null
    }

}

class SSHSettingDialog extends JDialog {
    private boolean result = false
    private JTextField host
    private JTextField dest
    private JTextField main

    new(Properties properties) {
        modal = true
        val dialog = this
        
        host = new JTextField()
        if(properties.get("host")!=null){
            host.text = properties.get("host") as String
        }
        dest = new JTextField()
        if(properties.get("dest")!=null){
            dest.text = properties.get("dest") as String
        }
        main = new JTextField()
        if(properties.get("main")!=null){
            main.text = properties.get("main") as String
        }
        
        val gridBagLayout = new GridBagLayout
        contentPane.layout = gridBagLayout
        var gridBagConstraints = new GridBagConstraints
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL
        gridBagConstraints.insets = new Insets(5, 5, 5, 5)
        gridBagConstraints.gridx = 0
        gridBagConstraints.gridy = 0
        gridBagConstraints.gridwidth = 5
        contentPane.add(new JLabel("Raspberry Pi IP Address"), gridBagConstraints)
        gridBagConstraints.gridy = 1
        contentPane.add(host, gridBagConstraints)
        gridBagConstraints.gridy = 2
        contentPane.add(new JLabel("Destination Path"), gridBagConstraints)
        gridBagConstraints.gridy = 3
        contentPane.add(dest, gridBagConstraints)
        gridBagConstraints.gridy = 4
        contentPane.add(new JLabel("Main program file"), gridBagConstraints)
        gridBagConstraints.gridy = 5
        contentPane.add(main, gridBagConstraints)

        var okButton = new JButton("OK")
        okButton.addActionListener(new AbstractAction(){
            override actionPerformed(ActionEvent e) {
                result = true;
                dialog.visible = false
            }
        })
        var cancelButton = new JButton("Cancel")
        cancelButton.addActionListener(new AbstractAction() {
            override actionPerformed(ActionEvent e) {
                result = false
                dialog.dispose
            }
        })
        gridBagConstraints.gridwidth = 1
        gridBagConstraints.fill = GridBagConstraints.NONE
        gridBagConstraints.gridx = 1
        gridBagConstraints.gridy = 6
        contentPane.add(okButton, gridBagConstraints)
        gridBagConstraints.gridx = 2
        contentPane.add(cancelButton, gridBagConstraints)
        pack
    }

    def getHost() {
        host.text
    }

    def getDest() {
        dest.text
    }

    def getMain() {
        main.text
    }

    def getResult() {
        return result
    }
}
