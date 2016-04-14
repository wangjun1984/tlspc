package org.jzkangta.tlspc.wechat.entity;

public class MpArticles extends Articles {
	
	private String thumbMediaId;
	
	private String author;
	
	private String contentSourceUrl;
	
	private String content;
	
	private String digest;
	
	private int showCoverPic;

	public String getThumbMediaId() {
		return thumbMediaId;
	}

	public void setThumbMediaId(String thumbMediaId) {
		this.thumbMediaId = thumbMediaId;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getContentSourceUrl() {
		return contentSourceUrl;
	}

	public void setContentSourceUrl(String contentSourceUrl) {
		this.contentSourceUrl = contentSourceUrl;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getDigest() {
		return digest;
	}

	public void setDigest(String digest) {
		this.digest = digest;
	}

	public int getShowCoverPic() {
		return showCoverPic;
	}

	public void setShowCoverPic(int showCoverPic) {
		this.showCoverPic = showCoverPic;
	}
	
}
