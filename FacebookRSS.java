import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class FacebookRSS {
	
	public static void main(String[] args) {
		System.out.println(readURL("https://www.facebook.com/feeds/notifications.php?id=660703270&viewer=660703270&key=AWgUEffmMzO-W3D7&format=rss20"));
	}
	
	public static String readURL(String address) {
		try {
			URL rss = new URL(address);
			BufferedReader in = new BufferedReader(new InputStreamReader(rss.openStream()));
			String source = "";
			String line;
		
			while((line = in.readLine()) != null) {
				if(line.contains("<title><![CDATA[")) {
					int start = line.indexOf("<title><![CDATA[");
					String temp = line.substring(start);
					temp = temp.replace("<title><![CDATA[", "");
					
					if (!line.contains("]]></title>")) {
						while ((line = in.readLine()).contains("]]></title>"));
					}
					
					else {
						int end = temp.indexOf("]]></title>");
						temp = temp.substring(0, end);
						source += temp + "\n";
						//System.out.println(source);
					}
				}
			}
		
			in.close();
			return source;		
		}
		
		catch (MalformedURLException me) {
			System.out.println("Malformed URL");
		}
		
		catch (IOException ioe) {
			System.out.println("Could not read contents.");
		}
		
		return null;
	}

}
