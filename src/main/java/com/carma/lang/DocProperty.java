/*
 * @author DivyaJyoti Rajdev
 */

package com.carma.lang;

import org.bson.types.ObjectId;

public class DocProperty {
	private String headline;
	private String content;
	public DocProperty(String headline,String content) {
		this.headline=headline;
		this.content=content; 
	}
	public String getContent() {
		return content;
	}
	public String getHeadline() {
		return headline;
	}
}
