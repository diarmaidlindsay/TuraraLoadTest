package jp.pulseanddecibels.tularaloadtest.model;

import jp.pulseanddecibels.tularaloadtest.data.TelNumber;
import jp.pulseanddecibels.tularaloadtest.pjsip.TularaCall;
import jp.pulseanddecibels.tularaloadtest.util.Util;

/**
 *
 * 着信情報
 *
 *
 */
public class IncomingCallItem {
	/** 各着信コールの一意の識別子 */
	public final String callId;
	/** 表示名 */
	public final String displayName;
	/** 電話番号 */
	public final TelNumber telNum;

	public final String fromURI;
	public final String toURI;

	//if call is not answered, should we dispose of this?
	public final TularaCall call;


	/** 表示用ラベル */
	public String label = Util.STRING_EMPTY;





	/**
	 * コンストラクタ
	 */
	public IncomingCallItem(String callId,
							String displayName,
							TelNumber userName,
							String fromURI,
							String toURI,
							String label,
							TularaCall call) {

		this.callId 	 = callId;
		this.displayName = displayName;
		this.telNum 	 = userName;
		this.fromURI 	 = fromURI;
		this.toURI 		 = toURI;
		this.label 		 = label;
		this.call	     = call;

		// debug();
	}



	/**
	 * 表示用情報を取得
	 * @return
	 */
	public String getDisplayInfo() {
		return label;
	}





	/**
	 * 新しいインスタンスで、コピーを入手する
	 */
	public IncomingCallItem getNewCopy() {
		return new IncomingCallItem(callId,
									displayName,
									telNum,
									fromURI,
									toURI,
									label,
								    call);
	}
}