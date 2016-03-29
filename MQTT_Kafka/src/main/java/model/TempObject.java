package model;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class TempObject implements java.io.Serializable {

	@SerializedName("temp")
	@Expose
	private Temp temp;
	@SerializedName("datetime")
	@Expose
	private Datetime datetime;

	/**
	 * 
	 * @return The temp
	 */
	public Temp getTemp() {
		return temp;
	}

	/**
	 * 
	 * @param temp
	 *            The temp
	 */
	public void setTemp(Temp temp) {
		this.temp = temp;
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