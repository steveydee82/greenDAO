package de.greenrobot.dao.selectable;

import de.greenrobot.dao.Selectable;

public class StringSelectable implements Selectable {

	private String mSelect;
	private String mColumnName;
	
	public StringSelectable(String select) {
		mSelect = select;
	}
	
	public StringSelectable as(String columnName) {
		mColumnName = columnName;
		
		return this;
	}

	@Override
	public String getColumnName() {
		if(mColumnName != null && mColumnName.length() > 0) {
			return String.format("'%s' as %s", mSelect, mColumnName);	
		}
		
		return mSelect;
	}

	@Override
	public String getColumnPrefix() {
		return "";
	}
		
}
