package com.demo.tempstorm.Temp_Storm;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class NotificationBolt1 implements IRichBolt {
	private OutputCollector collector;
	// private Map<String, String> cache;
	private static final Logger logger = LogManager.getLogger(NotificationBolt1.class);
	final String username = "loudywendev@gmail.com";
	final String password = "loudywen198316";
	private int limit = 25;
	final int addMinuteTime = 2;
	private Map<String, String> timeInterval;

	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
		// cache = new HashMap<String, String>();
		timeInterval = new HashMap<String, String>();
	}

	public void execute(Tuple input) {
		if (NumberUtils.createDouble(input.getString(1)) >= limit) {
			// logger.info("======================== greater than 50");
			if (input.getStringByField("email") != null) {

				if (timeInterval.get(input.getStringByField("email")) == null) {
					timeInterval.put(input.getStringByField("email"), input.getStringByField("dateTimeValue"));
					sendEmail(input.getStringByField("email"), input.getStringByField("tempValue"));
					sendTwitter(input.getStringByField("tempValue"), input.getStringByField("dateTimeValue"));
				} else {

					Date oldEntryTime;
					try {
						oldEntryTime = DateUtils.parseDate(timeInterval.get(input.getStringByField("email")), "yyyy-MM-dd HH:mm:ss");
						Date diffTime = DateUtils.addMinutes(oldEntryTime, addMinuteTime);
						Date currentTime = new Date();
						if (currentTime.getTime() > diffTime.getTime()) {
							sendEmail(input.getStringByField("email"), input.getStringByField("tempValue"));

							String updatedTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(currentTime);
							sendTwitter(input.getStringByField("tempValue"), updatedTime);
							timeInterval.put(input.getStringByField("email"), updatedTime);
						}
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				collector.emit(new Values(input.getStringByField("email"),input.getStringByField("tempValue"),timeInterval.get(input.getStringByField("email"))));
				collector.ack(input);
			}
		} else {
			// logger.info("======================== no action");
			collector.ack(input);
		}
		
		//test http request
		collector.emit(new Values(input.getStringByField("email"),input.getStringByField("tempValue"),timeInterval.get(input.getStringByField("email"))));
		collector.ack(input);
	}

	public void cleanup() {
		// TODO Auto-generated method stub

	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("email", "tempValue", "dateTimeValue"));

	}

	public Map<String, Object> getComponentConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

	private void sendTwitter(String temp, String time) {
		Twitter twitter = TwitterFactory.getSingleton();
		String message = "Oops~~~The current temperature of your refrigerator is " + temp + "\u00b0F at " + time + " which is over " + limit + "\u00b0F!";
		try {
			Status status = twitter.updateStatus(message);

			logger.info("| Posted twitte: " + status.getText());
			logger.info("-----------------------NotificationBolt-----------------------");
		} catch (TwitterException e) {

		}
	}

	private void sendEmail(String toEmail, String temp) {
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("loudywendev@gmail.com"));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
			message.setSubject("Temperature Notice");
			message.setText("Dear User," + "\n\nThe current temperature of your refrigerator is " + temp + "\u00b0F which is over " + limit + "\u00b0F.");

			Transport.send(message);
			logger.info("-----------------------NotificationBolt-----------------------");
			logger.info("| Sent Email to: " + toEmail);
			logger.info("| message: " + "Dear User," + "The current temperature of your refrigerator is " + temp + "\u00b0F which is over " + limit + "\u00b0F.");
			// logger.info("-----------------------NotificationBolt-----------------------");
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
}
