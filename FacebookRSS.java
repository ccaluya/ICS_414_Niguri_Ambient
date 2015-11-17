import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;


public class FacebookRSS {
	
	public static void main(String[] args) {
		System.out.println("Instructions on How to Use (please read):\n");
		System.out.println("1. Go to https://www.facebook.com.");
		System.out.println("2. Login to your account (if you have one).");
		System.out.println("3. Go to https://www.facebook.com/notifications.");
		System.out.println("4. Click on RSS in 'Get notifications via'.");
		System.out.println("5. Copy the URL link.");
		System.out.println("6. Paste the URL link here and get your notifications!");
		
		System.out.print("Please enter your RSS URL: ");
		Scanner keybd = new Scanner(System.in);
		String URL = keybd.next();
		
		System.out.println();
		System.out.println("Please wait while we process your notifications ...\n");
		System.out.println(readURL(URL));
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
