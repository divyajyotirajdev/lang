Project Duration Feb-May 2015

######Details

Project makes use of Opensource Language Detection and Carrot2 clustering libraries to achieve Document clustering in a 2 pass way. The first pass clusters small batches (5000 docs) at a time, the second pass takes only the cluster labels and clusters those effective creating 2 sets of cluster buckets.
Before the Clustering module, a langauge detection module ensures that if a language is detected with over x% (x being 95 in this case) confidence, make that the langauge of the document in the collection. This had to be done as a lot of incoming english news had spam or relevant content of other langauges.
The clutered buckets are then written to file thats is invoke by Javascript for visualization to get insight into trend evolution, relative size of clusters and chronological information



BEFORE RUNNING: note that input here is being taken from NoSQL Mongo Database, the exact fields and values of which have been obfuscated intentionally in MongoClass, please make your own relevant initializations or use another input stream that gives the documents as specified by getDocsOfEng() function signature

1. Source Files
    [Language Detection] (https://code.google.com/p/language-detection/)
    [Carrot2] (http://project.carrot2.org/download.html)
    
2. Between successive runs of program, ideally delete the output files created.

3. Output files created
    
  1.   AllClusters.txt : Batchwise Clusters with corresponding headlines 
         sorted by score
  2.  LabelCount.txt : if 'x' number of documents are tagged with label 
         'abc', then 'abc' will be written 'x' times to file
  3. LabelClusters.txt : which clusters the labels have been assigned to.
         Suggested improvement- Word2Vec
         
4. The visualizations in JavaScript have not been included. Ping me for a code skeleton sample