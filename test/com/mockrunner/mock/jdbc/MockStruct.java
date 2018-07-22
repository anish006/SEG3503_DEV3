package com.mockrunner.mock.jdbc;

import java.sql.SQLException;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mockrunner.base.NestedApplicationException;

/**
 * Mock implementation of <code>Struct</code>.
 */
public class MockStruct implements Struct, Cloneable
{
    private String sqlTypeName;
    private List<Object> attributes;
    
    public MockStruct(Object[] attributes)
    {
        this(null, attributes);
    }
    
    public MockStruct(String sqlTypeName, Object[] attributes)
    {
        this.sqlTypeName = sqlTypeName;
        this.attributes = new ArrayList<Object>();
        addAttributes(attributes);
    }
    
    public MockStruct(String sqlTypeName)
    {
        this(sqlTypeName, new Object[0]);
    }
    
    public String getSQLTypeName() throws SQLException
    {
        return sqlTypeName;
    }
    
    public void setSQLTypeName(String sqlTypeName)
    {
        this.sqlTypeName = sqlTypeName;
    }

    public Object[] getAttributes() throws SQLException
    {
        return attributes.toArray();
    }

    public Object[] getAttributes(Map<String, Class<?>> map) throws SQLException
    {
        return getAttributes();
    }
    
    public void addAttribute(Object attribute)
    {
        attributes.add(attribute);
    }
    
    public void addAttributes(Object[] attributes)
    {
        for (Object attribute : attributes) {
            addAttribute(attribute);
        }
    }
    
    public void addAttributes(List<Object> attributes)
    {
        addAttributes(attributes.toArray());
    }
    
    public void clearAttributes()
    {
        attributes.clear();
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if(null == obj) return false;
        if(!obj.getClass().equals(this.getClass())) return false;
        MockStruct other = (MockStruct)obj;
        if(null != sqlTypeName && !sqlTypeName.equals(other.sqlTypeName)) return false;
        if(null != other.sqlTypeName && !other.sqlTypeName.equals(sqlTypeName)) return false;
        if(null == attributes && null == other.attributes) return true;
        if(null == attributes || null == other.attributes) return false;
        return attributes.equals(other.attributes);
    }

    @Override
    public int hashCode()
    {
        int hashCode = 17;
        if(null != sqlTypeName) hashCode = (31 * hashCode) + sqlTypeName.hashCode();
        if(null != attributes) hashCode = (31 * hashCode) + attributes.hashCode();
        return hashCode;
    }

    @Override
    public String toString()
    {
        return "Struct data: " + attributes.toString();
    }
    
    @Override
    public Object clone()
    {
        try
        {
            MockStruct copy = (MockStruct)super.clone();
            copy.attributes = new ArrayList<Object>();
            copy.addAttributes(attributes.toArray());
            return copy;
        } 
        catch(CloneNotSupportedException exc)
        {
            throw new NestedApplicationException(exc);
        }
    }
}
