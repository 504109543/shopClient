package com.lo.shop.adapter;

public final class TopTabAdapter {

	private String mPictureUrl; // 图片URL
	private String mArticleUrl; // 文章URL
	private String mTitle; // 文章标题
	
	public TopTabAdapter(String mPictureUrl, String mArticleUrl, String mTitle) {
		this.mPictureUrl = mPictureUrl;
		this.mArticleUrl = mArticleUrl;
		this.mTitle = mTitle;
	}

	public String getmArticleUrl() {
		return mArticleUrl;
	}

	public String getmPictureUrl() {
		return mPictureUrl;
	}

	public String getmTitle() {
		return mTitle;
	}

	public void setmArticleUrl(String mArticleUrl) {
		this.mArticleUrl = mArticleUrl;
	}

	public void setmPictureUrl(String mPictureUrl) {
		this.mPictureUrl = mPictureUrl;
	}

	public void setmTitle(String mTitle) {
		this.mTitle = mTitle;
	}
	
}
