package model;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class HObject {

	@SerializedName("email")
	@Expose
	private String email;
	@SerializedName("humidity")
	@Expose
	private Humidity humidity;
	@SerializedName("datetime")
	@Expose
	private Datetime datetime;

	/**
	 * 
	 * @return The email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * 
	 * @param email
	 *            The email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * 
	 * @return The humidity
	 */
	public Humidity getHumidity() {
		return humidity;
	}

	/**
	 * 
	 * @param humidity
	 *            The humidity
	 */
	public void setHumidity(Humidity humidity) {
		this.humidity = humidity;
	}

	/**
	 * 
	 * @return The datetime
	 */
	public Datetime getDatetime() {
		return datetime;
	}

	/**
	 * 
	 * @param datetime
	 *            The datetime
	 */
	public void setDatetime(Datetime datetime) {
		this.datetime = datetime;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}