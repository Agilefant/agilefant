package fi.hut.soberit.agilefant.db.hibernate;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.ParameterizedType;
import org.hibernate.usertype.UserType;

/**
 * This is a filter system built on top of hibernate user types. This enables
 * modifying data incoming/outgoing from/to the database on the fly. It's
 * invisible for the entity bean user.
 * <p>
 * Technically, this is a UserType, which delegates all UserType functionality
 * to some other UserType, but modifying the data passing trough. Subclasses may
 * define the filtering functionality in methods filterDown and filterUp.
 * Chaining the filters is possible.
 * <p>
 * Using the type is as follows:
 * <p>
 * <code>
 * &#064;TypeDef(<br>
 * 		name="name_of_type",<br>
 * 	    typeClass = your.filter.class,<br>
 * 	    parameters = { &#064;Parameter(name="subtypes", value="next.filter another.filter actual.user.type.used") }<br>
 * )</code>
 * </p>
 * <p>
 * So, parameter "subtypes" is a space separated list of first filters, and
 * lastly the actual user type used to serialize/deserialize the data into the
 * database. This way you can chain UserTypeFilter implementations.
 * <p>
 * With the TypeDef defined, one would apply the filtered type as follows:
 * <p>
 * <code>
 * &#064;Type(type="name_of_type")
 * </code>
 * </p>
 * 
 * @author Turkka Äijälä
 * @see fi.hut.soberit.agilefant.db.hibernate.StringTruncateFilter
 * @see fi.hut.soberit.agilefant.db.hibernate.StringEscapeFilter
 * @see fi.hut.soberit.agilefant.db.hibernate.TextUserType
 * @see fi.hut.soberit.agilefant.db.hibernate.VarcharUserType
 */
public abstract class UserTypeFilter implements UserType, ParameterizedType {

    /**
     * UserType, to which delegate all actual functionality to.
     */
    UserType subUserType;

    /**
     * Receives the parameters.
     */
    @Override
    public void setParameterValues(Properties parameters) {
        String subTypes = parameters.getProperty("subtypes");

        if (subTypes == null)
            throw new HibernateException("no subtypes defined for the filter");

        // separate the first token from the subtype list

        // find first space
        int firstSpace = subTypes.indexOf(' ');

        String currentType;

        // if no space
        if (firstSpace == -1) {
            // there's only one token, this is the type name
            currentType = new String(subTypes);
            // no more subTypes
            subTypes = null;
        } else {
            // everything before the space is the type name
            currentType = subTypes.substring(0, firstSpace);
            // update subTypes to exclude the read token
            subTypes = subTypes.substring(firstSpace + 1);
        }

        try {
            // get a Class instance for the type
            Class<?> clazz = Class.forName(currentType);

            // create an instance of that class
            Object classInstance = clazz.newInstance();

            if (!(classInstance instanceof UserType))
                throw new HibernateException("got a subtype class of invalid type: should be subclass of UserType");

            subUserType = (UserType) classInstance;
            // if the SubType is parametrized
            if (classInstance instanceof ParameterizedType) {

                // forward our parameters for it, only removing the first token
                // this enables chaining the filters

                Properties newParameters = new Properties(parameters);

                // replace subtypes with new type list, with first type removed
                if (subTypes != null) {
                    newParameters.setProperty("subtypes", subTypes);
                } else
                    // remove the value totally, if there's no more subtypes
                    newParameters.remove("subtypes");

                ParameterizedType paramUserType = (ParameterizedType) classInstance;
                paramUserType.setParameterValues(newParameters);
            }
        } catch (ClassNotFoundException cnfe) {
            throw new HibernateException("subtype not found", cnfe);
        } catch (IllegalAccessException iae) {
            throw new HibernateException("invalid subtype", iae);
        } catch (InstantiationException ie) {
            throw new HibernateException("invalid subtype", ie);
        }
    }

    /**
     * Subclass implemented method, with modifies the data coming "up" in the
     * UserType hierarchy, ie. from the database.
     * 
     * @param ob
     *            value to modify
     * @return modified value
     */
    protected Object filterUp(Object ob) {
        // by default no action
        return ob;
    }

    /**
     * Subclass implemented method, with modifies the data going "down" in the
     * UserType hierarchy, ie. to the database.
     * 
     * @param ob
     *            value to modify
     * @return modified value
     */
    protected Object filterDown(Object ob) {
        // by default no action
        return ob;
    }

    // "UserType" methods, which are implemented by delegating everything to
    // subUserType.
    // All data passed down to subUserType is filtered with FilterDown. All data
    // from
    // the subUserType is filtered with filterUp.
    // ///////////

    @Override
    public int[] sqlTypes() {
        return subUserType.sqlTypes();
    }

    @Override
    public Class<?> returnedClass() {
        return subUserType.returnedClass();
    }

    @Override
    public boolean isMutable() {
        return subUserType.isMutable();
    }

    @Override
    public Object deepCopy(Object value) {
        Object ob = subUserType.deepCopy(filterDown(value));
        return filterUp(ob);
    }

    @Override
    public boolean equals(Object x, Object y) {
        return subUserType.equals(filterDown(x), filterDown(y));
    }

    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        Object ob = subUserType.replace(filterDown(original), filterDown(target), owner);
        return filterUp(ob);
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws HibernateException, SQLException {
        Object ob = subUserType.nullSafeGet(rs, names, session, owner);

        return filterUp(ob);
    }

    @Override
    public void nullSafeSet(PreparedStatement statement, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
        Object filteredValue = filterDown(value);

        subUserType.nullSafeSet(statement, filteredValue, index, session);
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        return subUserType.hashCode(filterDown(x));
    }

    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        return subUserType.disassemble(filterDown(value));
    }

    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        Object ob = subUserType.assemble(cached, owner);
        return filterUp(ob);
    }
}
