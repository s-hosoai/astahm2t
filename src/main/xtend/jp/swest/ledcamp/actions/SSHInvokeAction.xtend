package jp.swest.ledcamp.actions

import com.change_vision.jude.api.inf.ui.IPluginActionDelegate
import com.change_vision.jude.api.inf.ui.IWindow
import com.change_vision.jude.api.inf.ui.IPluginActionDelegate.UnExpectedException
import jp.swest.ledcamp.setting.SettingManager
import com.jcraft.jsch.JSch
import com.jcraft.jsch.ChannelExec
import java.util.Properties
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.swing.JOptionPane

class SSHInvokeAction implements IPluginActionDelegate {
    SettingManager manager = SettingManager.instance
	override run(IWindow arg0) throws UnExpectedException {
        val host = manager.sshSettings.getProperty("host")
        val dest = manager.sshSettings.getProperty("dest")
        val main = manager.sshSettings.getProperty("main")
        if (host == null || host.isEmpty) {
            JOptionPane.showMessageDialog(arg0.parent, "settingsを開き，Raspberry PiのIPを指定してください．", "SSH execute error", JOptionPane.ERROR_MESSAGE)
            return null
        }
        if (dest == null || dest.isEmpty) {
            JOptionPane.showMessageDialog(arg0.parent, "settingsを開き，転送先のディレクトリを指定してください．", "SSH execute error", JOptionPane.ERROR_MESSAGE)
            return null
        }
        if (main == null || main.isEmpty) {
            JOptionPane.showMessageDialog(arg0.parent, "settingsを開き，実行プログラムを指定して下さい", "SSH execute error", JOptionPane.ERROR_MESSAGE)
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
    
            val exec = session.openChannel("exec") as ChannelExec
            val in = new BufferedReader(new InputStreamReader(exec.getInputStream()))
    
            exec.command = "cd "+dest+";python "+main
            exec.connect
            var msg = ""
            while ((msg = in.readLine) != null) {
                println(msg);
            }
    
            exec.disconnect
            session.disconnect
        }catch(Exception e){
            JOptionPane.showMessageDialog(arg0.parent, "Raspberry pi ("+host+")に接続できません．", "SSH execute error", JOptionPane.ERROR_MESSAGE)
        }
        return null
	}
}
