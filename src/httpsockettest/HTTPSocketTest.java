/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package httpsockettest;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Tom Wang
 */
public class HTTPSocketTest {

	public static void main(String[] args) throws Exception {
//		testGet();
//		testPost();
		httpPost();
	}

	public static void httpPost() throws Exception {

		HttpURLConnection connection = (HttpURLConnection) new URL("http://localhost:8080" + _URL_LOGIN_POST).openConnection();

		String[][] parameters =
			{{_P_P_ID_PREFIX + "_formDate",
				String.valueOf(System.currentTimeMillis())
			},
			{_P_P_ID_PREFIX + "_saveLastPath", "false"},
			{_P_P_ID_PREFIX + "_redirect", ""},
			{_P_P_ID_PREFIX + "_doActionAfterLogin", "false"},
			{_P_P_ID_PREFIX + "_login", "test@liferay.com"},
			{_P_P_ID_PREFIX + "_password",
				"test"
			},
			{_P_P_ID_PREFIX + "_checkboxNames", "rememberMe"}
			};

		String data = "";
		if (parameters != null) {
				boolean first = true;

				for (String[] parameter : parameters) {
					if (first) {
						first = false;
					}
					else {
						data += '&';
					}

					data += parameter[0];
					data += '=';
					data += URLEncoder.encode(parameter[1], "UTF-8");
				}
			}

		connection.setDoOutput(true);
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		connection.setRequestProperty("Content-Length", String.valueOf(data.length()));

		connection.connect();

		OutputStream os = connection.getOutputStream();
		os.write(data.getBytes());


		Map<String, List<String>> map = connection.getHeaderFields();
		for (Entry<String, List<String>> entry : map.entrySet()) {
			System.out.println(entry.getKey() + "=" + entry.getValue());
		}
	}

	public static void testGet() throws IOException {
		Socket socket = new Socket("localhost", 8080);

		PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), false);
//		PrintWriter printWriter = new PrintWriter(new File("/home/tom/temp/test.txt"));

		printWriter.print("POST /c/portal/login?p_l_id=20143 HTTP/1.0\r\nAccept: text/plain, text/html, text/*\r\n\r\n");

        printWriter.flush();


		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

		bufferedReader.lines().forEach(
			line -> {
			System.out.println(line);
		});

		printWriter.close();
		bufferedReader.close();
		socket.close();
	}

	public static void testPost() throws IOException {
		Socket socket = new Socket("localhost", 8080);

//		PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), false);
		PrintWriter printWriter = new PrintWriter(new File("/home/tom/temp/test.txt"));

		String[][] parameters =
			{{_P_P_ID_PREFIX + "_formDate",
				String.valueOf(System.currentTimeMillis())
			},
			{_P_P_ID_PREFIX + "_saveLastPath", "false"},
			{_P_P_ID_PREFIX + "_redirect", ""},
			{_P_P_ID_PREFIX + "_doActionAfterLogin", "false"},
			{_P_P_ID_PREFIX + "_login", "test@liferay.com"},
			{_P_P_ID_PREFIX + "_password",
				"test"
			},
			{_P_P_ID_PREFIX + "_checkboxNames", "rememberMe"}
			};


//		printWriter.print("GET / HTTP/1.0\r\nAccept: text/plain, text/html, text/*\r\n\r\n");
		printWriter.print("POST " + _URL_LOGIN_POST + " HTTP/1.0\r\n");

		String data = "";
		if (parameters != null) {
				boolean first = true;

				for (String[] parameter : parameters) {
					if (first) {
						first = false;
					}
					else {
						data += '&';
					}

					data += parameter[0];
					data += '=';
					data += URLEncoder.encode(parameter[1], "UTF-8");
				}
			}
		printWriter.write("Content-Length: " + data.length() + "\r\n");
		printWriter.write("Content-Type: application/x-www-form-urlencoded\r\n");

//		if (parameters != null) {
//				boolean first = true;
//
//				for (String[] parameter : parameters) {
//					if (first) {
//						first = false;
//					}
//					else {
//						printWriter.print('&');
//					}
//
//					printWriter.print(parameter[0]);
//					printWriter.print('=');
//					printWriter.print(URLEncoder.encode(parameter[1], "UTF-8"));
//				}
//			}

		printWriter.print("\r\n");

		printWriter.write(data);
        printWriter.flush();


		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

		bufferedReader.lines().forEach(
			line -> {
			System.out.println(line);
		});

		printWriter.close();
		bufferedReader.close();
		socket.close();
	}

	public static void googleApiSolution() throws IOException {
		HttpTransport httpTransport = new NetHttpTransport();

		HttpRequestFactory httpRequestFactory = httpTransport.createRequestFactory();

		HttpRequest httpRequest = httpRequestFactory.buildGetRequest(new GenericUrl("http://localhost:8080"));

		HttpResponse httpResponse = httpRequest.execute();

		System.out.println(httpResponse.getHeaders());
	}

	private static final String _KEY_HOME_PAGE = "Liferay.currentURL";

	private static final String _KEY_LOGIN = "You are signed in as";

	private static final String _P_P_ID =
		"com_liferay_login_web_portlet_LoginPortlet";

	private static final String _P_P_ID_PREFIX = "_" + _P_P_ID;

	private static final String _URL_LOGIN_POST =
		"/web/guest/welcome?p_p_id=" + _P_P_ID + "&p_p_lifecycle=1&" +
			"p_p_state=normal&p_p_mode=view&p_p_col_id=column-1&" +
				"p_p_col_count=1&" + _P_P_ID_PREFIX +
					"_javax.portlet.action=/login/login&" + _P_P_ID_PREFIX +
						"_mvcRenderCommandName=/login/login";

	private static final String _URL_LOGIN_REDIRECT = "/";

	private static final String _URL_LOGOUT ="/c/portal/logout";

	private static final String _URL_REDIRECT = "/c";

}
