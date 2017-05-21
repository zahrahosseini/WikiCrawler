
/// ***************************************************************************
//                         Seyedehzahra Hosseini 
//                         Hamid Bagheri
//******************************************************************************

import java.io.IOException;

public class WikiTennisCrawler {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String[] topics = { "tennis", "grand slam" };
		WikiCrawler w = new WikiCrawler("/wiki/Tennis", topics, 1000, "WikiTennisGraph.txt");
		w.crawl();
	}

}
