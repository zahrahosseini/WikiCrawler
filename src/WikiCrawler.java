
/// ***************************************************************************
//                         Seyedehzahra Hosseini 
//                         Hamid Bagheri
//******************************************************************************


import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.swing.text.AttributeSet;
import javax.swing.text.Element;
import javax.swing.text.ElementIterator;
import javax.swing.text.StyleConstants;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;

class GenQueue<E> {
	private LinkedList<E> list = new LinkedList<E>();

	public void enqueue(E item) {
		list.addLast(item);
	}

	public E dequeue() {
		return list.poll();
	}

	public boolean hasItems() {
		return !list.isEmpty();
	}

	public int size() {
		return list.size();
	}

	public void addItems(GenQueue<? extends E> q) {
		while (q.hasItems())
			list.addLast(q.dequeue());
	}

	public boolean has(E item) {
		return list.contains(item);
	}
}

public class WikiCrawler {
	public GenQueue Queue = new GenQueue<String>();
	public LinkedList<String> list = new LinkedList<String>();
	public PrintWriter writer;

	public String getSeedUrl() {
		return seedUrl;
	}

	public void setSeedUrl(String seedUrl) {
		this.seedUrl = seedUrl;
	}

	public String[] getKeyword() {
		return keyword;
	}

	public void setKeyword(String[] keyword) {
		this.keyword = keyword;
	}

	public Integer getMax() {
		return max;
	}

	public void setMax(Integer max) {
		this.max = max;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	String seedUrl;
	String[] keyword;
	Integer max;
	String fileName;
	String BASE_URL = "https://en.wikipedia.org";
	public static int numReq = 0;
	// This code is referenced by
	// http://www.chenirvine.org/projects/wikipedia_crawler.htm
	public static String getWebPageByUrl(String m_strUrl) throws Exception {

		if (numReq > 100) {
			numReq = 0;
			TimeUnit.SECONDS.sleep(5);

		} else
			numReq++;
		String strWebpage = ""; // return webpage
		InputStream in = null;
		OutputStream out = null;
		URL url;
		byte[] buffer = new byte[1024];
		int bytes_read; // the actual bytes for every read

		// System.out.printf("Crawler: The webpage corresponding to URL %s is to
		// be crawlered\n", m_strUrl);

		// crawl the corresponding webpage
		try {
			out = new ByteArrayOutputStream();
			url = new URL(m_strUrl); // create URL
			in = url.openStream();

			while ((bytes_read = in.read(buffer)) != -1) {
				out.write(buffer, 0, bytes_read);
			}
			strWebpage = out.toString();

			// close InputStream and OutputStream
			in.close();
			out.close();
		} catch (IOException e) {
			System.out.println("Crawler: IOException! " + e.toString());
			return "";
		}

		return strWebpage;
	}
// This function go through the RAW file of given URL 
	//, return True if it has all of keywords
	// else return false;
	private boolean insideCrawl(String str) throws Exception {
		{
			if (str.length() > 0) {
				int endIndex = str.lastIndexOf("/wiki/");
				if (endIndex != -1) {

					String keyWord = str.substring(endIndex + 6, str.length());

					String overviewUrl = "";
					overviewUrl = "https://en.wikipedia.org/w/index.php?title=";
					overviewUrl += keyWord;
					overviewUrl += "&action=raw";
					String s = "";
					if (!CheckPolite("/w/index.php?title=" + keyWord + "&action=raw"))
						return false;
					s = this.getWebPageByUrl(overviewUrl);

					String[] K = getKeyword();
					boolean f = true;
					for (int i = 0; i < K.length; i++) {
						if (s.toLowerCase().indexOf(K[i].toLowerCase()) == -1) {
							return false;
						}
					}
					return f;
				}
			}
		}
		return false;
	}

	public Set<String> Visited = new HashSet<String>();
	public Set<String> PVisited = new HashSet<String>();
// This function Crawl a webpage Use Insidecrawl to detect whether add it as a 
	//relevent URL or not
	public void mycrawl2() throws Exception {
		if (!CheckPolite(seedUrl))
			return;
		if (Visited.size() == max)
			return;
		if (Visited.contains(seedUrl))
			return;
		else
			Visited.add(seedUrl);
		if (PVisited.size() < max)
			PVisited.add(seedUrl);
		else if (!PVisited.contains(seedUrl))
			return;
		String HTMLPage = getWebPageByUrl(BASE_URL + seedUrl);
		String findHref = "<a href=";

		String fingP = "<p>";
		int index_start = HTMLPage.indexOf(fingP);
		HTMLPage = HTMLPage.substring(index_start + 3, HTMLPage.length());

		index_start = HTMLPage.indexOf(findHref);

		while (index_start != -1) {

			int Endindex = HTMLPage.indexOf('"', index_start + 8);
			Endindex = HTMLPage.indexOf('"', Endindex + 1);

			String Href = HTMLPage.substring(index_start + 9, Endindex);
			if (!Href.contains(":") && !Href.contains("#") && Href.indexOf("/wiki/") == 0) {
				if ((PVisited.size() < max) || (PVisited.size() >= max && PVisited.contains(Href))) {
					if (!Visited.contains(Href)) {
						if (!list.contains(Href)) {
							if (insideCrawl(Href)) {
								PVisited.add(Href);
								list.add(Href);
								//System.out.println(seedUrl + " * " + Href);
								writer.println(seedUrl + " " + Href);
								Queue.enqueue(Href);

							}
						}

					}

					else

					if (Visited.contains(Href)) {
						if (!list.contains(Href)) {
							PVisited.add(Href);

							list.add(Href);
							//System.out.println(seedUrl + " & " + Href);
							writer.println(seedUrl + " " + Href);

						}

					}
				}
			}

			HTMLPage = HTMLPage.substring(Endindex, HTMLPage.length());

			index_start = HTMLPage.indexOf(findHref);

		}

	}

	public Set<String> Polite = new HashSet<String>();
	public String PolitePage = "";

	public String getPolitePage() {
		return PolitePage;
	}

	public void setPolitePage(String politePage) {
		PolitePage = politePage;
	}
// This function Set politeness page.
	public void politeness() throws Exception {
		String s = getWebPageByUrl("https://en.wikipedia.org/robots.txt");
		setPolitePage(s);
	}
// This function check the politeness based on robot.txt
	public boolean CheckPolite(String URL) {
		Boolean f = true;
		if (PolitePage.indexOf(URL) != -1) {
			f = false;
			String Allow1 = "/w/api.php?action=mobileview&";
			String Allow2 = "/w/load.php?";
			String Allow3 = "/api/rest_v1/?doc";
			if (Allow1.indexOf(URL) != -1 || Allow2.indexOf(URL) != -1 || Allow3.indexOf(URL) != -1)
				f = true;
		} else {
			f = true;
		}
		return f;
	}

	
// This function Crawl with Using BFS
	public void crawl() throws Exception {
		writer = new PrintWriter(fileName, "UTF-8");
		writer.println(max);

		politeness();
		int no = 1;
		while (Queue.size() > 0) {

			seedUrl = (String) Queue.dequeue();
			list.add(seedUrl);
			//System.out.println("VV" + no + "VV" + seedUrl);

			mycrawl2();
			no++;
			list.clear();
		}
		writer.close();

	}
// Set Variable
	public WikiCrawler(String seedUrl, String[] keyword, Integer max, String fileName) throws IOException {
		super();
		this.seedUrl = seedUrl;
		this.keyword = keyword;
		this.max = max;
		this.fileName = fileName;
		Queue.enqueue(seedUrl);
	}

}
