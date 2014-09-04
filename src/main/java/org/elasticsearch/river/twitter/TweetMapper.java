package org.elasticsearch.river.twitter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.common.Strings;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.ESLoggerFactory;

import twitter4j.JSONException;
import twitter4j.JSONObject;
import twitter4j.Status;

public class TweetMapper {
	
	private Map<String,String> trackListing;
	private Map<String,String> followListings;
	
	ESLogger logger = ESLoggerFactory.getLogger(TweetMapper.class.getName());
	
	public TweetMapper(){
		trackListing = new HashMap<String,String>();
		followListings = new HashMap<String,String>();

	}

	private void addMapping(Map<String,String> map , String ruleName, String listString) {
		if(listString == null || listString.isEmpty()){
			logger.info("Empty string detected for rule " + ruleName);
			return;
		}
		for(String key : Strings.commaDelimitedListToStringArray(listString)){
			map.put(key, ruleName);
		}
	}

	public void addTrackMapping(String ruleName, String trackList) {
		addMapping(trackListing,ruleName,trackList);
	}

	public void addFollowMapping(String ruleName, String followList) {
		addMapping(followListings,ruleName,followList);		
	}

	public List<Map<String,String>> getContextInfo(Status status) throws JSONException {
		List<Map<String,String>> contexts = new ArrayList<Map<String,String>>();
		String text = status.getText();
		for(String trackKeyword : trackListing.keySet()){
			if(text.indexOf(trackKeyword) != -1){
				Map<String,String> context = new HashMap<String,String>();
				context.put("type", "tracks");
				context.put("keyword", trackKeyword);
				context.put("rule", trackListing.get(trackKeyword));
				contexts.add(context);
			}
		}
		return contexts;
	}
	

}
