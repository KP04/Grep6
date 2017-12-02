/***
Unify Program written



変数:前に？をつける．  

Examle:
% Unify "Takayuki" "Takayuki"
true

% Unify "Takayuki" "Takoyuki"
false

% Unify "?x am Takayuki" "I am Takayuki"
?x = I .

% Unify "?x is ?x" "a is b"
false

% Unify "?x is ?x" "a is a"
?x = a .

% Unify "?x is a" "b is ?y"
?x = b.
?y = a.

% Unify "?x is a" "?y is ?x"
?x = a.
?y = a.

Unify は，ユニフィケーション照合アルゴリズムを実現し，
パターン表現を比較して矛盾のない代入によって同一と判断
できるかどうかを調べる．

ポイント！
ここでは，ストリング同士の単一化であるから，出現検査を行う必要はない．
しかし，"?x is a"という表記を"is(?x,a)"とするなど，構造を使うならば，
単一化において出現検査を行う必要がある．
例えば，"a(?x)"と"?x"を単一化すると ?x = a(a(a(...))) となり，
無限ループに陥ってしまう．

 ***/

import java.util.*;

public class Unify {

	static HashMap<String, String> copyHashMap(HashMap<String, String> baseHash) {
		HashMap<String, String> copyHash = new HashMap<String, String>();

		for (String key : baseHash.keySet()) {
			copyHash.put(key, baseHash.get(key));
		}

		return copyHash;
	}

	static ArrayList<HashMap<String, String>> Search(ArrayList<String> data,
			String[] SearchObj) {
		// tmp[i]はi番目の引数をどこまでマッチングさせたかを表す
		int[] tmp = new int[SearchObj.length];

		for (int i = 0; i < SearchObj.length; i++) {
			tmp[i] = 0;
		}

		// i番目の引数までのマッチング結果を一時的に保存しておくリスト
		ArrayList<HashMap<String, String>> tmpHash = new ArrayList<HashMap<String, String>>();

		for (int i = 0; i < SearchObj.length; i++) {
			tmpHash.add(new HashMap<String, String>());
		}

		// 全引数でマッチング成功したときの結果を格納するリスト
		ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();

		Unifier2 unifier = new Unifier2();

		while (true) {

			unifier.vars = new HashMap<String, String>(); // ハッシュマップの初期化

			for (int i = 0; i < SearchObj.length; i++) {

				while (i >= 0 && tmp[i] >= data.size()) {
					tmp[i] = 0;
					i--;
				}

				if (i <= -1) {// 全探索が終了したかの判定
					// System.out.println(result);
					return result;
				}

				for (int j = tmp[i]; j < data.size(); j++) {

					// マッチングが成功したら
					if (unifier.unify(SearchObj[i], data.get(j), unifier.vars)) {

						// マッチング成功した文章の、次の文章の添字を格納
						// 次のマッチング操作はtmp[i]から再開される
						tmp[i] = j + 1;

						// 最後の引数でマッチングが成功したら
						if (i == SearchObj.length - 1) {
							result.add(unifier.vars); // 結果を格納
							if (i != 0) {// 引数が2つ以上の時
								unifier.vars = copyHashMap(tmpHash.get(i - 1)); // 最後から2番目の引数までのマッチング結果から再開
							} else {// 引数が1つのとき
								unifier.vars = new HashMap<String, String>();
							}
							i--;
						} else { // 最後の引数ではなかったら
							tmpHash.set(i, copyHashMap(unifier.vars)); // 現時点でのマッチング結果を保存し、次の引数へ
						}

						break;

					} else {// マッチング失敗したら

						if (i != 0) {// 最初の引数以外でマッチング失敗したら
							unifier.vars = copyHashMap(tmpHash.get(i - 1));// 前の引数までのマッチング結果を代入
						} else {// 最初の引数でマッチング失敗したら
							unifier.vars = new HashMap<String, String>(); // 初期化
						}

						// i番目の引数が最後までマッチングしたら
						if (j >= data.size() - 1) {
							tmp[i] = 0;
							if (i > 1) {
								unifier.vars = copyHashMap(tmpHash.get(i - 2));
							} else {
								unifier.vars = new HashMap<String, String>();
							}
							i -= 2; // 一つ前の引数に戻る
						}
					}
				}
			}
		}
	}

	public static void main(String arg[]) {

		// ファイルから検索対象のデータセットを取得
		ArrayList<String> data = FileLoading.fileLoading("data.txt");
		System.out.println(Search(data, arg));

	}
}

class Unifier2 {
	StringTokenizer st1;
	String buffer1[];
	StringTokenizer st2;
	String buffer2[];
	HashMap<String, String> vars;

	Unifier2() {
		vars = new HashMap<String, String>();
	}

	public boolean unify(String string1, String string2,
			HashMap<String, String> bindings) {
		this.vars = bindings;
		return unify(string1, string2);
	}

	public boolean unify(String string1, String string2) {
		// System.out.println(string1);
		// System.out.println(string2);

		// 同じなら成功
		if (string1.equals(string2))
			return true;

		// 各々トークンに分ける
		st1 = new StringTokenizer(string1);
		st2 = new StringTokenizer(string2);

		// 数が異なったら失敗
		if (st1.countTokens() != st2.countTokens())
			return false;

		// 定数同士
		int length = st1.countTokens();
		buffer1 = new String[length];
		buffer2 = new String[length];
		for (int i = 0; i < length; i++) {
			buffer1[i] = st1.nextToken();
			buffer2[i] = st2.nextToken();
		}
		for (int i = 0; i < length; i++) {
			if (!tokenMatching(buffer1[i], buffer2[i])) {
				return false;
			}
		}

		// 最後まで O.K. なら成功
		System.out.println(vars.toString());
		return true;
	}

	boolean tokenMatching(String token1, String token2) {
		if (token1.equals(token2))
			return true;
		if (var(token1) && !var(token2))
			return varMatching(token1, token2);
		if (!var(token1) && var(token2))
			return varMatching(token2, token1);
		if (var(token1) && var(token2))
			return varMatching(token1, token2);
		return false;
	}

	boolean varMatching(String vartoken, String token) {
		if (vars.containsKey(vartoken)) {
			if (token.equals(vars.get(vartoken))) {
				return true;
			} else {
				return false;
			}
		} else {
			replaceBuffer(vartoken, token);
			if (vars.containsValue(vartoken)) {
				replaceBindings(vartoken, token);
			}
			vars.put(vartoken, token);
		}
		return true;
	}

	void replaceBuffer(String preString, String postString) {
		for (int i = 0; i < buffer1.length; i++) {
			if (preString.equals(buffer1[i])) {
				buffer1[i] = postString;
			}
			if (preString.equals(buffer2[i])) {
				buffer2[i] = postString;
			}
		}
	}

	void replaceBindings(String preString, String postString) {
		Iterator<String> keys;
		for (keys = vars.keySet().iterator(); keys.hasNext();) {
			String key = (String) keys.next();
			if (preString.equals(vars.get(key))) {
				vars.put(key, postString);
			}
		}
	}

	boolean var(String str1) {
		// 先頭が ? なら変数
		return str1.startsWith("?");
	}

}
