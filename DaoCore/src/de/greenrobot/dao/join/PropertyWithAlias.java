package de.greenrobot.dao.join;

import de.greenrobot.dao.Property;
import de.greenrobot.dao.Selectable;

/**
 * Allows attaching of a table alias to a property
 * @author Stephen Dunford
 *
 */
public class PropertyWithAlias implements Selectable {

	private final Property property;
	private String alias;
	
	public PropertyWithAlias(Property property, String alias) {
		this.property = property;
		this.alias = alias;
	}
	
	@Override
	public String getColumnName() {
		return property.columnName;
	}

	@Override
	public String getColumnPrefix() {
		// TODO Auto-generated method stub
		return alias;
	}
	
}
