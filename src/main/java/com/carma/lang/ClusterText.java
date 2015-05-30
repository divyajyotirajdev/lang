/*
 * @author DivyaJyoti Rajdev
 */

package com.carma.lang;// headline, summary, content (2/3)

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.carrot2.clustering.kmeans.BisectingKMeansClusteringAlgorithm;
import org.carrot2.clustering.lingo.LingoClusteringAlgorithm;
import org.carrot2.clustering.stc.STCClusteringAlgorithm;
import org.carrot2.core.*;

public class ClusterText {
	
	/* initializes controller, performs clustering on document headline + content
	 * optional to get summary from MONGOClass while extracting docs 
	 * NOTE: so far no checks have been placed to ensure correct choice of algorithm is entered
	 * by default k means clustering will be used for incorrect entry
	 * ----------------------------------------------------------------------------------
	 * @input - collection of DocProperty documents, choice for algorithm
	 * @output - Collection of Clusters
	 */
	public static List<Cluster> clusterDocs(List<DocProperty> docs, String ch) throws Exception {
		final Controller controller = ControllerFactory.createSimple();
        List<Document> documents = new ArrayList<Document>();
        Iterator<DocProperty> docItr=docs.iterator();
        
        while(docItr.hasNext()) {
        	DocProperty temp= docItr.next();
        	documents.add(new Document(temp.getHeadline(),temp.getContent(),""));
        }

        ProcessingResult result = controller.process(documents,"",BisectingKMeansClusteringAlgorithm.class);
        if (ch.equalsIgnoreCase("stc")) {
        	result = controller.process(documents,"",STCClusteringAlgorithm.class);
        }
        else {
        	if(ch.equalsIgnoreCase("lingo")) result = controller.process(documents,"",LingoClusteringAlgorithm.class);
        }
        return result.getClusters();
      }
	
	/*
	 * same functionality as above except that this is for clustering labels from arrayList of Labels
	 */
	public static List<Cluster> clusterLabels(List<String> docs, String ch) throws Exception {
		List<Document> documents = new ArrayList<Document>();
		final Controller controller = ControllerFactory.createSimple();
        Iterator<String> docItr=docs.iterator();
        
        while(docItr.hasNext()) {
        	String temp= docItr.next();
        	documents.add(new Document(temp,temp,""));
        }

        ProcessingResult result = controller.process(documents,"",BisectingKMeansClusteringAlgorithm.class);
        if (ch.equalsIgnoreCase("stc")) {
        	result = controller.process(documents,"",STCClusteringAlgorithm.class);
        }
        else {
        	if(ch.equalsIgnoreCase("lingo")) result = controller.process(documents,"",LingoClusteringAlgorithm.class);
        }
        return result.getClusters();
      }
	
	
	/* writes cluster labels + cluster scores + titles of documents to a file 
	 * in sequential order of clusters they belong to  
	 * ----------------------------------------------------------------------------------
	 * @input - collection of clusters, int count that specifies limit, previously open write stream
	 * @output - void
	 */
	public static void saveResultsToFile(List<Cluster> clusters, int batch, PrintWriter wr) throws IOException {
		
		wr.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
		wr.println("BATCH NUMBER" + batch);
		wr.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        for (Cluster cluster : clusters) {
            wr.println("************************************************");

            wr.println("Cluster: " + cluster.getLabel() + " : " + cluster.getScore());
          for (Document document : cluster.getDocuments()) {
        	  wr.println("\t" + document.getTitle());
          }
        }
        wr.flush();
      }
	
	/* writes each cluster label the number of times it occurs (count) to a file  
	 * in sequential order of clusters they belong to , this will be passed to 2nd pass of clustering
	 * ----------------------------------------------------------------------------------
	 * @input - collection of clusters, previously open write stream
	 * @output - void
	 */
	public static void saveLabelCount(List<Cluster> clusters, PrintWriter wr) throws FileNotFoundException, UnsupportedEncodingException {
        List<ClusterLabelCount> cl= new ArrayList<ClusterLabelCount>();
        for (Cluster cluster : clusters) {
          int count=cluster.size();
          cl.add( new ClusterLabelCount(cluster.getLabel(),count));
        }
        Collections.sort(cl);
        for (ClusterLabelCount c: cl) {
        	for (int i=0; i<c.getCount(); ++i) {
        		wr.println(c.getLabel());
        	}
        }
        wr.flush();
      }
	
	public static List<String> returnLabelCount(List<Cluster> clusters) {
        List<String> cl= new ArrayList<String>();
        for (Cluster cluster : clusters) {
          int count=cluster.size();
          for (int i=0; i<count; ++i) {
        	  cl.add(cluster.getLabel());
          }
      	}
        return cl;
      }

      public static void displayResults(Collection<Cluster> clusters) {
        
        for (Cluster cluster : clusters) {
            System.out.println("************************************************");

          System.out.println("Cluster: " + cluster.getLabel() + " : " + cluster.getScore());
          /*for (Document document : cluster.getDocuments()) {
            System.out.println("\t" + document.getTitle());
          }*/
        }
      }

}
