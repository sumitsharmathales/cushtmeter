package com.example.demo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AvatarUrls {
	
    @JsonProperty("48x48") 
    public String _48x48;
    @JsonProperty("24x24") 
    public String _24x24;
    @JsonProperty("16x16") 
    public String _16x16;
    @JsonProperty("32x32") 
    public String _32x32;
	public String get_48x48() {
		return _48x48;
	}
	public void set_48x48(String _48x48) {
		this._48x48 = _48x48;
	}
	public String get_24x24() {
		return _24x24;
	}
	public void set_24x24(String _24x24) {
		this._24x24 = _24x24;
	}
	public String get_16x16() {
		return _16x16;
	}
	public void set_16x16(String _16x16) {
		this._16x16 = _16x16;
	}
	public String get_32x32() {
		return _32x32;
	}
	public void set_32x32(String _32x32) {
		this._32x32 = _32x32;
	}

    
    
}
