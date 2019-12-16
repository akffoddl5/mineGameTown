package com.java.miniClient;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public class BattleLogTableModel extends AbstractTableModel {
	
	private List<ArrayList<String> > data;
	private List<String> name;
	
	public BattleLogTableModel(List<ArrayList<String> > data1 , List<String> name1) {
		this.data = data1;
		this.name = name1;
	}
	
	@Override
	public int getColumnCount() {
		return name.size();
	}

	@Override
	public int getRowCount() {
		return data.size();
	}

	@Override
	public Object getValueAt(int arg0, int arg1) {
		return data.get(arg0).get(arg1);
	}
	
	@Override
	public String getColumnName(int column) {
		return name.get(column);
	}
	
	

}
