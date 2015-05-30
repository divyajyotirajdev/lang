/*
 * @author DivyaJyoti Rajdev
 */

package com.carma.lang;

public class ClusterLabelCount implements Comparable<ClusterLabelCount>{
	private String label;
	private int count;
	
	public ClusterLabelCount(String label, int count){
		this.label= new String(label);
		this.count = count;
	}
	
	public String getLabel() {
		return label;
	}

	public int getCount() {
		return count;
	}

	public int compareTo(ClusterLabelCount d) {
		return count < d.count ? -1 : count > d.count ? 1 : 0;
	}

}
