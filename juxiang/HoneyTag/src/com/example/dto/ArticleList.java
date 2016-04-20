package com.example.dto;

import java.util.ArrayList;
import java.util.List;

public class ArticleList {

	public List<ArticleDTO> getArtilist() {
		return artilist;
	}

	public void setArtilist(List<ArticleDTO> artilist) {
		this.artilist = artilist;
	}

	private List<ArticleDTO> artilist = new ArrayList<ArticleDTO>();
}
