package jp.co.dk.datastoremanager.core.rdb;

import static jp.co.dk.datastoremanager.core.message.DataStoreManagerMessage.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import jp.co.dk.datastoremanager.core.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.core.exception.DataStoreManagerFatalException;
import jp.co.dk.datastoremanager.core.rdb.BytesColumnData;
import jp.co.dk.datastoremanager.core.rdb.SqlParameter;

public class BytesColumnData implements ColumnData, SqlParameter {
	
	protected byte[] parameter;
	
	BytesColumnData(byte[] parameter) {
		this.parameter = parameter;
	} 
	
	BytesColumnData(Object parameter) throws DataStoreManagerException {
		if (parameter != null) {
			try {
				ByteArrayOutputStream baos= new ByteArrayOutputStream();  
				ObjectOutputStream oos = new ObjectOutputStream(baos);
				oos.writeObject(parameter);
				this.parameter = baos.toByteArray();
				baos.close();
				oos.close();
			} catch (IOException e) {
				throw new DataStoreManagerException(FAILE_TO_AN_ATTEMPT_WAS_MADE_TO_CONVERT_TO_BYTE_ARRAY, e);
			}
		}
	}
	
	public byte[] get() {
		return this.parameter;
	}
	
	@Override
	public void set(int index, PreparedStatement statement) throws DataStoreManagerException {
		try {
			if (this.parameter != null) {
				statement.setBytes(index, this.parameter);
			} else {
				statement.setBytes(index, null);
			}
		} catch (SQLException e) {
			throw new DataStoreManagerException(AN_EXCEPTION_OCCURRED_WHEN_PERFORMING_THE_SET_PARAMETERS_TO_SQL, e);
		}
	}
	
	@Override
	public String getDataByString() {
		return this.getHash(Algorithm.MD5);
	}
	
	/**
	 * このデータを指定のアルゴリズムにて暗号化した文字列を返却します。<p/>
	 * このデータを指定のアルゴリズムにて暗号し、その結果を16進数の文字列にて整形した文字列を返却します。<br/>
	 * <br/>
	 * 指定のアルゴリズムが指定されていなかった、または指定のアルゴリズムが不明な文字列が指定されていた場合、DocumentFatalExceptionが発生します。<br/>
	 * 
	 * @return 指定のアルゴリズムにて暗号化済みの16進数表記文字列
	 * @throws DataStoreManagerFatalException 暗号化処理にて致命的例外が発生した場合
	 */
	public String getHash(Algorithm algorithm) throws DataStoreManagerFatalException {
        try {
        	if (algorithm == null) throw new DataStoreManagerFatalException(ALGORITHM_IS_NOT_SET);
        	MessageDigest messageDigest = MessageDigest.getInstance(algorithm.getAlgorithmName());
        	messageDigest.reset();
        	byte[] hashBytes = messageDigest.digest(this.parameter);
        	StringBuilder hash = new StringBuilder();
        	for (byte hashByte : hashBytes) hash.append(String.format("%02x", new Byte(hashByte)));
        	return hash.toString();
        } catch (NoSuchAlgorithmException na) {
        	throw new DataStoreManagerFatalException(FAILE_TO_CONVERT_BY_ALGORITHM);
        }
	}
	
	@Override
	public boolean equals(Object object) {
		if (object == null) return false;
		if (!(object instanceof BytesColumnData)) return false;
		BytesColumnData thisClassObj = (BytesColumnData) object;
		if (thisClassObj.hashCode() == this.hashCode()) return true;
		return false;
	}
	
	@Override
	public int hashCode() {
		int result = -1;
		if (this.parameter != null) {
			for (int i=0; i<this.parameter.length; i++) {
				result*=this.parameter[i];
			}
			return result * 17;
		} else {
			return result;
		}
		
	}
	
	@Override
	public String toString() {
		if (this.parameter != null) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i< this.parameter.length; i++) {
				sb.append(this.parameter[i]).append(',');
			}
			StringBuilder result = new StringBuilder(sb.substring(0, sb.length()-1));
			return result.toString();
		} else {
			return "NULL";
		}
	}

	/**
	 * ハッシュコードを生成する際に使用するアルゴリズムを定義する定数クラス。<br/>
	 * 
	 * @version 1.0
	 * @author D.Kanno
	 */
	enum Algorithm {
		
		/** SHA-256 */
		SHA_256("SHA-256"),
		
		/** SHA-512 */
		SHA_512("SHA-512"),
		
		/** MD5 */
		MD5("MD5"),
		
		;
		
		/** アルゴリズム名称 */
		private String algorithmName;
		
		/**
		 * コンストラクタ<p/>
		 * 
		 * @param algorithmName
		 */
		Algorithm(String algorithmName) {
			this.algorithmName = algorithmName;
		}
		
		/**
		 * この定数が保持しているアルゴリズム名称を取得します。<p/>
		 * 
		 * @return アルゴリズム名称
		 */
		String getAlgorithmName() {
			return this.algorithmName;
		}
	}
}