/*
 * Copyright (C) 2011 Markus Junginger, greenrobot (http://greenrobot.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.greenrobot.dao.query;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.os.Process;
import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.DaoException;
import de.greenrobot.dao.Property;

/**
 * A repeatable query returning entities.
 * 
 * @author Markus
 * 
 * @param <T>
 *            The enitity class the query will return results for.
 */
// TODO support long, double and other types, not just Strings, for parameters
// TODO Make parameters setable by Property (if unique in paramaters)
// TODO Query for PKs/ROW IDs
// TODO Make query compilable
public class Query<T> extends AbstractQuery<T> {
    private final static class QueryData<T2> extends AbstractQueryData<T2, Query<T2>> {
        private final int limitPosition;
        private final int offsetPosition;

        QueryData(AbstractDao<T2, ?> dao, String sql, String[] initialValues, int limitPosition, int offsetPosition) {
            super(dao,sql,initialValues);
            this.limitPosition = limitPosition;
            this.offsetPosition = offsetPosition;
        }

        @Override
        protected Query<T2> createQuery() {
            return new Query<T2>(this, dao, sql, initialValues.clone(), limitPosition, offsetPosition);
        }

    }

    /** For internal use by greenDAO only. */
    public static <T2> Query<T2> internalCreate(AbstractDao<T2, ?> dao, String sql, Object[] initialValues) {
        return create(dao, sql, initialValues, -1, -1);
    }

    static <T2> Query<T2> create(AbstractDao<T2, ?> dao, String sql, Object[] initialValues, int limitPosition,
            int offsetPosition) {
        QueryData<T2> queryData = new QueryData<T2>(dao, sql, toStringArray(initialValues), limitPosition,
                offsetPosition);
        return queryData.forCurrentThread();
    }

    private final int limitPosition;
    private final int offsetPosition;
    private final QueryData<T> queryData;

    private Query(QueryData<T> queryData, AbstractDao<T, ?> dao, String sql, String[] initialValues, int limitPosition,
            int offsetPosition) {
        super(dao, sql, initialValues);
        this.queryData = queryData;
        this.limitPosition = limitPosition;
        this.offsetPosition = offsetPosition;
    }

    public Query<T> forCurrentThread() {
        return queryData.forCurrentThread(this);
    }

    /**
     * Sets the parameter (0 based) using the position in which it was added during building the query.
     */
    public void setParameter(int index, Object parameter) {
        if (index >= 0 && (index == limitPosition || index == offsetPosition)) {
            throw new IllegalArgumentException("Illegal parameter index: " + index);
        }
        super.setParameter(index, parameter);
    }

    /**
     * Sets the limit of the maximum number of results returned by this Query. {@link QueryBuilder#limit(int)} must have
     * been called on the QueryBuilder that created this Query object.
     */
    public void setLimit(int limit) {
        checkThread();
        if (limitPosition == -1) {
            throw new IllegalStateException("Limit must be set with QueryBuilder before it can be used here");
        }
        parameters[limitPosition] = Integer.toString(limit);
    }

    /**
     * Sets the offset for results returned by this Query. {@link QueryBuilder#offset(int)} must have been called on the
     * QueryBuilder that created this Query object.
     */
    public void setOffset(int offset) {
        checkThread();
        if (offsetPosition == -1) {
            throw new IllegalStateException("Offset must be set with QueryBuilder before it can be used here");
        }
        parameters[offsetPosition] = Integer.toString(offset);
    }

    /** Executes the query and returns the result as a list containing all entities loaded into memory. */
    public List<T> list() {
        checkThread();
        Cursor cursor = dao.getDatabase().rawQuery(sql, parameters);
        return daoAccess.loadAllAndCloseCursor(cursor);
    }
    
    /**
     * Returns a single string field of a list of results
     * @param property		The property to retrieve
     * @param fieldType		The type of the property
     * @return				The values of the property for all matches
     */
    public List<String> listOfFieldAsString(Property property) {
    	return listOfFieldAsString(property.columnName);
    }
    
    /**
     * Returns a single string field of a list of results
     * @param columnName	The column name to retrieve
     * @param fieldType		The type of the property
     * @return				The values of the property for all matches
     */
    public List<String> listOfFieldAsString(String columnName) {
    	List<Object> values = listOfField(columnName, FieldType.String);
    	
    	List<String> typedValues = new ArrayList<String>();
    	
    	for(Object value: values) {
    		typedValues.add((String)value);
    	}
    	
    	return typedValues;
    }
    
    /**
     * Returns a double field of a list of results
     * @param property		The property to retrieve
     * @param fieldType		The type of the property
     * @return				The values of the property for all matches
     */
    public List<Double> listOfFieldAsDouble(Property property) {
    	return listOfFieldAsDouble(property.columnName);
    }
    
    /**
     * Returns a double field of a list of results
     * @param columnName	The column name to retrieve
     * @param fieldType		The type of the property
     * @return				The values of the property for all matches
     */
    public List<Double> listOfFieldAsDouble(String columnName) {
    	List<Object> values = listOfField(columnName, FieldType.Double);
    	
    	List<Double> typedValues = new ArrayList<Double>();
    	
    	for(Object value: values) {
    		typedValues.add((Double)value);
    	}
    	
    	return typedValues;
    }
    
    /**
     * Returns an int field of a list of results
     * @param property		The property to retrieve
     * @param fieldType		The type of the property
     * @return				The values of the property for all matches
     */
    public List<Integer> listOfFieldAsInt(Property property) {
    	return listOfFieldAsInt(property.columnName);
    }
    
    /**
     * Returns an int field of a list of results
     * @param columnName	The column name to retrieve
     * @param fieldType		The type of the property
     * @return				The values of the property for all matches
     */
    public List<Integer> listOfFieldAsInt(String columnName) {
    	List<Object> values = listOfField(columnName, FieldType.Int);
    	
    	List<Integer> typedValues = new ArrayList<Integer>();
    	
    	for(Object value: values) {
    		typedValues.add((Integer)value);
    	}
    	
    	return typedValues;
    }
    
    /**
     * Returns a long field of a list of results
     * @param property		The property to retrieve
     * @param fieldType		The type of the property
     * @return				The values of the property for all matches
     */
    public List<Long> listOfFieldAsLong(Property property) {
    	return listOfFieldAsLong(property.columnName);
    }
    
    /**
     * Returns a long field of a list of results
     * @param columnName	The column name to retrieve
     * @param fieldType		The type of the property
     * @return				The values of the property for all matches
     */
    public List<Long> listOfFieldAsLong(String columnName) {
    	List<Object> values = listOfField(columnName, FieldType.Long);
    	
    	List<Long> typedValues = new ArrayList<Long>();
    	
    	for(Object value: values) {
    		typedValues.add((Long)value);
    	}
    	
    	return typedValues;
    }
    
    /**
     * Returns a byte array field of a list of results
     * @param property		The property to retrieve
     * @param fieldType		The type of the property
     * @return				The values of the property for all matches
     */
    public List<Byte[]> listOfFieldAsByteArray(Property property) {
    	return listOfFieldAsByteArray(property.columnName);
    }
    
    /**
     * Returns a byte array field of a list of results
     * @param columnName	The column name to retrieve
     * @param fieldType		The type of the property
     * @return				The values of the property for all matches
     */
    public List<Byte[]> listOfFieldAsByteArray(String columnName) {
    	List<Object> values = listOfField(columnName, FieldType.ByteArray);
    	
    	List<Byte[]> typedValues = new ArrayList<Byte[]>();
    	
    	for(Object value: values) {
    		typedValues.add((Byte[])value);
    	}
    	
    	return typedValues;
    }
    
    /**
     * Returns a single field of a single result as a string
     * @param property		The property to retrieve
     * @return				The value of the property
     */
    public String uniqueFieldAsString(Property property) {
    	return (String)uniqueField(property, FieldType.String);
    }
    
    /**
     * Returns a single field of a single result as an int
     * @param property		The property to retrieve
     * @return				The value of the property
     */
    public Integer uniqueFieldAsInt(Property property) {
    	return (Integer)uniqueField(property, FieldType.Int);
    }
    
    /**
     * Returns a single field of a single result as a double
     * @param property		The property to retrieve
     * @return				The value of the property
     */
    public Double uniqueFieldAsDouble(Property property) {
    	return (Double)uniqueField(property, FieldType.Double);
    }
    
    /**
     * Returns a single field of a single result as a long
     * @param property		The property to retrieve
     * @return				The value of the property
     */
    public Long uniqueFieldAsLong(Property property) {
    	return (Long)uniqueField(property, FieldType.Long);
    }
    
    /**
     * Returns a single field of a single result as a byte array
     * @param property		The property to retrieve
     * @return				The value of the property
     */
    public Byte[] uniqueFieldAsByteArray(Property property) {
    	return (Byte[])uniqueField(property, FieldType.ByteArray);
    }
    
    /**
     * Returns a single field of a single result
     * @param property		The property to retrieve
     * @param fieldType		The type of the property
     * @return				The value of the property
     */
    public Object uniqueField(Property property, FieldType fieldType) {
    	checkThread();
        Cursor cursor = dao.getDatabase().rawQuery(sql, parameters);
        
        int columnIndex = cursor.getColumnIndex(property.columnName);
        
        if(cursor.moveToNext()) {
        	return getValueFromCursor(cursor, columnIndex, fieldType);
        }
        
        return null;
    }
    
    /**
     * Returns a single field of a list of results
     * @param property		The property to retrieve
     * @param fieldType		The type of the property
     * @return				The values of the property for all matches
     */
    public List<Object> listOfField(Property property, FieldType fieldType) {
    	return listOfField(property.columnName, fieldType);
    }
    
    /**
     * Returns a single field of a list of results
     * @param property		The property to retrieve
     * @param fieldType		The type of the property
     * @return				The values of the property for all matches
     */
    public List<Object> listOfField(String columnName, FieldType fieldType) {
    	checkThread();
        Cursor cursor = dao.getDatabase().rawQuery(sql, parameters);
        
        List<Object> toReturn = new ArrayList<Object>();
        
        if(cursor.getCount() > 0) {
	        int columnIndex = cursor.getColumnIndex(columnName);
	        
	        if(columnIndex == -1) {
	        	columnIndex = cursor.getColumnIndex("'" + columnName + "'");
	        }
	        
	        if(columnIndex != -1) {
	        	while(cursor.moveToNext()) {
		        	toReturn.add(getValueFromCursor(cursor, columnIndex, fieldType));
		        }	
	        }
        }
        return toReturn;
    }
    
    
    private Object getValueFromCursor(Cursor cursor, int columnIndex, FieldType fieldType) {
    	switch(fieldType) {
	    	case ByteArray:
	    		return cursor.getBlob(columnIndex);
	    	case Double:
	    		return cursor.getDouble(columnIndex);
	    	case String:
	    		return cursor.getString(columnIndex);
	    	case Int:
	    		return cursor.getInt(columnIndex);
	    	case Long:
	    		return cursor.getLong(columnIndex);
	    	}
    	
    	return null;
    }
    
    /** Executes the query and returns the results as a cursor. */
    public Cursor cursor() {
        checkThread();
        return dao.getDatabase().rawQuery(sql, parameters);
    }

    /**
     * Executes the query and returns the result as a list that lazy loads the entities on first access. Entities are
     * cached, so accessing the same entity more than once will not result in loading an entity from the underlying
     * cursor again.Make sure to close it to close the underlying cursor.
     */
    public LazyList<T> listLazy() {
        checkThread();
        Cursor cursor = dao.getDatabase().rawQuery(sql, parameters);
        return new LazyList<T>(daoAccess, cursor, true);
    }

    /**
     * Executes the query and returns the result as a list that lazy loads the entities on every access (uncached). Make
     * sure to close the list to close the underlying cursor.
     */
    public LazyList<T> listLazyUncached() {
        checkThread();
        Cursor cursor = dao.getDatabase().rawQuery(sql, parameters);
        return new LazyList<T>(daoAccess, cursor, false);
    }

    /**
     * Executes the query and returns the result as a list iterator; make sure to close it to close the underlying
     * cursor. The cursor is closed once the iterator is fully iterated through.
     */
    public CloseableListIterator<T> listIterator() {
        return listLazyUncached().listIteratorAutoClose();
    }

    /**
     * Executes the query and returns the unique result or null.
     * 
     * @throws DaoException
     *             if the result is not unique
     * @return Entity or null if no matching entity was found
     */
    public T unique() {
        checkThread();
        Cursor cursor = dao.getDatabase().rawQuery(sql, parameters);
        return daoAccess.loadUniqueAndCloseCursor(cursor);
    }
   

    /**
     * Executes the query and returns the unique result (never null).
     * 
     * @throws DaoException
     *             if the result is not unique or no entity was found
     * @return Entity
     */
    public T uniqueOrThrow() {
        T entity = unique();
        if (entity == null) {
            throw new DaoException("No entity found for query");
        }
        return entity;
    }

}
