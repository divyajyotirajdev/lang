/*
 * @author DivyaJyoti Rajdev
 */

package com.carma.lang;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class LangDetection extends Detector{
	public static final double REDEFINE_THRESHOLD = 0.95;
	
	public LangDetection(DetectorFactory factory) throws LangDetectException {
		super(factory);
		Path currentRelativePath = Paths.get("");
		String profileDirectory = currentRelativePath.toAbsolutePath().toString() + "/profiles/";
		//System.out.println(profileDirectory);
		DetectorFactory.loadProfile(profileDirectory);
	}

	/*
	 * puts docs in relevant buckets after detecting language with greater than
	 * REDEFINE_THRESHOLD confidence
	 * ----------------------------------------------------------------------------------
	 * @input - current language, all headlines&content from DB, empty bucket collections
	 * @output - returns all filled buckets
	 */
	public static List<LanguageGroups> languageDetected(String DBlang, ArrayList<DocProperty> allData, List<LanguageGroups> allColl){
		try{
        	//String text="//https://regex101.com/r/vM7wT6/2 good morning tu parle ingles";
			LanguageGroups defaultColl=whichCollection(allColl, DBlang);
        	for (DocProperty doc:allData) {
        		boolean savedDoc=false;
        		Detector detector;
        		detector = DetectorFactory.create();
        		detector.append(doc.getContent());
       			try {
       				String langDetected= detector.detect();
       				if (langDetected.equals(DBlang)) {
       					defaultColl.addDoc(doc);
       				}
       				else {
       					ArrayList<Language> probList = detector.getProbabilities();
       					for (Language langObj : probList) {
       						savedDoc=false;
       						if (langObj.prob > REDEFINE_THRESHOLD){
       							savedDoc=true;
       							LanguageGroups coll=whichCollection(allColl, langObj.lang);
       							coll.addDoc(doc);
       						}	
       					}
       					if(!savedDoc) defaultColl.addDoc(doc); 
       				}
       			} catch (Exception ex) {
       				//do nothing
       			}
        	}
        }
        catch (Exception e){
        	e.printStackTrace();
        }
		return allColl;
  	}
	
	/*
	 * returns which bucket to put current docs in, if bucket not found creates "other" bin
	 * ----------------------------------------------------------------------------------
	 * @input - all buckets, current language
	 * @output - returns relevant bucket
	 */
	public static LanguageGroups whichCollection(List<LanguageGroups> allColl, String lang) {
		LanguageGroups coll = new LanguageGroups("other");
		for (LanguageGroups lg: allColl) {
			if (lg.getLang().equals(lang)) {
				return lg;
			}
		}
		return coll;
	}
}
