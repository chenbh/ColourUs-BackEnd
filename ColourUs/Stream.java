/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ColourUs;

/**
 *
 * @author Daniel
 */
import java.util.ArrayList;
import java.util.LinkedList;
import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.Configuration;

public class Stream extends Thread implements StatusListener {

    ArrayList<String> users = new ArrayList<>();
    TwitterStream twitterStream;
    FilterQuery filter;
    LinkedList<Status> data = new LinkedList<> ();
    Trending trending;

    public Stream(Configuration config, Trending trending) {
        this.trending = trending;

        TwitterStreamFactory tsf = new TwitterStreamFactory(config);
        twitterStream = tsf.getInstance();
        
        twitterStream.addListener(this);
        filter = new FilterQuery();
        filter.track (new String[] {"I", "you", "he", "she", "them", "they", "us", "our", "her", "him", "they're", "I'm", "my", "your", "is", "was", "are", "were"});
     }

    @Override
    public void run() {
        twitterStream.filter(filter);
    }
    
    public int total () {
        return data.size();
    }
    public LinkedList<Status> getData () {
        return data;
    }
    
    public void resetData () {
        data.clear ();
        users.clear ();
    }

    @Override
    public void onStatus(Status status) {
        if ((status.getPlace () != null) && !users.contains ((status.getUser ().getName()))
                && trending.validate (status)) {
            data.add (status);
            users.add (status.getUser().getName());
        }
    }
        
    @Override
    public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
        
    }

    @Override
    public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
        
    }

    @Override
    public void onScrubGeo(long userId, long upToStatusId) {
        
    }

    @Override
    public void onStallWarning(StallWarning warning) {
        
    }

    @Override
    public void onException(Exception ex) {
        ex.printStackTrace();
    }
}
