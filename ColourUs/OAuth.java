/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ColourUs;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import twitter4j.*;
import twitter4j.auth.*;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

/**
 *
 * @author Daniel
 */
public class OAuth {

    private static final String C_KEY;
    private static final String C_SECRET;
    private static final String A_TOKEN;
    private static final String A_SECRET;

    private static final String C2_KEY;
    private static final String C2_SECRET;
    private static final String A2_TOKEN;
    private static final String A2_SECRET;
    
    private Configuration config;

    public OAuth() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(C2_KEY)
                .setOAuthConsumerSecret(C2_SECRET)
                .setOAuthAccessToken(A2_TOKEN)
                .setOAuthAccessTokenSecret(A2_SECRET);
        config = cb.build();
    }

    public Configuration getConfig() {
        return config;
    }

    private void reauthorize() throws Exception {
        // In case we lose the A_SECRET
        Twitter twitter = TwitterFactory.getSingleton();
        twitter.setOAuthConsumer(C_KEY, C_SECRET);
        RequestToken requestToken = twitter.getOAuthRequestToken();
        AccessToken accessToken = null;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (null == accessToken) {
            System.out.println("Open the following URL and grant access to your account:");
            System.out.println(requestToken.getAuthorizationURL());
            System.out.print("Enter the PIN(if aviailable) or just hit enter.[PIN]:");
            String pin = br.readLine();
            try {
                if (pin.length() > 0) {
                    accessToken = twitter.getOAuthAccessToken(requestToken, pin);
                } else {
                    accessToken = twitter.getOAuthAccessToken();
                }
            } catch (TwitterException te) {
                if (401 == te.getStatusCode()) {
                    System.out.println("Unable to get the access token.");
                } else {
                    te.printStackTrace();
                }
            }
        }
        show ((int) twitter.verifyCredentials().getId() , accessToken);
    }

    private static void show(int useId, AccessToken accessToken) {
        System.out.println("key: " + C_KEY);
        System.out.println("key secret:" + C_SECRET);
        System.out.println("token: " + accessToken.getToken());
        System.out.println("secret: " + accessToken.getTokenSecret());
    }
}
