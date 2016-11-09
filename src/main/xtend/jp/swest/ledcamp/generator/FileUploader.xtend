package jp.swest.ledcamp.generator

import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.ArrayList
import java.util.regex.Pattern
import jp.swest.ledcamp.setting.SettingManager
import org.apache.http.Consts
import org.apache.http.Header
import org.apache.http.client.config.RequestConfig
import org.apache.http.client.methods.HttpPost
import org.apache.http.client.utils.HttpClientUtils
import org.apache.http.entity.ContentType
import org.apache.http.entity.mime.HttpMultipartMode
import org.apache.http.entity.mime.MIME
import org.apache.http.entity.mime.MultipartEntityBuilder
import org.apache.http.entity.mime.content.FileBody
import org.apache.http.impl.client.BasicResponseHandler
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.message.BasicHeader
import java.nio.file.StandardOpenOption
import java.nio.file.StandardCopyOption
import com.change_vision.jude.api.inf.AstahAPI
import javax.swing.JOptionPane
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.stream.Collectors

class FileUploader {
    static val boundary = "----WebKitFormBoundaryAR7c5if126HH4FGk"
    static val defaultURL = "http://mdd-compile.shinshu-u.ac.jp/upload"
    static val urlJsonPatetrn = Pattern.compile("\\{\"url\": \"(.+)\"\\}")
    static val fileFieldPattern = Pattern.compile(".+filename=(.*)")

    static def fileUpload(String url, File zipFile) {
        val entity = MultipartEntityBuilder.create()
            .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
            .setBoundary(boundary)
            .setCharset(Consts.UTF_8)
            .addTextBody("user", "user", ContentType.create("text/plain", MIME.UTF8_CHARSET))
            .addTextBody("user_id", "user-id", ContentType.create("text/plain", MIME.UTF8_CHARSET))
            .addPart("file", new FileBody(zipFile, "application/x-zip-compressed"))
            .build
//        var line = ""
//        val br = new BufferedReader(new InputStreamReader(entity.content))
//        while( (line=br.readLine)!=null){
//            println(line)
//        }

        val httpPost = new HttpPost(url)
        httpPost.setHeader("Content-Type", "multipart/form-data; boundary="+boundary)
        httpPost.entity = entity

        val headers = new ArrayList<Header>();
        headers.add(new BasicHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8"))
        headers.add(new BasicHeader("Accept-Charset","utf-8"))
        headers.add(new BasicHeader("Accept-Language","ja,en-US;q=0.8,en;q=0.6"))
        headers.add(new BasicHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36"))
        headers.add(new BasicHeader("Accept-Encoding","gzip, deflate"))

        val requestConfig = RequestConfig.custom
            .setCircularRedirectsAllowed(true)
            .setMaxRedirects(30)
            .setRedirectsEnabled(true).build

        val client = HttpClientBuilder.create
            .setDefaultRequestConfig(requestConfig)
            .setDefaultHeaders(headers)
            .build

        val response = client.execute(httpPost)
        if(response.statusLine.statusCode != 200){
            println(response.statusLine.statusCode)
            // throw exception
        }

        val body = new BasicResponseHandler().handleResponse(response)
        HttpClientUtils.closeQuietly(client)
        return getURL(body)
    }

    def static void fileDownload(String url, Path targetPath){
        val astahAPI = AstahAPI.astahAPI
        val conn = new URL(url).openConnection as HttpURLConnection
        conn.requestMethod = "GET"
        conn.doInput = true
        conn.connect
        println(conn.responseCode)
        println(conn.headerFields)
        val fileField = conn.headerFields.get("Content-Disposition")
        if( fileField==null || fileField.isEmpty){ 
            val br = new BufferedReader(new InputStreamReader(conn.inputStream))
            val errorTemp = br.lines.collect(Collectors.joining("\n"))
            val errorReport = errorTemp.subSequence(errorTemp.indexOf("<pre>")+5, errorTemp.lastIndexOf("</pre>"))
            println("Compile is failed")
            println(errorReport)
            JOptionPane.showMessageDialog( astahAPI.viewManager.mainFrame, errorReport, "Compile is failed", JOptionPane.ERROR_MESSAGE)
            br.close
            return
        }

        val matcher = fileFieldPattern.matcher(fileField.get(0))
        if( ! matcher.matches){ /* throw exception */}
        val filename = matcher.group(1)
        if(filename.endsWith("bin")){ // compile success
            val writer = Files.newOutputStream(targetPath.resolve(filename), StandardOpenOption.CREATE, StandardOpenOption.WRITE)
            val is = conn.inputStream
            var buf = newByteArrayOfSize(1024);
            var bytesRead = 0
            while((bytesRead = is.read(buf))!= -1){
                writer.write(buf);
            }
            writer.flush
            writer.close
            is.close
            
            println("Compile is successful")
            JOptionPane.showMessageDialog( astahAPI.viewManager.mainFrame, "Compile is successful", "Compile is successful", JOptionPane.INFORMATION_MESSAGE)
        }else {
            println(filename) // unknown file? throw exception
        }
    }

    def static void main(String[] args) {
        val url = fileUpload(defaultURL, new File("C:/Users/hosoai/Desktop/3colors.zip"))
//        val url = "http://mdd-compile.shinshu-u.ac.jp/download/5ff995e2-a59b-11e6-b5fb-d850e63d46ca" // success var
//        val url = "http://mdd-compile.shinshu-u.ac.jp/download/7c704878-a628-11e6-b5fb-d850e63d46ca" // error var

        println(url)
        val setting = SettingManager.instance.currentSetting
        Thread.sleep(3000)
        fileDownload(url, Paths.get(setting.targetPath));
    }
    
    def fileUpload(File zipFile){
        val url = fileUpload(defaultURL, zipFile)
//        val url = fileUpload(defaultURL, new File("C:/Users/hosoai/Desktop/3colors.zip"))
//        val url = "http://mdd-compile.shinshu-u.ac.jp/download/5ff995e2-a59b-11e6-b5fb-d850e63d46ca" // success var
//        val url = "http://mdd-compile.shinshu-u.ac.jp/download/7c704878-a628-11e6-b5fb-d850e63d46ca" // error var

        val setting = SettingManager.instance.currentSetting
        Thread.sleep(3000)
        fileDownload(url, Paths.get(setting.targetPath));
    }

    def static getURL(String body){
        val match = urlJsonPatetrn.matcher(body)
        if (match.matches){
            return match.group(1)
        }else{
            return ""
        }
    }
}
