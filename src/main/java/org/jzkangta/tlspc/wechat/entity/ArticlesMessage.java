package org.jzkangta.tlspc.wechat.entity;

import java.util.List;

public class ArticlesMessage extends BaseMessage {
	private int ArticleCount;
	private List<Articles> Articles;
	public int getArticleCount() {
		return ArticleCount;
	}
	public void setArticleCount(int articleCount) {
		ArticleCount = articleCount;
	}
	public List<Articles> getArticles() {
		return Articles;
	}
	public void setArticles(List<Articles> articles) {
		Articles = articles;
	}
	
}
