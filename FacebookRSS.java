import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Scanner;
import java.util.TimeZone;


public class FacebookRSS {
	
	public static void main(String[] args) {
		// Instructions
		System.out.println("Instructions on How to Use (please read):\n");
		System.out.println("1. Go to https://www.facebook.com.");
		System.out.println("2. Login to your account (if you have one).");
		System.out.println("3. Go to https://www.facebook.com/notifications.");
		System.out.println("4. Click on RSS in 'Get notifications via'.");
		System.out.println("5. Copy the URL link.");
		System.out.println("6. Paste the URL link here and get your notifications!");
		
		// User Input
		System.out.print("Please enter your RSS URL: ");
		Scanner keybd = new Scanner(System.in);
		String URL = keybd.next();
		
		System.out.println();
		System.out.println("Please wait while we process your notifications ...\n");
		System.out.println(readURL(URL));
	}
	
	public static String readURL(String address) {
		try {
			// Facebook URL Reader
			URL rss = new URL(address);
			BufferedReader in = new BufferedReader(new InputStreamReader(rss.openStream()));
			String source = "";
			String line;
			boolean weekOld = false;
		
			while((line = in.readLine()) != null && !weekOld) {
				// Notification Titles
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
					}
				}
				
				if(line.contains("<link>")) {
					int start = line.indexOf("<link>");
					String temp = line.substring(start);
					temp = temp.replace("<link>", "");
					int end = temp.indexOf("</link>");
					temp = temp.substring(0, end);
					
					address = address.replace("/feed", "");
					if(address.contains(temp)) {
						System.out.println("This is the notification link.");
					}
					
					else {
						source += "<a href=\"" + temp + "\">Notification link</a>" + "\n";
					}
				}
				
				// Notification Dates
				if(line.contains("<pubDate>")) {	
					// Current Date
					Calendar currCalendar = Calendar.getInstance(TimeZone.getTimeZone("Pacific/Honolulu"));
					String curr = currCalendar.getTime().toString();
					String currMonth = curr.substring(4,7);
					int currDate = Integer.parseInt(curr.substring(8,10));
					int currYear = Integer.parseInt(curr.substring(24,28));
					
					int monthInt = 0;
					switch(currMonth) {
						case "Jan":
							monthInt = 1;
							break;
						case "Feb":
							monthInt = 2;
							break;
						case "Mar":
							monthInt = 3;
							break;
						case "Apr":
							monthInt = 4;
							break;
						case "May":
							monthInt = 5;
							break;
						case "Jun":
							monthInt = 6;
							break;
						case "Jul":
							monthInt = 7;
							break;
						case "Aug":
							monthInt = 8;
							break;
						case "Sep":
							monthInt = 9;
							break;
						case "Oct":
							monthInt = 10;
							break;
						case "Nov":
							monthInt = 11;
							break;
						case "Dec":
							monthInt = 12;
							break;
					}
					
					// Current Date Conversion (1 week earlier)
					currDate = currDate - 7;
					if (currDate < 1) {
						monthInt = monthInt - 1;
						if (monthInt < 1) {
							currYear = currYear - 1;
							monthInt = monthInt + 12;
						}
						
						if (monthInt == 1 || monthInt == 3 || monthInt == 5 || monthInt == 7
								|| monthInt == 8 || monthInt == 10 || monthInt == 12) {
							currDate = currDate + 31;
						}
						
						else if (monthInt == 4 || monthInt == 6 || monthInt == 9 || monthInt == 11) {
							currDate = currDate + 30;
						}
						
						else if (monthInt == 2) {
							if ((currYear % 400 == 0) || (currYear % 4 == 0 && currYear % 100 != 0)) {
								currDate = currDate + 29;
							}
							
							else {
								currDate = currDate + 28;
							}
						}
					}
					
					// Event Date
					int start = line.indexOf("<pubDate>");
					String temp = line.substring(start);
					temp = temp.replace("<pubDate>", "");
					int end = temp.indexOf("-1000</pubDate>");
					temp = temp.substring(0, end);
					String eventWeek = temp.substring(0,3);
					int eventDate = Integer.parseInt(temp.substring(5,7));
					String eventMonth = temp.substring(8,11);
					int eventYear = Integer.parseInt(temp.substring(12,16));
					
					int eventInt = 0;
					switch(eventMonth) {
					case "Jan":
						eventInt = 1;
						break;
					case "Feb":
						eventInt = 2;
						break;
					case "Mar":
						eventInt = 3;
						break;
					case "Apr":
						eventInt = 4;
						break;
					case "May":
						eventInt = 5;
						break;
					case "Jun":
						eventInt = 6;
						break;
					case "Jul":
						eventInt = 7;
						break;
					case "Aug":
						eventInt = 8;
						break;
					case "Sep":
						eventInt = 9;
						break;
					case "Oct":
						eventInt = 10;
						break;
					case "Nov":
						eventInt = 11;
						break;
					case "Dec":
						eventInt = 12;
						break;
					}
					
					// Event Date Checker
					if(currYear >= eventYear) {
						if (monthInt >= eventInt) {
							if (currDate >= eventDate) {
								weekOld = true;
								break;
							}
						}
					}
					
					String parseTemp = "Event occured: " + eventWeek + ", " + eventMonth
							+ " " + eventDate + ", " + eventYear;
					source += parseTemp + "\n\n";
				}
			}
		
			in.close();
			source = source.substring(0, source.lastIndexOf("\n\n"));
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
