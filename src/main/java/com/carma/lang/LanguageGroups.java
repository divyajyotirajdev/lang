/*
 * @author DivyaJyoti Rajdev
 */

package com.carma.lang;

import java.util.*;

public class LanguageGroups {
	private String lang;
    private List<DocProperty> docs;
    public LanguageGroups(String lang) {
        this.lang = lang;
        this.docs = new ArrayList<DocProperty>();
    }
    
    public void addDoc(DocProperty doc) {
    	this.docs.add(doc);
    }
    
    public String getLang() {
    	return lang;
    }
    
    public List<DocProperty> getDocs() {
    	return docs;
    }
    
    public void addManyDocs(List<DocProperty> docs) {
    	this.docs.addAll(docs);
    }
    
    /*
	 * gets all distinct languages marked as an arraylist
	 * ----------------------------------------------------------------------------------
	 * @input - arrayList of all languages in database
	 * @output - creates blank "LanguageGroups" based on language from data base, empty buckets
	 */
    public static List<LanguageGroups> groupedCollections(List<String> allLang) {
    	List<LanguageGroups> allColl = new ArrayList<LanguageGroups>();
    	for (String lang: allLang) {
    		LanguageGroups lg= new LanguageGroups(lang);
    		allColl.add(lg);
    	}
    	return allColl;
    }

}
