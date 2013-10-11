package fi.hut.soberit.agilefant.db.hibernate;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.UserType;

/**
 * UserType, which saves strings as VARCHARs.
 * <p>
 * Normally you wouldn't write UserTypes that do nothing special, you would use
 * Hibernate built-in types instead. This is for the filtering functionality
 * however: it enables a UserType to be at the bottom of the filtering
 * hierarchy.
 * 
 * @author Turkka Äijälä
 * @see fi.hut.soberit.agilefant.db.hibernate.TextUserType
 * @see fi.hut.soberit.agilefant.db.hibernate.UserTypeFilter
 */
public class VarcharUserType implements UserType {

    private static final int[] SQL_TYPES = { Types.VARCHAR };

    @Override
    public int[] sqlTypes() {
        return SQL_TYPES;
    }

    @Override
    public Class<?> returnedClass() {
        return String.class;
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Object deepCopy(Object value) {
        if (value == null)
            return null;
        String str = (String) value;
        return new String(str);
    }

    @Override
    public boolean equals(Object x, Object y) {
        if (x == y)
            return true;
        if (x == null || y == null)
            return false;
        return x.equals(y);
    }

    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        String t = (String) original;
        return new String(t);
    }

    @Override
    public Object nullSafeGet(ResultSet resultSet, String[] names, SessionImplementor session, Object owner) throws HibernateException, SQLException {
        String s = resultSet.getString(names[0]);

        if (resultSet.wasNull())
            return null;

        return new String(s);
    }

    @Override
    public void nullSafeSet(PreparedStatement statement, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
        if (value == null) {
            statement.setNull(index, Types.VARCHAR);
            return;
        }

        String str = (String) value;

        statement.setString(index, str);
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        return x.hashCode();
    }

    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) value;
    }

    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return cached;
    }
}
