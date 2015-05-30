/*
 * @author DivyaJyoti Rajdev
 */

package com.carma.lang;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.UnknownHostException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import org.carrot2.core.Cluster;

public class App 
{
    public static void main( String[] args ) throws Exception
    {	
    	/* ###################################################################################
    	 * # STEP 1: correct the language mark-up in docs									 #
    	 * ###################################################################################
    	 * */
    	Path currentRelativePath = Paths.get("");
    	
		String profileDirectory = currentRelativePath.toAbsolutePath().toString() + "/profiles/";
    	DetectorFactory.loadProfile(profileDirectory);
		//System.out.println("Language Detection");
		
		/*
		 * Get all languages from Mongo, check if "en" exists otherwise throw exception
		 */
		List<String> DBlang=MongoClass.getAllLang();
		String language = "en";
		if (!DBlang.contains(language)) {
			throw new EnglishNotFoundException("Database does not have english language");
		}
		
		
		/* ###################################################################################
		 * # STEP 2: put a batch of 5000 docs in English bucket											 #
		 * ###################################################################################
		 */
		int limit=5000;
		List<DocProperty> engDocs = new ArrayList<DocProperty>(); 
		List<Cluster> currentCluster = new ArrayList<Cluster>();
		/*
		 * Previous GIT commit had more efficient way of doing this by creating language buckets
		 * and storing collection of documents in bucket of corresponding language
		 * LanguageGroups class left in to support the above implementation
		 * but MongoDB java implementation does not allow query with a variable in it, so explicitly
		 * had to send "en" and modify the previous function signature from getDocsOfLang(String lang)
		 */
		engDocs.addAll(MongoClass.getDocsOfEng(limit));
		
		
		/* ###################################################################################
		 * # STEP 3: batch cluster "en" bucket							 					 #
		 * ###################################################################################
		 */
		File file1 = new File("AllClusters.txt");
		PrintWriter writer1 =null;
		File file2 = new File("LabelCount.txt");
		PrintWriter writer2 =null;
		File file3 = new File("LabelCluster.txt");
		PrintWriter writer3 =null;
		List<String> onlyLabels= new ArrayList<String>();
		/*
		 * Set up the write stream outside the writer function as that needs to be called
		 * once for each batch undergoing clustering
		 */
		if(!file1.exists()){
			   writer1 = new PrintWriter(file1, "UTF-8");  
		  }
		else {
			   writer1 = new PrintWriter(new BufferedWriter(new FileWriter("AllClusters.txt", true)));
		  }
		if(!file2.exists()){
			   writer2 = new PrintWriter(file2, "UTF-8");  
		  }
		else {
			   writer2 = new PrintWriter(new BufferedWriter(new FileWriter("LabelCount.txt", true)));
		  }
		if(!file3.exists()){
			   writer3 = new PrintWriter(file3, "UTF-8");  
		  }
		else {
			   writer3 = new PrintWriter(new BufferedWriter(new FileWriter("LabelClusters.txt", true)));
		  }
		int batchnumber = 0;
		while (engDocs.size()>10) {
			/*
			 * three choices of algorithm "stc", "k", "lingo"
			 * -------------------------------------------------------------------------
			 */
			currentCluster = ClusterText.clusterDocs(engDocs, "lingo");
			/* NOTE:
			 * Can be done more efficiently by giving out ArrayList<DocProperty> as a collection
			 * for each cluster however while scaling it might not be a good idea to keep all those
			 * documents in program memory space so writing title to a single file 
			 * in sequential order of clusters
			 * alternately, update Mongo with annotation denoting cluster
			 */
			++batchnumber;
			ClusterText.saveResultsToFile(currentCluster,batchnumber,writer1);
			ClusterText.saveLabelCount(currentCluster, writer2);
			onlyLabels= ClusterText.returnLabelCount(currentCluster);
			/*
			 * get the next batch
			 */
			engDocs.clear();
			engDocs.addAll(MongoClass.getDocsOfEng(limit));	
		}
		
		
		/* ###################################################################################
		 * # STEP 4: second pass of clustering on cluster labels
		 * ###################################################################################
		 */
		List<Cluster> labelCluster = new ArrayList<Cluster>();
		labelCluster= ClusterText.clusterLabels(onlyLabels, "lingo");
		/*
		 * final label clusters stored as collection, uncomment the following to display on console
		 * writing only cluster labels to file, problem is the biggest cluster will be other documents
		 * but if i broke that down to titles, heap spaced out even with an extended heap so leaving as is
		 * NOTE: more efficient alternative is to use word2vec (https://code.google.com/p/word2vec/)
		 * to cluster only the labels, but that required word level training which I could not finish
		 * due to time constraints.
		 */
		//ClusterText.displayResults(labelCluster);
		ClusterText.saveResultsToFile(labelCluster,0,writer3);
		writer1.close();
		writer2.close();
		writer3.close();
		//System.out.println("end");
    }
}

