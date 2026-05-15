package com.nammapustaka.app.database;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Boolean;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class BorrowTransactionDao_Impl implements BorrowTransactionDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<BorrowTransactionEntity> __insertionAdapterOfBorrowTransactionEntity;

  private final SharedSQLiteStatement __preparedStmtOfMarkReturned;

  public BorrowTransactionDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfBorrowTransactionEntity = new EntityInsertionAdapter<BorrowTransactionEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `transactions` (`id`,`user_id`,`book_id`,`issue_date`,`due_date`,`returned`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final BorrowTransactionEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getUserId());
        statement.bindLong(3, entity.getBookId());
        statement.bindLong(4, entity.getIssueDateMillis());
        statement.bindLong(5, entity.getDueDateMillis());
        final int _tmp = entity.getReturned() ? 1 : 0;
        statement.bindLong(6, _tmp);
      }
    };
    this.__preparedStmtOfMarkReturned = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE transactions SET returned = 1 WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final BorrowTransactionEntity transaction,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfBorrowTransactionEntity.insertAndReturnId(transaction);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertAll(final List<BorrowTransactionEntity> transactions,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfBorrowTransactionEntity.insert(transactions);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object markReturned(final long transactionId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfMarkReturned.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, transactionId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfMarkReturned.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object hasActiveBorrow(final long bookId,
      final Continuation<? super Boolean> $completion) {
    final String _sql = "SELECT EXISTS(SELECT 1 FROM transactions WHERE book_id = ? AND returned = 0)";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, bookId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Boolean>() {
      @Override
      @NonNull
      public Boolean call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Boolean _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp != 0;
          } else {
            _result = false;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<BorrowRecordRow>> observeAllBorrowRecords() {
    final String _sql = "\n"
            + "        SELECT\n"
            + "            t.id AS transaction_id,\n"
            + "            t.user_id,\n"
            + "            t.book_id,\n"
            + "            t.issue_date,\n"
            + "            t.due_date,\n"
            + "            t.returned,\n"
            + "            u.name AS user_name,\n"
            + "            u.role AS user_role,\n"
            + "            u.roll AS user_roll,\n"
            + "            u.pin AS user_pin,\n"
            + "            b.title AS book_title,\n"
            + "            b.author AS book_author,\n"
            + "            b.category AS book_category,\n"
            + "            b.pages AS book_pages,\n"
            + "            b.summary AS book_summary,\n"
            + "            b.available AS book_available,\n"
            + "            b.image_path AS book_image_path,\n"
            + "            b.qr_data AS book_qr_data\n"
            + "        FROM transactions t\n"
            + "        INNER JOIN users u ON u.id = t.user_id\n"
            + "        INNER JOIN books b ON b.id = t.book_id\n"
            + "        ORDER BY t.returned ASC, t.due_date ASC, t.issue_date DESC\n"
            + "        ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"transactions", "users",
        "books"}, new Callable<List<BorrowRecordRow>>() {
      @Override
      @NonNull
      public List<BorrowRecordRow> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfTransactionId = 0;
          final int _cursorIndexOfUserId = 1;
          final int _cursorIndexOfBookId = 2;
          final int _cursorIndexOfIssueDateMillis = 3;
          final int _cursorIndexOfDueDateMillis = 4;
          final int _cursorIndexOfReturned = 5;
          final int _cursorIndexOfUserName = 6;
          final int _cursorIndexOfUserRole = 7;
          final int _cursorIndexOfUserRoll = 8;
          final int _cursorIndexOfUserPin = 9;
          final int _cursorIndexOfBookTitle = 10;
          final int _cursorIndexOfBookAuthor = 11;
          final int _cursorIndexOfBookCategory = 12;
          final int _cursorIndexOfBookPages = 13;
          final int _cursorIndexOfBookSummary = 14;
          final int _cursorIndexOfBookAvailable = 15;
          final int _cursorIndexOfBookImagePath = 16;
          final int _cursorIndexOfBookQrData = 17;
          final List<BorrowRecordRow> _result = new ArrayList<BorrowRecordRow>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final BorrowRecordRow _item;
            final long _tmpTransactionId;
            _tmpTransactionId = _cursor.getLong(_cursorIndexOfTransactionId);
            final long _tmpUserId;
            _tmpUserId = _cursor.getLong(_cursorIndexOfUserId);
            final long _tmpBookId;
            _tmpBookId = _cursor.getLong(_cursorIndexOfBookId);
            final long _tmpIssueDateMillis;
            _tmpIssueDateMillis = _cursor.getLong(_cursorIndexOfIssueDateMillis);
            final long _tmpDueDateMillis;
            _tmpDueDateMillis = _cursor.getLong(_cursorIndexOfDueDateMillis);
            final boolean _tmpReturned;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfReturned);
            _tmpReturned = _tmp != 0;
            final String _tmpUserName;
            _tmpUserName = _cursor.getString(_cursorIndexOfUserName);
            final String _tmpUserRole;
            _tmpUserRole = _cursor.getString(_cursorIndexOfUserRole);
            final String _tmpUserRoll;
            if (_cursor.isNull(_cursorIndexOfUserRoll)) {
              _tmpUserRoll = null;
            } else {
              _tmpUserRoll = _cursor.getString(_cursorIndexOfUserRoll);
            }
            final String _tmpUserPin;
            _tmpUserPin = _cursor.getString(_cursorIndexOfUserPin);
            final String _tmpBookTitle;
            _tmpBookTitle = _cursor.getString(_cursorIndexOfBookTitle);
            final String _tmpBookAuthor;
            _tmpBookAuthor = _cursor.getString(_cursorIndexOfBookAuthor);
            final String _tmpBookCategory;
            _tmpBookCategory = _cursor.getString(_cursorIndexOfBookCategory);
            final int _tmpBookPages;
            _tmpBookPages = _cursor.getInt(_cursorIndexOfBookPages);
            final String _tmpBookSummary;
            _tmpBookSummary = _cursor.getString(_cursorIndexOfBookSummary);
            final boolean _tmpBookAvailable;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfBookAvailable);
            _tmpBookAvailable = _tmp_1 != 0;
            final String _tmpBookImagePath;
            if (_cursor.isNull(_cursorIndexOfBookImagePath)) {
              _tmpBookImagePath = null;
            } else {
              _tmpBookImagePath = _cursor.getString(_cursorIndexOfBookImagePath);
            }
            final String _tmpBookQrData;
            if (_cursor.isNull(_cursorIndexOfBookQrData)) {
              _tmpBookQrData = null;
            } else {
              _tmpBookQrData = _cursor.getString(_cursorIndexOfBookQrData);
            }
            _item = new BorrowRecordRow(_tmpTransactionId,_tmpUserId,_tmpBookId,_tmpIssueDateMillis,_tmpDueDateMillis,_tmpReturned,_tmpUserName,_tmpUserRole,_tmpUserRoll,_tmpUserPin,_tmpBookTitle,_tmpBookAuthor,_tmpBookCategory,_tmpBookPages,_tmpBookSummary,_tmpBookAvailable,_tmpBookImagePath,_tmpBookQrData);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<BorrowRecordRow>> observeBorrowRecordsByReturned(final boolean returned) {
    final String _sql = "\n"
            + "        SELECT\n"
            + "            t.id AS transaction_id,\n"
            + "            t.user_id,\n"
            + "            t.book_id,\n"
            + "            t.issue_date,\n"
            + "            t.due_date,\n"
            + "            t.returned,\n"
            + "            u.name AS user_name,\n"
            + "            u.role AS user_role,\n"
            + "            u.roll AS user_roll,\n"
            + "            u.pin AS user_pin,\n"
            + "            b.title AS book_title,\n"
            + "            b.author AS book_author,\n"
            + "            b.category AS book_category,\n"
            + "            b.pages AS book_pages,\n"
            + "            b.summary AS book_summary,\n"
            + "            b.available AS book_available,\n"
            + "            b.image_path AS book_image_path,\n"
            + "            b.qr_data AS book_qr_data\n"
            + "        FROM transactions t\n"
            + "        INNER JOIN users u ON u.id = t.user_id\n"
            + "        INNER JOIN books b ON b.id = t.book_id\n"
            + "        WHERE t.returned = ?\n"
            + "        ORDER BY t.returned ASC, t.due_date ASC, t.issue_date DESC\n"
            + "        ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    final int _tmp = returned ? 1 : 0;
    _statement.bindLong(_argIndex, _tmp);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"transactions", "users",
        "books"}, new Callable<List<BorrowRecordRow>>() {
      @Override
      @NonNull
      public List<BorrowRecordRow> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfTransactionId = 0;
          final int _cursorIndexOfUserId = 1;
          final int _cursorIndexOfBookId = 2;
          final int _cursorIndexOfIssueDateMillis = 3;
          final int _cursorIndexOfDueDateMillis = 4;
          final int _cursorIndexOfReturned = 5;
          final int _cursorIndexOfUserName = 6;
          final int _cursorIndexOfUserRole = 7;
          final int _cursorIndexOfUserRoll = 8;
          final int _cursorIndexOfUserPin = 9;
          final int _cursorIndexOfBookTitle = 10;
          final int _cursorIndexOfBookAuthor = 11;
          final int _cursorIndexOfBookCategory = 12;
          final int _cursorIndexOfBookPages = 13;
          final int _cursorIndexOfBookSummary = 14;
          final int _cursorIndexOfBookAvailable = 15;
          final int _cursorIndexOfBookImagePath = 16;
          final int _cursorIndexOfBookQrData = 17;
          final List<BorrowRecordRow> _result = new ArrayList<BorrowRecordRow>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final BorrowRecordRow _item;
            final long _tmpTransactionId;
            _tmpTransactionId = _cursor.getLong(_cursorIndexOfTransactionId);
            final long _tmpUserId;
            _tmpUserId = _cursor.getLong(_cursorIndexOfUserId);
            final long _tmpBookId;
            _tmpBookId = _cursor.getLong(_cursorIndexOfBookId);
            final long _tmpIssueDateMillis;
            _tmpIssueDateMillis = _cursor.getLong(_cursorIndexOfIssueDateMillis);
            final long _tmpDueDateMillis;
            _tmpDueDateMillis = _cursor.getLong(_cursorIndexOfDueDateMillis);
            final boolean _tmpReturned;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfReturned);
            _tmpReturned = _tmp_1 != 0;
            final String _tmpUserName;
            _tmpUserName = _cursor.getString(_cursorIndexOfUserName);
            final String _tmpUserRole;
            _tmpUserRole = _cursor.getString(_cursorIndexOfUserRole);
            final String _tmpUserRoll;
            if (_cursor.isNull(_cursorIndexOfUserRoll)) {
              _tmpUserRoll = null;
            } else {
              _tmpUserRoll = _cursor.getString(_cursorIndexOfUserRoll);
            }
            final String _tmpUserPin;
            _tmpUserPin = _cursor.getString(_cursorIndexOfUserPin);
            final String _tmpBookTitle;
            _tmpBookTitle = _cursor.getString(_cursorIndexOfBookTitle);
            final String _tmpBookAuthor;
            _tmpBookAuthor = _cursor.getString(_cursorIndexOfBookAuthor);
            final String _tmpBookCategory;
            _tmpBookCategory = _cursor.getString(_cursorIndexOfBookCategory);
            final int _tmpBookPages;
            _tmpBookPages = _cursor.getInt(_cursorIndexOfBookPages);
            final String _tmpBookSummary;
            _tmpBookSummary = _cursor.getString(_cursorIndexOfBookSummary);
            final boolean _tmpBookAvailable;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfBookAvailable);
            _tmpBookAvailable = _tmp_2 != 0;
            final String _tmpBookImagePath;
            if (_cursor.isNull(_cursorIndexOfBookImagePath)) {
              _tmpBookImagePath = null;
            } else {
              _tmpBookImagePath = _cursor.getString(_cursorIndexOfBookImagePath);
            }
            final String _tmpBookQrData;
            if (_cursor.isNull(_cursorIndexOfBookQrData)) {
              _tmpBookQrData = null;
            } else {
              _tmpBookQrData = _cursor.getString(_cursorIndexOfBookQrData);
            }
            _item = new BorrowRecordRow(_tmpTransactionId,_tmpUserId,_tmpBookId,_tmpIssueDateMillis,_tmpDueDateMillis,_tmpReturned,_tmpUserName,_tmpUserRole,_tmpUserRoll,_tmpUserPin,_tmpBookTitle,_tmpBookAuthor,_tmpBookCategory,_tmpBookPages,_tmpBookSummary,_tmpBookAvailable,_tmpBookImagePath,_tmpBookQrData);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
