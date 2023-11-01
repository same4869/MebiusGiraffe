package com.pokemon.mebius.giraffe.base.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.pokemon.mebius.giraffe.base.entities.GiraffeHttpLogInfo;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "GIRAFFE_HTTP_LOG_INFO".
*/
public class GiraffeHttpLogInfoDao extends AbstractDao<GiraffeHttpLogInfo, Long> {

    public static final String TABLENAME = "GIRAFFE_HTTP_LOG_INFO";

    /**
     * Properties of entity GiraffeHttpLogInfo.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Host = new Property(1, String.class, "host", false, "HOST");
        public final static Property CurlBean = new Property(2, String.class, "curlBean", false, "CURL_BEAN");
        public final static Property Path = new Property(3, String.class, "path", false, "PATH");
        public final static Property RequestBody = new Property(4, String.class, "requestBody", false, "REQUEST_BODY");
        public final static Property ResponseStr = new Property(5, String.class, "responseStr", false, "RESPONSE_STR");
        public final static Property Size = new Property(6, String.class, "size", false, "SIZE");
        public final static Property RequestType = new Property(7, String.class, "requestType", false, "REQUEST_TYPE");
        public final static Property ResponseContentType = new Property(8, String.class, "responseContentType", false, "RESPONSE_CONTENT_TYPE");
        public final static Property ResponseCode = new Property(9, String.class, "responseCode", false, "RESPONSE_CODE");
        public final static Property RequestParamsMapString = new Property(10, String.class, "requestParamsMapString", false, "REQUEST_PARAMS_MAP_STRING");
        public final static Property Time = new Property(11, Long.class, "time", false, "TIME");
        public final static Property TookTime = new Property(12, Long.class, "tookTime", false, "TOOK_TIME");
        public final static Property IsSuccessRequest = new Property(13, boolean.class, "isSuccessRequest", false, "IS_SUCCESS_REQUEST");
        public final static Property IsExceptionRequest = new Property(14, boolean.class, "isExceptionRequest", false, "IS_EXCEPTION_REQUEST");
        public final static Property RequestHeaders = new Property(15, String.class, "requestHeaders", false, "REQUEST_HEADERS");
        public final static Property ResponseHeaders = new Property(16, String.class, "responseHeaders", false, "RESPONSE_HEADERS");
    }


    public GiraffeHttpLogInfoDao(DaoConfig config) {
        super(config);
    }
    
    public GiraffeHttpLogInfoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"GIRAFFE_HTTP_LOG_INFO\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"HOST\" TEXT," + // 1: host
                "\"CURL_BEAN\" TEXT," + // 2: curlBean
                "\"PATH\" TEXT," + // 3: path
                "\"REQUEST_BODY\" TEXT," + // 4: requestBody
                "\"RESPONSE_STR\" TEXT," + // 5: responseStr
                "\"SIZE\" TEXT," + // 6: size
                "\"REQUEST_TYPE\" TEXT," + // 7: requestType
                "\"RESPONSE_CONTENT_TYPE\" TEXT," + // 8: responseContentType
                "\"RESPONSE_CODE\" TEXT," + // 9: responseCode
                "\"REQUEST_PARAMS_MAP_STRING\" TEXT," + // 10: requestParamsMapString
                "\"TIME\" INTEGER," + // 11: time
                "\"TOOK_TIME\" INTEGER," + // 12: tookTime
                "\"IS_SUCCESS_REQUEST\" INTEGER NOT NULL ," + // 13: isSuccessRequest
                "\"IS_EXCEPTION_REQUEST\" INTEGER NOT NULL ," + // 14: isExceptionRequest
                "\"REQUEST_HEADERS\" TEXT," + // 15: requestHeaders
                "\"RESPONSE_HEADERS\" TEXT);"); // 16: responseHeaders
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"GIRAFFE_HTTP_LOG_INFO\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, GiraffeHttpLogInfo entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String host = entity.getHost();
        if (host != null) {
            stmt.bindString(2, host);
        }
 
        String curlBean = entity.getCurlBean();
        if (curlBean != null) {
            stmt.bindString(3, curlBean);
        }
 
        String path = entity.getPath();
        if (path != null) {
            stmt.bindString(4, path);
        }
 
        String requestBody = entity.getRequestBody();
        if (requestBody != null) {
            stmt.bindString(5, requestBody);
        }
 
        String responseStr = entity.getResponseStr();
        if (responseStr != null) {
            stmt.bindString(6, responseStr);
        }
 
        String size = entity.getSize();
        if (size != null) {
            stmt.bindString(7, size);
        }
 
        String requestType = entity.getRequestType();
        if (requestType != null) {
            stmt.bindString(8, requestType);
        }
 
        String responseContentType = entity.getResponseContentType();
        if (responseContentType != null) {
            stmt.bindString(9, responseContentType);
        }
 
        String responseCode = entity.getResponseCode();
        if (responseCode != null) {
            stmt.bindString(10, responseCode);
        }
 
        String requestParamsMapString = entity.getRequestParamsMapString();
        if (requestParamsMapString != null) {
            stmt.bindString(11, requestParamsMapString);
        }
 
        Long time = entity.getTime();
        if (time != null) {
            stmt.bindLong(12, time);
        }
 
        Long tookTime = entity.getTookTime();
        if (tookTime != null) {
            stmt.bindLong(13, tookTime);
        }
        stmt.bindLong(14, entity.getIsSuccessRequest() ? 1L: 0L);
        stmt.bindLong(15, entity.getIsExceptionRequest() ? 1L: 0L);
 
        String requestHeaders = entity.getRequestHeaders();
        if (requestHeaders != null) {
            stmt.bindString(16, requestHeaders);
        }
 
        String responseHeaders = entity.getResponseHeaders();
        if (responseHeaders != null) {
            stmt.bindString(17, responseHeaders);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, GiraffeHttpLogInfo entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String host = entity.getHost();
        if (host != null) {
            stmt.bindString(2, host);
        }
 
        String curlBean = entity.getCurlBean();
        if (curlBean != null) {
            stmt.bindString(3, curlBean);
        }
 
        String path = entity.getPath();
        if (path != null) {
            stmt.bindString(4, path);
        }
 
        String requestBody = entity.getRequestBody();
        if (requestBody != null) {
            stmt.bindString(5, requestBody);
        }
 
        String responseStr = entity.getResponseStr();
        if (responseStr != null) {
            stmt.bindString(6, responseStr);
        }
 
        String size = entity.getSize();
        if (size != null) {
            stmt.bindString(7, size);
        }
 
        String requestType = entity.getRequestType();
        if (requestType != null) {
            stmt.bindString(8, requestType);
        }
 
        String responseContentType = entity.getResponseContentType();
        if (responseContentType != null) {
            stmt.bindString(9, responseContentType);
        }
 
        String responseCode = entity.getResponseCode();
        if (responseCode != null) {
            stmt.bindString(10, responseCode);
        }
 
        String requestParamsMapString = entity.getRequestParamsMapString();
        if (requestParamsMapString != null) {
            stmt.bindString(11, requestParamsMapString);
        }
 
        Long time = entity.getTime();
        if (time != null) {
            stmt.bindLong(12, time);
        }
 
        Long tookTime = entity.getTookTime();
        if (tookTime != null) {
            stmt.bindLong(13, tookTime);
        }
        stmt.bindLong(14, entity.getIsSuccessRequest() ? 1L: 0L);
        stmt.bindLong(15, entity.getIsExceptionRequest() ? 1L: 0L);
 
        String requestHeaders = entity.getRequestHeaders();
        if (requestHeaders != null) {
            stmt.bindString(16, requestHeaders);
        }
 
        String responseHeaders = entity.getResponseHeaders();
        if (responseHeaders != null) {
            stmt.bindString(17, responseHeaders);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public GiraffeHttpLogInfo readEntity(Cursor cursor, int offset) {
        GiraffeHttpLogInfo entity = new GiraffeHttpLogInfo( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // host
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // curlBean
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // path
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // requestBody
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // responseStr
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // size
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // requestType
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // responseContentType
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // responseCode
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // requestParamsMapString
            cursor.isNull(offset + 11) ? null : cursor.getLong(offset + 11), // time
            cursor.isNull(offset + 12) ? null : cursor.getLong(offset + 12), // tookTime
            cursor.getShort(offset + 13) != 0, // isSuccessRequest
            cursor.getShort(offset + 14) != 0, // isExceptionRequest
            cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15), // requestHeaders
            cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16) // responseHeaders
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, GiraffeHttpLogInfo entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setHost(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setCurlBean(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setPath(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setRequestBody(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setResponseStr(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setSize(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setRequestType(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setResponseContentType(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setResponseCode(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setRequestParamsMapString(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setTime(cursor.isNull(offset + 11) ? null : cursor.getLong(offset + 11));
        entity.setTookTime(cursor.isNull(offset + 12) ? null : cursor.getLong(offset + 12));
        entity.setIsSuccessRequest(cursor.getShort(offset + 13) != 0);
        entity.setIsExceptionRequest(cursor.getShort(offset + 14) != 0);
        entity.setRequestHeaders(cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15));
        entity.setResponseHeaders(cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(GiraffeHttpLogInfo entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(GiraffeHttpLogInfo entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(GiraffeHttpLogInfo entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
