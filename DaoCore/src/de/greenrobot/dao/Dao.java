package de.greenrobot.dao;

import java.util.Collection;
import java.util.List;

import android.database.sqlite.SQLiteDatabase;
import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;

public interface Dao<T, K> {

	public abstract AbstractDaoSession getSession();

	public abstract String getTablename();

	public abstract Property[] getProperties();

	public abstract Property getPkProperty();

	public abstract String[] getAllColumns();

	public abstract String[] getPkColumns();

	public abstract String[] getNonPkColumns();

	/**
	 * Loads and entity for the given PK.
	 * 
	 * @param key
	 *            a PK value or null
	 * @return The entity or null, if no entity matched the PK value
	 */
	public abstract T load(K key);

	public abstract T loadByRowId(long rowId);

	/** Loads all available entities from the database. */
	public abstract List<T> loadAll();

	/** Detaches an entity from the identity scope (session). Subsequent query results won't return this object. */
	public abstract boolean detach(T entity);

	/**
	 * Inserts the given entities in the database using a transaction.
	 * 
	 * @param entities
	 *            The entities to insert.
	 */
	public abstract void insertInTx(Iterable<T> entities);

	/**
	 * Inserts the given entities in the database using a transaction.
	 * 
	 * @param entities
	 *            The entities to insert.
	 */
	public abstract void insertInTx(T... entities);

	/**
	 * Inserts the given entities in the database using a transaction. The given entities will become tracked if the PK
	 * is set.
	 * 
	 * @param entities
	 *            The entities to insert.
	 * @param setPrimaryKey
	 *            if true, the PKs of the given will be set after the insert; pass false to improve performance.
	 */
	public abstract void insertInTx(Iterable<T> entities, boolean setPrimaryKey);

	/**
	 * Inserts or replaces the given entities in the database using a transaction. The given entities will become
	 * tracked if the PK is set.
	 * 
	 * @param entities
	 *            The entities to insert.
	 * @param setPrimaryKey
	 *            if true, the PKs of the given will be set after the insert; pass false to improve performance.
	 */
	public abstract void insertOrReplaceInTx(Iterable<T> entities,
			boolean setPrimaryKey);

	/**
	 * Inserts or replaces the given entities in the database using a transaction.
	 * 
	 * @param entities
	 *            The entities to insert.
	 */
	public abstract void insertOrReplaceInTx(Iterable<T> entities);

	/**
	 * Inserts or replaces the given entities in the database using a transaction.
	 * 
	 * @param entities
	 *            The entities to insert.
	 */
	public abstract void insertOrReplaceInTx(T... entities);

	/**
	 * Insert an entity into the table associated with a concrete DAO.
	 * 
	 * @return row ID of newly inserted entity
	 */
	public abstract long insert(T entity);

	/**
	 * Insert an entity into the table associated with a concrete DAO <b>without</b> setting key property. Warning: This
	 * may be faster, but the entity should not be used anymore. The entity also won't be attached to identy scope.
	 * 
	 * @return row ID of newly inserted entity
	 */
	public abstract long insertWithoutSettingPk(T entity);

	/**
	 * Insert an entity into the table associated with a concrete DAO.
	 * 
	 * @return row ID of newly inserted entity
	 */
	public abstract long insertOrReplace(T entity);


	/** A raw-style query where you can pass any WHERE clause and arguments. */
	public abstract List<T> queryRaw(String where, String... selectionArg);

	/**
	 * Creates a repeatable {@link Query} object based on the given raw SQL where you can pass any WHERE clause and
	 * arguments.
	 */
	public abstract Query<T> queryRawCreate(String where,
			Object... selectionArg);

	/**
	 * Creates a repeatable {@link Query} object based on the given raw SQL where you can pass any WHERE clause and
	 * arguments.
	 */
	public abstract Query<T> queryRawCreateListArgs(String where,
			Collection<Object> selectionArg);

	public abstract void deleteAll();

	/** Deletes the given entity from the database. Currently, only single value PK entities are supported. */
	public abstract void delete(T entity);

	/** Deletes an entity with the given PK from the database. Currently, only single value PK entities are supported. */
	public abstract void deleteByKey(K key);

	/**
	 * Deletes the given entities in the database using a transaction.
	 * 
	 * @param entities
	 *            The entities to delete.
	 */
	public abstract void deleteInTx(Iterable<T> entities);

	/**
	 * Deletes the given entities in the database using a transaction.
	 * 
	 * @param entities
	 *            The entities to delete.
	 */
	public abstract void deleteInTx(T... entities);

	/**
	 * Deletes all entities with the given keys in the database using a transaction.
	 * 
	 * @param keys
	 *            Keys of the entities to delete.
	 */
	public abstract void deleteByKeyInTx(Iterable<K> keys);

	/**
	 * Deletes all entities with the given keys in the database using a transaction.
	 * 
	 * @param keys
	 *            Keys of the entities to delete.
	 */
	public abstract void deleteByKeyInTx(K... keys);

	/** Resets all locally changed properties of the entity by reloading the values from the database. */
	public abstract void refresh(T entity);

	public abstract void update(T entity);

	public abstract QueryBuilder<T> queryBuilder();

	/**
	 * Updates the given entities in the database using a transaction.
	 * 
	 * @param entities
	 *            The entities to insert.
	 */
	public abstract void updateInTx(Iterable<T> entities);

	/**
	 * Updates the given entities in the database using a transaction.
	 * 
	 * @param entities
	 *            The entities to update.
	 */
	public abstract void updateInTx(T... entities);

	public abstract long count();

	/** Gets the SQLiteDatabase for custom database access. Not needed for greenDAO entities. */
	public abstract SQLiteDatabase getDatabase();

}