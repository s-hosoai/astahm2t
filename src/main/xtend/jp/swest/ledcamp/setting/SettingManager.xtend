package jp.swest.ledcamp.setting

import java.io.File
import java.util.HashMap
import java.util.Map
import javax.xml.bind.JAXB
import javax.xml.bind.annotation.XmlTransient
import org.eclipse.xtend.lib.annotations.Accessors
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.FileOutputStream
import java.util.zip.ZipFile
import java.io.IOException
import java.util.Properties
import java.io.FileInputStream
import java.nio.file.Files
import java.nio.file.Paths
import java.io.OutputStream

// Singleton
class SettingManager extends HashMap<String, GenerateSetting> {
    private static SettingManager instance;
    @XmlTransient @Accessors private String settingFilePath
    @Accessors private GenerateSetting currentSetting
    @XmlTransient private String userFolder = System.getProperty("user.home")
    @XmlTransient @Accessors private String m2tPluginFolderPath = userFolder + "/.astah/plugins/m2t/"
    @XmlTransient @Accessors private String currentAstahFileName

    @XmlTransient private String sshSettingFilePath
    @XmlTransient @Accessors private Properties sshSettings

    private new() {
        super()
        settingFilePath = m2tPluginFolderPath + "m2tsetting.xml"
        sshSettingFilePath = m2tPluginFolderPath + "sshsetting.properties"
    }

    static def getInstance() {
        if (instance == null) {
            instance = new SettingManager
            instance.load
        }
        return instance
    }

    def save() {
        JAXB.marshal(instance, new File(settingFilePath))
        sshSettings?.store(new FileOutputStream(sshSettingFilePath),"")
    }

    def load() {
        // load property file
        userFolder = System.getProperty("user.home")
        m2tPluginFolderPath = userFolder + "/.astah/plugins/m2t/"
        val asgenPluginFolder = new File(m2tPluginFolderPath);
        if (!(asgenPluginFolder.exists())) { // install setting files.
            try {
                asgenPluginFolder.getParentFile().mkdirs();
                val zipFile = File.createTempFile("astahm2t", "templeatezip");
                var BufferedInputStream bis = null;
                var BufferedOutputStream bos = null;
                try {
                    bis = new BufferedInputStream(getClass().getClassLoader().getResourceAsStream("m2t.zip"))
//                    println(getClass().classLoader.getResource("m2t.zip")
                    bos = new BufferedOutputStream(new FileOutputStream(zipFile))
                    writeFile(bis, bos);
                } finally {
                    bis.close();
                    bos.close();
                }
                unzip(new ZipFile(zipFile), asgenPluginFolder.getParent());
            } catch (IOException ioe) {
                System.err.println("cannot create template folders.");
            }
        }
        try {
            val settingFile = new File(settingFilePath)
            if (settingFile.exists) {
                val settings = JAXB.unmarshal(new File(settingFilePath), SettingManager)
                instance.clear
                instance.putAll(settings)
                instance.currentSetting = settings.currentSetting
            } else {
                createDefaultSetting
                save
            }
        } catch (Exception e) {
            e.printStackTrace
        // load fail. 
        }

        // SSH settings for ledcamp4
        sshSettingFilePath = m2tPluginFolderPath + "sshsetting.properties"
        sshSettings = new Properties()
        if (! Files.exists(Paths.get(sshSettingFilePath))) {
            return
        }
        sshSettings.load(new FileInputStream(sshSettingFilePath))
    }

    private def createDefaultSetting() {
        val sampleGenerateSetting = new GenerateSetting
        sampleGenerateSetting.targetPath = new File(userFolder).absolutePath
        sampleGenerateSetting.templatePath = new File(m2tPluginFolderPath + "templates/PyCreate/").absolutePath
        sampleGenerateSetting.templateEngine = TemplateEngine.Groovy
        sampleGenerateSetting.templateID = "PyCreate"
        sampleGenerateSetting.mapping.add(TemplateMap.newGlobalTemplateMap("main.template", "main.py"))
        sampleGenerateSetting.mapping.add(TemplateMap.newDefaultTemplateMap("python.template", "py"))
        instance.put("PyCreate", sampleGenerateSetting)
        instance.currentSetting = sampleGenerateSetting
        save
    }

    def getMap() {
        return instance as Map<String, GenerateSetting>
    }

    def setMap(Map<String, GenerateSetting> settings) {
        putAll(settings)
    }

    private def unzip(ZipFile zipFile, String path) throws IOException{
        val entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            val entry = entries.nextElement();
            val extractFile = new File(path, entry.getName());
            if (entry.isDirectory()) {
                extractFile.mkdirs();
            } else {
                var BufferedInputStream bis = null
                var BufferedOutputStream bos = null
                try {
                    bis = new BufferedInputStream(zipFile.getInputStream(entry))
                    bos = new BufferedOutputStream(new FileOutputStream(extractFile))
                    writeFile(bis, bos)
                } finally {
                    if (bis != null) {
                        bis.close();
                    }
                    if (bos != null) {
                        bos.close();
                    }
                }
            }
        }
    }

    private def writeFile(BufferedInputStream bis, BufferedOutputStream bos) throws IOException{
        var available = 0;
        while ((available = bis.available()) > 0) {
            val byte[] bs = newByteArrayOfSize(available)
            bis.read(bs);
            bos.write(bs);
        }
    }
}
