package jp.swest.ledcamp.actions

import com.change_vision.jude.api.inf.ui.IPluginActionDelegate
import com.change_vision.jude.api.inf.ui.IWindow
import com.change_vision.jude.api.inf.ui.IPluginActionDelegate.UnExpectedException
import jp.swest.ledcamp.setting.SettingManager
import com.jcraft.jsch.JSch
import com.jcraft.jsch.ChannelSftp
import java.io.File
import java.util.Properties
import javax.swing.JOptionPane
import java.awt.Window.Type

class SCPAction implements IPluginActionDelegate {
    SettingManager manager = SettingManager.instance

    override run(IWindow arg0) throws UnExpectedException {
        manager.sshSettings.put("host", "192.168.1.2")
        manager.sshSettings.put("dest", "sample")
        manager.sshSettings.put("main", "main.py")

        val host = manager.sshSettings.getProperty("host")
        val targetPath = new File(manager.currentSetting.targetPath)
        val dest = manager.sshSettings.getProperty("dest")
        if (host == null || host.isEmpty) {
            JOptionPane.showMessageDialog(arg0.parent, "settingsを開き，Raspberry PiのIPを指定してください．", "SCP error", JOptionPane.ERROR_MESSAGE)
        }
        if (dest == null || dest.isEmpty) {
            JOptionPane.showMessageDialog(arg0.parent, "settingsを開き，転送先のディレクトリを指定してください．", "SCP error", JOptionPane.ERROR_MESSAGE)
            return null
        }
        if (targetPath == null || !targetPath.exists) {
            JOptionPane.showMessageDialog(arg0.parent, "m2t＞settingsを開き，生成先ディレクトリを指定してください．", "SCP error", JOptionPane.ERROR_MESSAGE)
            return null
        }

        val jsch = new JSch
        val jschProp = new Properties
        jschProp.put("StrictHostKeyChecking", "no")
        val session = jsch.getSession("pi", host)
        session.config = jschProp
        session.password = "raspberry"
        session.timeout = 5
        try{
            session.connect
            
            val sftp = session.openChannel("sftp") as ChannelSftp
            sftp.connect
            sftp.cd(dest)
            for (file : targetPath.listFiles) {
                sftp.put(file.absolutePath, file.name)
            }
            sftp.disconnect
            session.disconnect
        }catch(Exception e){
            JOptionPane.showMessageDialog(arg0.parent, "Raspberry pi ("+host+")に接続できません．", "SSH execute error", JOptionPane.ERROR_MESSAGE)
        }
        return null
    }
}
