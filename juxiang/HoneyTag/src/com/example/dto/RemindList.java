package com.example.dto;

import java.util.ArrayList;
import java.util.List;

public class RemindList {

	private List<RemindDTO> artilist = new ArrayList<RemindDTO>();

	public List<RemindDTO> getArtilist() {
		return artilist;
	}

	public void setArtilist(List<RemindDTO> artilist) {
		this.artilist = artilist;
	}
}
