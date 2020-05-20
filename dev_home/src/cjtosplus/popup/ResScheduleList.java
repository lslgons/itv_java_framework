package cjtosplus.popup;

public class ResScheduleList {
	/**
	 * 방송중 상품 인덱스
	 */
	public int onAirItemIndx;
	/**
	 * 방송중 상품 여부
	 */
	public boolean[] isOnAirItem;
	/**
	 * VOD 어셋
	 */
	public String[] assetId;
	/**
	 * 상품타입
	 */
	public String[] productType;

	public ResScheduleList() {
	}

	public void init(int size) {
//		super.init(size); // 추후 작업.. 20min...
		this.isOnAirItem = new boolean[size];
		this.assetId = new String[size];
		this.productType = new String[size];
	}
}
