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

class RuleBaseFrame{
	
	JFrame frame;
	Container contentPane;
	
	JPanel togglePanel;
	ButtonGroup buttonGroup;
	JToggleButton ftoggle;
	JToggleButton btoggle;
	
	RuleBaseFrame(){
		frame = new JFrame();

		frame.setTitle("ForwardChain/BackwardChain");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(646, 343);
		frame.setLocationRelativeTo(null);
		frame.setLayout(null);
		
		contentPane = frame.getContentPane();
		
		ftoggle = new JToggleButton("ForwardChain",true);
		btoggle = new JToggleButton("BackwardChain");
		buttonGroup = new ButtonGroup();
		buttonGroup.add(ftoggle);
		buttonGroup.add(btoggle);
		togglePanel = new JPanel(new GridLayout(2,1));
		togglePanel.setPreferredSize(new Dimension(150, 50));
		togglePanel.setBounds(500,0,150,50);
		togglePanel.add(ftoggle);
		togglePanel.add(btoggle);
		contentPane.add(togglePanel,BorderLayout.CENTER);
		
		frame.setVisible(true);
	}
}

