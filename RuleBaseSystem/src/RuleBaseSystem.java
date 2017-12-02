import java.util.*;
import java.io.*;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class RuleBaseSystem{
	public static void main(String[] args){
		new RuleBaseFrame();
	}
}

class RuleBaseFrame implements ActionListener{
	private String outBuffer,proBuffer,qBuffer;
	JFrame frame;
	Container contentPane;
	
	//右側のボタン部分を構成するパーツ
	JPanel Panel;
	ButtonGroup buttonGroup;
	JToggleButton ftoggle,btoggle;
	JButton run,load,save;
	
	//ファイルの入力部分を構成するパーツ
	JTextArea assertionfileTextArea,rulefileTextArea;
	JTextArea assertionTextArea,ruleTextArea;
	JScrollPane assertionScrollPane,ruleScrollPane;
	
	//出力部分を構成するパーツ
	JTextArea qTextArea,outTextArea,proTextArea;
	JScrollPane outScrollPane,proScrollPane;
	JLabel qLabel,proLabel,outLabel,assertionLabel,ruleLabel;
	
	RuleBaseFrame(){
		frame = new JFrame();

		frame.setTitle("ForwardChain/BackwardChain");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(746, 620);
		frame.setLocationRelativeTo(null);
		frame.setLayout(null);
		frame.setResizable(false);
		
		contentPane = frame.getContentPane();
		
		//ボタン部分
		ftoggle = new JToggleButton("ForwardChain",true);
		btoggle = new JToggleButton("BackwardChain");
		buttonGroup = new ButtonGroup();
		buttonGroup.add(ftoggle);
		buttonGroup.add(btoggle);
		run = new JButton("Run");
		run.addActionListener(this);
		load = new JButton("Load");
		load.addActionListener(this);
		save = new JButton("Save");
		save.addActionListener(this);
		Panel = new JPanel(new GridLayout(5,1));
		Panel.setPreferredSize(new Dimension(140, 160));
		Panel.setBounds(600,0,140,160);
		Panel.add(ftoggle);
		Panel.add(btoggle);
		Panel.add(load);
		Panel.add(save);
		Panel.add(run);
		
		//入力部分
		assertionLabel = new JLabel("Assersion Filename:");
		assertionLabel.setBounds(0,0,275,32);
		assertionLabel.setFont(new Font("Arial", Font.PLAIN, 15));
		assertionfileTextArea = new JTextArea("CarShopWm.data");
		assertionfileTextArea.setBounds(0,32,275,32);
		assertionTextArea = new JTextArea();
		assertionScrollPane = new JScrollPane(assertionTextArea);
		assertionScrollPane.setPreferredSize(new Dimension(275,200));
		assertionScrollPane.setBounds(0,69,275,200);
		ruleLabel = new JLabel("Rules Filename:");
		ruleLabel.setBounds(325,0,275,32);
		ruleLabel.setFont(new Font("Arial", Font.PLAIN, 15));
		rulefileTextArea = new JTextArea("CarShop.data");
		rulefileTextArea.setBounds(325,32,275,32);
		ruleTextArea = new JTextArea();
		ruleScrollPane = new JScrollPane(ruleTextArea);
		ruleScrollPane.setPreferredSize(new Dimension(275,200));
		ruleScrollPane.setBounds(325,69,275,200);
		qLabel = new JLabel("Question:");
		qLabel.setBounds(0,269,500,32);
		qLabel.setFont(new Font("Arial", Font.PLAIN, 32));
		qTextArea = new JTextArea();
		qTextArea.setPreferredSize(new Dimension(600,32));
		qTextArea.setBounds(0,301,600,32);
		
		//出力部分
		proLabel = new JLabel("Process:");
		proLabel.setBounds(0,341,550,32);
		proLabel.setFont(new Font("Arial", Font.PLAIN, 32));
		proTextArea = new JTextArea();
		proScrollPane = new JScrollPane(proTextArea);
		proScrollPane.setPreferredSize(new Dimension(600,96));
		proScrollPane.setBounds(0,373,600,96);
		outLabel = new JLabel("Result:");
		outLabel.setBounds(0,469,550,32);
		outLabel.setFont(new Font("Arial", Font.PLAIN, 32));
		outTextArea = new JTextArea();
		outScrollPane = new JScrollPane(outTextArea);
		outScrollPane.setPreferredSize(new Dimension(600,96));
		outScrollPane.setBounds(0,501,600,96);

		contentPane.add(Panel);
		contentPane.add(assertionLabel);
		contentPane.add(assertionfileTextArea);
		contentPane.add(assertionScrollPane);
		contentPane.add(ruleLabel);
		contentPane.add(rulefileTextArea);
		contentPane.add(ruleScrollPane);
		contentPane.add(qLabel);
		contentPane.add(qTextArea);
		contentPane.add(proLabel);
		contentPane.add(proScrollPane);
		contentPane.add(outLabel);
		contentPane.add(outScrollPane);

		frame.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent event) {
		JButton button = (JButton) event.getSource();
		
		if(isClear()){//ファイル名が書かれていなかったら
			initBuffer();//バッファ初期化
			outBuffer = "Error:Input Filename";
			outTextArea.setText(outBuffer);
			return;
		}
		
		//ファイル読み込み
		String assertionFileName = assertionfileTextArea.getText();
		String ruleFileName = rulefileTextArea.getText();
		ArrayList<String> assertion = FileLoading.fileLoading(assertionFileName);
		ArrayList<String> rule = FileLoading.fileLoading(ruleFileName);
		
		//ファイルが見つからなかったら
		if(assertion.get(0).equals("error") || rule.get(0).equals("error")){
			initBuffer();//バッファ初期化
			assertionfileTextArea.setText(writeBuffer(assertion));
			assertionfileTextArea.setText(writeBuffer(rule));
			return;
		}

		if(button == run){//Runが押されたら

			qBuffer = qTextArea.getText();

			//質問が書かれていなかったら
			if(qBuffer.equals("")){
				initBuffer();//バッファ初期化
				outBuffer = "Error:Input Question";
				outTextArea.setText(outBuffer);
				return;
			}
			
			if(ftoggle.isSelected()){
				ForwardRuleBase rb = new ForwardRuleBase(assertionFileName,ruleFileName,this);
				rb.forwardChain();
				setOutBuffer(Unify.Search(rb.wm.assertions, qBuffer.split(",")).toString());
				outTextArea.setText(outBuffer);
			}
			else{
				BackwardRuleBase rb;
				FileManager fm;
				fm = new FileManager();
			    ArrayList<BackwardRule> rules = fm.loadRules(ruleFileName);
			    ArrayList<String> wm    = fm.loadWm(assertionFileName);
			    rb = new BackwardRuleBase(rules,wm,this);
			    StringTokenizer st = new StringTokenizer(qBuffer,",");
			    ArrayList<String> queries = new ArrayList<String>();
			    for(int i = 0 ; i < st.countTokens();){
				queries.add(st.nextToken());
			    }
			    rb.backwardChain(queries);
			}
		}
		else if(button == load){//Loadが押されたら
			//バッファの初期化
			initBuffer();

			assertionTextArea.setText(writeBuffer(assertion));
			ruleTextArea.setText(writeBuffer(rule));

		}
		else if(button == save){//Saveが押されたら
			try {
				FileWriter assertionFw = new FileWriter(assertionFileName);
				FileWriter ruleFw = new FileWriter(ruleFileName);
				
				assertionFw.write(assertionTextArea.getText());
				assertionFw.close();
				ruleFw.write(ruleTextArea.getText());
				ruleFw.close();
				
				outBuffer = "Saved";
				outTextArea.setText(outBuffer);

			} catch (IOException e) {
				System.out.println(e);
			}
		}
	}
	
	public boolean isClear(){
		//ファイル名が入力されていなかったら
		if(assertionfileTextArea.getText().equals("") || rulefileTextArea.getText().equals("")){
			return true;
		}
		else{
			return false;
		}
	}
	
	public String getOutBuffer(){
		return outBuffer;
	}
	public void setOutBuffer(String s){
		outBuffer = s;
	}
	public String getProBuffer(){
		return proBuffer;
	}
	public void setProBuffer(String s){
		proBuffer = s;
	}
	public String getQBuffer(){
		return qBuffer;
	}
	
	/**
	 * ファイルから読み込んだテキストを1行ずつ格納したString型のリストをString型に変換するメソッド
	 * @param data
	 * @return リストdataをString型に変換したもの
	 */
	public String writeBuffer(ArrayList<String> data){
		String buffer = "";
		
		for(int i=0; i<data.size(); i++){
			buffer += data.get(i);
			buffer += "\n";
		}
		
		return buffer;
	}
	
	public void initBuffer(){
		proBuffer = "";
		outBuffer = "";
		proTextArea.setText("");
		outTextArea.setText("");
	}
}

