package de.greenrobot.dao.query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.DaoException;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.Selectable;
import android.database.Cursor;

/**
 * Builder for performing a union of multple SQL queries
 * 
 * @author Stephen Dunford
 *
 */
public class UnionQueryBuilder extends BaseBuilder {

	private List<QueryBuilder<?>> mQueryBuilders;
	private AbstractDao<?, ?> mDao;
	
	protected UnionQueryBuilder(AbstractDao<?, ?> dao) {
		super(dao);
		
		mQueryBuilders = new ArrayList<QueryBuilder<?>>();
		mDao = dao;
		
	}
	
	public UnionQueryBuilder union(QueryBuilder<?> queryBuilder) {
		if(queryBuilder.hasOrderBy()) {
			throw new DaoException("Cannot add a query with an ORDER BY to a UNION clause. Please use .union(...).orderby(...)");
		}
		mQueryBuilders.add(queryBuilder);
		return this;
	}
	
	/** Adds the given properties to the ORDER BY section using ascending order. */
	UnionQueryBuilder orderAsc(Property... properties) {
        orderAscOrDesc(" ASC", properties);
        return this;
    }

    /** Adds the given properties to the ORDER BY section using descending order. */
	UnionQueryBuilder orderDesc(Property... properties) {
        orderAscOrDesc(" DESC", properties);
        return this;
    }

    
    /** Adds the given properties to the ORDER BY section using the given custom order. */
    UnionQueryBuilder orderCustom(Property property, String customOrderForProperty) {
    	orderCustomInternal(property, customOrderForProperty);
        return this;
    }

    /**
     * Adds the given raw SQL string to the ORDER BY section. Do not use this for standard properties: ordedAsc and
     * orderDesc are prefered.
     */
    public UnionQueryBuilder orderRaw(String rawOrder) {
        orderRawInternal(rawOrder);
        return this;
    }
	
	/**
	 * Builds the union query and executes to return a cursor
	 * @return Cursor for the results of the query
	 */
	public Cursor cursor() {
		
		boolean first = true;

		ArrayList<String> parameters = new ArrayList<String>();
		
		StringBuilder sql = new StringBuilder();
		
		for(QueryBuilder<?> qb : mQueryBuilders) {
		
			if(!first) {
				sql.append(" UNION ");
			}
			
			Query<?> q = qb.build();
			
			sql.append(q.sql);

			for(String parameter : q.parameters) {
				parameters.add(parameter);
			}
			
			first = false;
        }
		
		if (orderBuilder != null && orderBuilder.length() > 0) {
			sql.append(" ORDER BY ").append(orderBuilder);
		}
		
		return mDao.getDatabase().rawQuery(sql.toString(), parameters.toArray(new String[parameters.size()]));
	}
}
