package model;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Datetime implements java.io.Serializable{

@SerializedName("value")
@Expose
private String value;
@SerializedName("unit")
@Expose
private String unit;

/**
* 
* @return
* The value
*/
public String getValue() {
return value;
}

/**
* 
* @param value
* The value
*/
public void setValue(String value) {
this.value = value;
}

/**
* 
* @return
* The unit
*/
public String getUnit() {
return unit;
}

/**
* 
* @param unit
* The unit
*/
public void setUnit(String unit) {
this.unit = unit;
}

}
