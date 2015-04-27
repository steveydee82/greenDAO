package de.greenrobot.dao.join;

import de.greenrobot.dao.Property;
import de.greenrobot.dao.Selectable;
import de.greenrobot.dao.query.QueryBuilder;

public class JoinBuilder<T> {

	private QueryBuilder<T> mQueryBuilder;
	private String mJoinTable;
	private String mJoinTableAlias;
	private JoinType mJoinType;
	private String mSourceProperty;
	private String mDestProperty;
	
	public JoinBuilder(QueryBuilder<T> queryBuilder, String joinTable, JoinType joinType) {
		mQueryBuilder = queryBuilder;
		mJoinTable = joinTable;
		mJoinType = joinType;
	}
	
	public JoinBuilder<T> alias(String joinTableAlias) {
		mJoinTableAlias = joinTableAlias;
		return this;
	}

	private String getQualitifedColumn(Selectable column) {
		if(mQueryBuilder.isMasterTable(column.getColumnPrefix())) {
			return mQueryBuilder.getMasterTablePrefix() + "." + column.getColumnName();
		} else {
			return column.getColumnPrefix() + "." + column.getColumnName();
		}
	}
	
	public QueryBuilder<T> on(Selectable sourceProperty, Selectable destProperty) {
		return on(getQualitifedColumn(sourceProperty), getQualitifedColumn(destProperty));
	}
	
	public QueryBuilder<T> on(Selectable sourceProperty, String destProperty) {
		return on(getQualitifedColumn(sourceProperty), destProperty);
	}
	
	public QueryBuilder<T> on(String sourceProperty, Selectable destProperty) {
		return on(sourceProperty, getQualitifedColumn(destProperty));
	}
	
	public QueryBuilder<T> on(String sourceProperty, String destProperty) {
		mSourceProperty = sourceProperty;
		mDestProperty = destProperty;
		return mQueryBuilder;
	}
	
	public String getJoinClause() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(getJoinSql());
		sb.append(getJoinToSql());
		sb.append("ON ");
		sb.append(mSourceProperty);
		sb.append(" = ");
		sb.append(mDestProperty);
		sb.append(" ");
		return sb.toString();
	}
	
	private String getJoinToSql() {
		String joinTo = "[" + mJoinTable + "] ";
		if(mJoinTableAlias != null && mJoinTableAlias.length() > 0) {
			joinTo +=  mJoinTableAlias + " ";
		}
		
		return joinTo;
	}
	
	
	private String getJoinSql() {
		switch(mJoinType) {
		case Left:
			return "LEFT JOIN ";
		case Cross:
			return "CROSS JOIN ";
		default:
			return "INNER JOIN ";
		}
	}
}
