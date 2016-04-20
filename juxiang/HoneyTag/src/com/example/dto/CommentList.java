package com.example.dto;

import java.util.ArrayList;
import java.util.List;

public class CommentList {

	private List<CommentDTO> commlist = new ArrayList<CommentDTO>();

	public List<CommentDTO> getCommlist() {
		return commlist;
	}

	public void setCommlist(List<CommentDTO> commlist) {
		this.commlist = commlist;
	}
}
