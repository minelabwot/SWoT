package bupt.tools;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Features {
	
	String context;
	String candidate;
	String[] contexts;
	String[] candidates;
	
	private Features(String context,String candidate) {
		this.context = context.toLowerCase();
		this.candidate = candidate.toLowerCase().replace("_", " ");
		this.contexts = this.context.split(" ");
		this.candidates = this.candidate.split(" ");
	}
	
	public static Features getInstance(String context,String candidate) {
		return new Features(context, candidate);
	}
	
	//keyword coefficient,feature1
	public float diceWord() {
		List<String> equal = new ArrayList<String>();
		Set<String> contextSet = new HashSet<String>();
		for(int i=0;i<contexts.length;i++) 
			contextSet.add(contexts[i]);
		for(int i=0;i<candidates.length;i++) {
			if(!contextSet.add(candidates[i]))
				equal.add(candidates[i]);
		}
		//分母
		for(String s : equal) {
			System.out.print(s+" ");
		}
		float denominator = (float)(contexts.length+candidates.length);
		return 2*equal.size()/denominator;
	}
	
	//编辑距离,feature2
	public float editDistance() {
		return Leven.levenshtein(context, candidate);
	}
		
	//return 0 or 1,两者完全相同,feature3
	public float allEqual() {
		if(context.equals(candidate))
			return (float)1;
		else
			return (float)0;
	}
	
	//return0 or 1 ,candidate是否含有所有context字符,feature4
	public float allcontains() {
		Set<String> candidateList = new HashSet<String>();
		for(int i=0;i<candidates.length;i++) {
			candidateList.add(candidates[i]);
		}
		for(int i=0;i<contexts.length;i++) {
			//如果add成功,说明candidate中并没有完全包含context的单词
			if(candidateList.add(contexts[i]))
				return (float)0;
		}
		return (float)1;
	}
	
	//sameOrder 是否context的出现顺序和candidate中的相同,return0 ,1,feature 5
	public float sameOrder() {
		String candidate1 = candidate;
		int count=0;
		for(int i=0;i<contexts.length;i++) {
			if(candidate1.contains(contexts[i])) {
				candidate1 = candidate1.substring(candidate1.indexOf(contexts[i]));
				count++;
				//System.out.println(candidate1);
			}
		}
		if(count == contexts.length)
			return (float)1;
		else
			return 0;
	}
	//字符串长度 ,feature6
	public float stringLength() {
		float contextLen = context.length();
		float candidateLen = candidate.length();
		if(contextLen >= candidateLen) {
			return 1-(contextLen-candidateLen)/contextLen;
		}
		else
			return 1-(candidateLen-contextLen)/candidateLen;
	}
	
	
}
