package de.greenrobot.dao.query;

import de.greenrobot.dao.Dao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.Selectable;

public class BaseBuilder {

	protected Dao<?, ?> mDao;
    protected StringBuilder orderBuilder;
	
	protected BaseBuilder(Dao<?, ?> dao) {
		mDao = dao;
	}
	
	private void checkOrderBuilder() {
        if (orderBuilder == null) {
            orderBuilder = new StringBuilder();
        } else if (orderBuilder.length() > 0) {
            orderBuilder.append(",");
        }
    }
	
    protected void orderAscOrDesc(String ascOrDescWithLeadingSpace, Property... properties) {
        for (Property property : properties) {
            checkOrderBuilder();
            append(orderBuilder, property);
            if (String.class.equals(property.type)) {
                orderBuilder.append(" COLLATE LOCALIZED");
            }
            orderBuilder.append(ascOrDescWithLeadingSpace);
        }
    }
    
    /** Adds the given properties to the ORDER BY section using the given custom order. */
    protected void orderCustomInternal(Property property, String customOrderForProperty) {
        checkOrderBuilder();
        append(orderBuilder, property).append(' ');
        orderBuilder.append(customOrderForProperty);
    }

    /**
     * Adds the given raw SQL string to the ORDER BY section. Do not use this for standard properties: ordedAsc and
     * orderDesc are prefered.
     */
    protected void orderRawInternal(String rawOrder) {
        checkOrderBuilder();
        orderBuilder.append(rawOrder);
    }
    
    public boolean hasOrderBy() {
    	return (orderBuilder != null);
    }
	
	public boolean isMasterTable(String tableName) {
		return mDao.getTablename().equalsIgnoreCase(tableName);
	}

	protected String getTableAlias(Selectable s) {
		if (isMasterTable(s.getColumnPrefix())) {
			return "T";
		} else {
			return s.getColumnPrefix();
		}
	}

	protected StringBuilder append(StringBuilder builder, Property property) {
		builder.append(getTableAlias(property)).append('.')
				.append(property.columnName);
		return builder;
	}
}
