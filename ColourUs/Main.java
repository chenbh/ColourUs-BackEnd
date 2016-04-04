/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ColourUs;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import twitter4j.*;
import twitter4j.conf.Configuration;

/**
 *
 * @author Daniel
 */
public class Main {

	Trending t;
	Stream s;
	Parser p;
	Calculation c;
	int num = 0;

	ArrayList<String> countries = new ArrayList<String>();
	ArrayList<String[]> texts = new ArrayList<String[]>();
	ArrayList<String[]> trendingPhrases = new ArrayList<String[]>();
	ArrayList<String> emotions = new ArrayList<String>();

	public Main() {
		OAuth auth = new OAuth();
		Configuration config = auth.getConfig();

		TwitterFactory tf = new TwitterFactory(config);
		Twitter twitter = tf.getInstance();

		t = new Trending(twitter, 10);
		s = new Stream(config, t);
		p = new Parser(t);
		c = new Calculation();
		Timing timer = new Timing(this);
		s.run();
		timer.start(120);
	}

	public void buzz() {
		p.setData(s.getData());

		update();
		s.resetData();
		p.resetData();
	}

	private void update() {
		countries.clear();
		texts.clear();
		trendingPhrases.clear();
		emotions.clear();
		for (String s : t.getCountries()) {
			countries.add(s);
			texts.add(p.getTextsFor(s));
			trendingPhrases.add(t.getTopTrends(s));
		}
		doStuff();
	}
	
	private void doStuff () {
		for (int i = 0; i < countries.size(); i++) {
			emotions.add(c.getTotalScore(texts.get(i)));
		}
		for (int i = 0; i < countries.size(); i++) {
			System.out.println(countries.get(i) + " has a total of " + texts.get(i).length + " tweet(s).");
			System.out.println("The country is currently feeling: " + emotions.get(i) + ". The tweet(s) are:");
			for (String s : texts.get(i)) {
				System.out.println("[#]:" + s);
			}
		}
	}

	private void doPrintingStuff() {
		try {
			PrintWriter out = new PrintWriter(new FileWriter(new File("data" + num + ".txt")));
			for (int i = 0; i < countries.size(); i++) {
				emotions.add(c.getTotalScore(texts.get(i)));
			}
			for (int i = 0; i < countries.size(); i++) {
				out.print(countries.get(i));
				for (String s : trendingPhrases.get(i)) {
					out.print(" " + s);
				}
				out.println(" " + emotions.get(i));
			}
			num ++;
			out.close();
			System.out.println ("Wrote report number " + num);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Main m = new Main();
	}
}
