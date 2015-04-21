package de.greenrobot.dao.selectable;

import de.greenrobot.dao.Selectable;

public class SqlSelectable implements Selectable {

	private String mSelect;
	private String mColumnName;
	private String mColumnPrefix = "";
	
	public SqlSelectable(String select) {
		mSelect = select;
	}
	
	public SqlSelectable(String select, String columnPrefix) {
		mSelect = select;
		mColumnPrefix = columnPrefix;
	}
	
	public SqlSelectable as(String columnName) {
		mColumnName = columnName;
		
		return this;
	}

	@Override
	public String getColumnName() {
		if(mColumnName != null && mColumnName.length() > 0) {
			return String.format("%s as %s", mSelect, mColumnName);	
		}
		
		return mSelect;
	}

	@Override
	public String getColumnPrefix() {
		return mColumnPrefix;
	}
		
}
