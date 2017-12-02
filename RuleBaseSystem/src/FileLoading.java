import java.io.*;
import java.util.ArrayList;

public class FileLoading {

    public static ArrayList<String> fileLoading(String fileName){
    	File file = new File(fileName);
    	ArrayList<String> data = new ArrayList<String>();
    	
    	if(!(file.exists() && file.isFile())){//ファイルが存在しなかったら
    		data.add("Error:Not found the file");
    		return data;
    	}

        try {    // ファイル読み込みに失敗した時の例外処理のためのtry-catch構文

            // 文字コードを指定してBufferedReaderオブジェクトを作る
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));

            // 変数lineに1行ずつ読み込むfor文
            for (String line = in.readLine(); line != null; line = in.readLine()) {
                //System.out.println(line);  // 1行表示
            	 data.add(line);
            }

        } catch (IOException e) {
            e.printStackTrace(); // 例外が発生した所までのスタックトレースを表示
        }
        
        return data;

    }
}
