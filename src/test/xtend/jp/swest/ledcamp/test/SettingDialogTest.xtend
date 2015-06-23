package jp.swest.ledcamp.test

import javax.swing.JFrame
import java.awt.BorderLayout
import javax.swing.JButton
import jp.swest.ledcamp.setting.SettingDialog
import org.junit.Test

class SettingDialogTest {
    def static void main(String[] args) {
        val frame = new JFrame()
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.setSize(500, 500)
        val layout = new BorderLayout
        val btn = new JButton("open dialog")
        btn.addActionListener(
            [
                val dialog = new SettingDialog(frame)
                dialog.visible = true
            ])
        frame.add(btn, BorderLayout.CENTER)
        frame.pack
        frame.layout = layout
        frame.visible = true    	
    }    
}