package com.github.luismoramedina;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;

/**
 * @author luismoramedina
 */
public class SearchInElastic {

	private static final String INDEX_NAME = "cinema";
	private static final String TYPE = "film";
	private static final String ID = "1";

	public static void main(String[] args) throws Exception {
		// on startup
		TransportClient client = new PreBuiltTransportClient(Settings.EMPTY)
				.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
		//Add transport addresses and do something with the client...

		GetResponse response = client.prepareGet(INDEX_NAME, TYPE, ID).get();
		String title = (String) response.getSource().get("title");
		System.out.println("Get: " + title);

		//Simple term search
		SearchResponse searchResponse = client.prepareSearch(INDEX_NAME)
				.setTypes(TYPE)
				.setSearchType(SearchType.DEFAULT)
				.setQuery(QueryBuilders.matchQuery("title", "star"))
				.get();

		SearchHits hits = searchResponse.getHits();
		System.out.println("Total hits: " + hits.totalHits());

		for (SearchHit next : hits) {
			Object titleValue = next.getSource().get("title");
			System.out.println("Found: " + titleValue);
		}
		// on shutdown
		client.close();
	}
}
