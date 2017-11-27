import java.util.*;
import java.io.*;

public class RuleBaseSystem {
	static ForwordChain Forword = new ForwordChain();
	static BackwordChain Backword = new BackwordChain();
	static boolean ForB = true;
	
	public static void main(String args[]){
		if (!ForB){
			initForword();
			for (int i = 0; i < args.length; i++)
				Forword.rb.wm.addAssertion(args[i]);
			Forword.rb.forwardChain();
			System.out.println("Working Memory:" + Forword.rb.wm);
		} else {
			initBackword();
			ArrayList<String> queries = new ArrayList<String>();
			for(int i = 0; i < args.length; i++){
				StringTokenizer st = new StringTokenizer(args[i],",");
				queries.add(st.nextToken());
			}
			Backword.rb.backwardChain(queries);
			System.out.println("Answer:" + Backword.rb.anAnswer);
		}
	}
	
	static void initForword() {
		Forword.rb = new ForwordRuleBase();
		Forword.rb.fileName = "CarShop.data";
		Forword.rb.wm = new WorkingMemory();
       Forword.rb.rules = new ArrayList<ForwordRule>();
       Forword.rb.loadRules(Forword.rb.fileName);
	}
	static void initBackword() {
		Backword.fm = new FileManager();
	    ArrayList<BackwordRule> rules = Backword.fm.loadRules("CarShop.data");
	    ArrayList<String> wm    = Backword.fm.loadWm("CarShopWm1.data");
	    Backword.rb = new BackwordRuleBase(rules,wm);
	}
	
}
