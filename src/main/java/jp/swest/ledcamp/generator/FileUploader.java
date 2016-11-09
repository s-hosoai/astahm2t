package jp.swest.ledcamp.generator;

import com.change_vision.jude.api.inf.AstahAPI;
import com.change_vision.jude.api.inf.view.IViewManager;
import com.google.common.base.Objects;
import java.awt.Container;
import java.awt.Frame;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MIME;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.InputOutput;

@SuppressWarnings("all")
public class FileUploader {
  public static class CompileErrorDialog extends JDialog {
    public CompileErrorDialog(final Frame owner, final String errorMessage) {
      super(owner);
      this.setTitle("Compile Error");
      final JTextArea textArea = new JTextArea();
      textArea.setText(errorMessage);
      textArea.setEditable(false);
      textArea.setSize(400, 400);
      final JScrollPane scroll = new JScrollPane(textArea);
      Container _contentPane = this.getContentPane();
      _contentPane.add(scroll);
      this.pack();
      this.setVisible(true);
    }
  }
  
  private final static String boundary = "----WebKitFormBoundaryAR7c5if126HH4FGk";
  
  private final static String defaultURL = "http://mdd-compile.shinshu-u.ac.jp/upload";
  
  private final static Pattern urlJsonPatetrn = Pattern.compile("\\{\"url\": \"(.+)\"\\}");
  
  private final static Pattern fileFieldPattern = Pattern.compile(".+filename=(.*)");
  
  public static String fileUpload(final String url, final File zipFile) {
    try {
      MultipartEntityBuilder _create = MultipartEntityBuilder.create();
      MultipartEntityBuilder _setMode = _create.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
      MultipartEntityBuilder _setBoundary = _setMode.setBoundary(FileUploader.boundary);
      MultipartEntityBuilder _setCharset = _setBoundary.setCharset(Consts.UTF_8);
      ContentType _create_1 = ContentType.create("text/plain", MIME.UTF8_CHARSET);
      MultipartEntityBuilder _addTextBody = _setCharset.addTextBody("user", "user", _create_1);
      ContentType _create_2 = ContentType.create("text/plain", MIME.UTF8_CHARSET);
      MultipartEntityBuilder _addTextBody_1 = _addTextBody.addTextBody("user_id", "user-id", _create_2);
      FileBody _fileBody = new FileBody(zipFile, "application/x-zip-compressed");
      MultipartEntityBuilder _addPart = _addTextBody_1.addPart("file", _fileBody);
      final HttpEntity entity = _addPart.build();
      final HttpPost httpPost = new HttpPost(url);
      httpPost.setHeader("Content-Type", ("multipart/form-data; boundary=" + FileUploader.boundary));
      httpPost.setEntity(entity);
      final ArrayList<Header> headers = new ArrayList<Header>();
      BasicHeader _basicHeader = new BasicHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
      headers.add(_basicHeader);
      BasicHeader _basicHeader_1 = new BasicHeader("Accept-Charset", "utf-8");
      headers.add(_basicHeader_1);
      BasicHeader _basicHeader_2 = new BasicHeader("Accept-Language", "ja,en-US;q=0.8,en;q=0.6");
      headers.add(_basicHeader_2);
      BasicHeader _basicHeader_3 = new BasicHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36");
      headers.add(_basicHeader_3);
      BasicHeader _basicHeader_4 = new BasicHeader("Accept-Encoding", "gzip, deflate");
      headers.add(_basicHeader_4);
      RequestConfig.Builder _custom = RequestConfig.custom();
      RequestConfig.Builder _setCircularRedirectsAllowed = _custom.setCircularRedirectsAllowed(true);
      RequestConfig.Builder _setMaxRedirects = _setCircularRedirectsAllowed.setMaxRedirects(30);
      RequestConfig.Builder _setRedirectsEnabled = _setMaxRedirects.setRedirectsEnabled(true);
      final RequestConfig requestConfig = _setRedirectsEnabled.build();
      HttpClientBuilder _create_3 = HttpClientBuilder.create();
      HttpClientBuilder _setDefaultRequestConfig = _create_3.setDefaultRequestConfig(requestConfig);
      HttpClientBuilder _setDefaultHeaders = _setDefaultRequestConfig.setDefaultHeaders(headers);
      final CloseableHttpClient client = _setDefaultHeaders.build();
      final CloseableHttpResponse response = client.execute(httpPost);
      StatusLine _statusLine = response.getStatusLine();
      int _statusCode = _statusLine.getStatusCode();
      boolean _notEquals = (_statusCode != 200);
      if (_notEquals) {
        StatusLine _statusLine_1 = response.getStatusLine();
        int _statusCode_1 = _statusLine_1.getStatusCode();
        InputOutput.<Integer>println(Integer.valueOf(_statusCode_1));
      }
      BasicResponseHandler _basicResponseHandler = new BasicResponseHandler();
      final String body = _basicResponseHandler.handleResponse(response);
      HttpClientUtils.closeQuietly(client);
      return FileUploader.getURL(body);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public static void fileDownload(final String url, final Path targetPath) {
    try {
      final AstahAPI astahAPI = AstahAPI.getAstahAPI();
      URL _uRL = new URL(url);
      URLConnection _openConnection = _uRL.openConnection();
      final HttpURLConnection conn = ((HttpURLConnection) _openConnection);
      conn.setRequestMethod("GET");
      conn.setDoInput(true);
      conn.connect();
      Map<String, List<String>> _headerFields = conn.getHeaderFields();
      final List<String> fileField = _headerFields.get("Content-Disposition");
      if ((Objects.equal(fileField, null) || fileField.isEmpty())) {
        InputStream _inputStream = conn.getInputStream();
        InputStreamReader _inputStreamReader = new InputStreamReader(_inputStream);
        final BufferedReader br = new BufferedReader(_inputStreamReader);
        Stream<String> _lines = br.lines();
        Collector<CharSequence, ?, String> _joining = Collectors.joining("\n");
        final String errorTemp = _lines.collect(_joining);
        int _indexOf = errorTemp.indexOf("<pre>");
        int _plus = (_indexOf + 5);
        int _lastIndexOf = errorTemp.lastIndexOf("</pre>");
        final CharSequence errorReport = errorTemp.subSequence(_plus, _lastIndexOf);
        InputOutput.<String>println("Compile is failed");
        InputOutput.<CharSequence>println(errorReport);
        String _string = errorReport.toString();
        FileUploader.showCompileErrorDialog(_string);
        br.close();
        return;
      }
      String _get = fileField.get(0);
      final Matcher matcher = FileUploader.fileFieldPattern.matcher(_get);
      boolean _matches = matcher.matches();
      boolean _not = (!_matches);
      if (_not) {
      }
      final String filename = matcher.group(1);
      boolean _endsWith = filename.endsWith("bin");
      if (_endsWith) {
        Path _resolve = targetPath.resolve(filename);
        final OutputStream writer = Files.newOutputStream(_resolve, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
        final InputStream is = conn.getInputStream();
        byte[] buf = new byte[1024];
        int bytesRead = 0;
        while (((bytesRead = is.read(buf)) != (-1))) {
          writer.write(buf, 0, bytesRead);
        }
        is.close();
        writer.close();
        InputOutput.<String>println("Compile is successful");
        IViewManager _viewManager = astahAPI.getViewManager();
        JFrame _mainFrame = _viewManager.getMainFrame();
        JOptionPane.showMessageDialog(_mainFrame, "Compile is successful", "Compile is successful", JOptionPane.INFORMATION_MESSAGE);
      } else {
        InputOutput.<String>println(filename);
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  /**
   * def static void main(String[] args) {
   * val url = fileUpload(defaultURL, new File("C:/Users/hosoai/Desktop/3colors.zip"))
   * //        val url = "http://mdd-compile.shinshu-u.ac.jp/download/5ff995e2-a59b-11e6-b5fb-d850e63d46ca" // success var
   * //        val url = "http://mdd-compile.shinshu-u.ac.jp/download/7c704878-a628-11e6-b5fb-d850e63d46ca" // error var
   * 
   * println(url)
   * val setting = SettingManager.instance.currentSetting
   * Thread.sleep(3000)
   * fileDownload(url, Paths.get(setting.targetPath));
   * }
   */
  public static String getURL(final String body) {
    final Matcher match = FileUploader.urlJsonPatetrn.matcher(body);
    boolean _matches = match.matches();
    if (_matches) {
      return match.group(1);
    } else {
      return "";
    }
  }
  
  public static FileUploader.CompileErrorDialog showCompileErrorDialog(final String errorMessage) {
    try {
      FileUploader.CompileErrorDialog _xblockexpression = null;
      {
        final AstahAPI astahAPI = AstahAPI.getAstahAPI();
        IViewManager _viewManager = astahAPI.getViewManager();
        JFrame _mainFrame = _viewManager.getMainFrame();
        _xblockexpression = new FileUploader.CompileErrorDialog(_mainFrame, errorMessage);
      }
      return _xblockexpression;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
