
/// ***************************************************************************
//                         Seyedehzahra Hosseini 
//                         Hamid Bagheri
//******************************************************************************



import java.io.IOException;

public class MyWikiCrawler {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String[] topics = { "Hash", "Table" };
		WikiCrawler w = new WikiCrawler("/wiki/Hash_table", topics, 5, "test2.txt");
		w.crawl();
	}

}
