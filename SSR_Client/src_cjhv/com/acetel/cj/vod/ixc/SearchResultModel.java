package com.acetel.cj.vod.ixc;

public class SearchResultModel implements java.io.Serializable, java.lang.Cloneable  {
	
/**
	 * 
	 */
	private static final long serialVersionUID = 11111L;

//	private int totalResults;  // it means the total number of asset for the requested search
//	private int searchOption; // it means the requested search option
	
	
	/**
	 * 
	 */
	
	protected  int totalResult;  // it is the total number of the requested search. 
	
	protected int catalogUid; // it is a unique id of asset from catalogoue system
	
	protected String providerAssetId;
	
	protected String name;  // the title of vod content
	
	protected String rating;
	
	protected String assetPrice;
	
	protected String productType;
	
	public SearchResultModel() {
	}

	/**
	 * @return the totalResult
	 */
	public int getTotalResult() {
		return totalResult;
	}
	
	/**
	 * @param totalResult the totalResult to set
	 */
	public void setTotalResult(int totalResult) {
		this.totalResult = totalResult;
	}
	
	/**
	 * @return the assetPrice
	 */
	public String getAssetPrice() {
		return assetPrice;
	}
	/**
	 * @param assetPrice the assetPrice to set
	 */
	public void setAssetPrice(String assetPrice) {
		this.assetPrice = assetPrice;
	}

	/**
	 * @return the catalogUid
	 */
	public int getCatalogUid() {
		return catalogUid;
	}
	/**
	 * @param catalogUid the catalogUid to set
	 */
	public void setCatalogUid(int catalogUid) {
		this.catalogUid = catalogUid;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the providerAssetId
	 */
	public String getProviderAssetId() {
		return providerAssetId;
	}

	/**
	 * @param providerAssetId the providerAssetId to set
	 */
	public void setProviderAssetId(String providerAssetId) {
		this.providerAssetId = providerAssetId;
	}
	/**
	 * @return the rating
	 */
	public String getRating() {
		return rating;
	}
	/**
	 * @param rating the rating to set
	 */
	public void setRating(String rating) {
		this.rating = rating;
	}
	/**
	 * @return the productType
	 */
	public String getProductType() {
		return productType;
	}
	/**
	 * @param productType the productType to set
	 */
	public void setProductType(String productType) {
		this.productType = productType;
	}

	public Object clone() throws CloneNotSupportedException{ 
		return super.clone(); 
	}














}
	
	

