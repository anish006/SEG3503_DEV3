package com.mockrunner.jdbc;

import com.mockrunner.mock.jdbc.MockParameterMap;
import com.mockrunner.util.regexp.PatternMatcher;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Abstract base class for all statement types
 * that support out parameters, i.e. <code>CallableStatement</code>.
 */
public abstract class AbstractOutParameterResultSetHandler extends AbstractParameterResultSetHandler
{
    private boolean mustRegisterOutParameters = false;
    private MockParameterMap globalOutParameter = null;
    private final Map<String, MockParameterMap> outParameterForStatement = new TreeMap<String, MockParameterMap>();
    private final Map<PatternMatcher, MockParameterMap> outParameterForStatementCompiled = new TreeMap<PatternMatcher, MockParameterMap>();
    private final Map<String, List<ParameterWrapper<MockParameterMap>>> outParameterForStatementParameters = new TreeMap<String, List<ParameterWrapper<MockParameterMap>>>();
    private final Map<PatternMatcher, List<ParameterWrapper<MockParameterMap>>> outParameterForStatementParametersCompiled
          = new HashMap<PatternMatcher, List<ParameterWrapper<MockParameterMap>>>();

    @Override
    protected void onPatternMatcherFactoryChanged() {
        super.onPatternMatcherFactoryChanged();
        recompile(outParameterForStatement, outParameterForStatementCompiled);
        recompile(outParameterForStatementParameters, outParameterForStatementParametersCompiled);
    }

    /**
     * Set if out parameters must be registered to be returned.
     * The default is <code>false</code>, i.e. if there are matching
     * out parameters prepared, they are returned even if the
     * <code>registerOutParameter</code> methods of <code>CallableStatement</code>
     * have not been called. If set to <code>true</code>, <code>registerOutParameter</code>
     * must be called.
     * @param mustOutParameterBeRegistered must out parameter be registered
     */
    public void setMustRegisterOutParameters(boolean mustOutParameterBeRegistered)
    {
        this.mustRegisterOutParameters = mustOutParameterBeRegistered;
    }
    
    /**
     * Get if out parameter must be registered to be returned.
     * @return must out parameter be registered
     */
    public boolean getMustRegisterOutParameters()
    {
        return mustRegisterOutParameters;
    }
    
    /**
     * Returns the first out parameter <code>Map</code> that matches 
     * the specified SQL string.
     * Please note that you can modify the match parameters with 
     * {@link #setCaseSensitive}, {@link #setExactMatch} and 
     * {@link #setUseRegularExpressions}.
     * @param sql the SQL string
     * @return the corresponding out parameter <code>Map</code>
     */
    public MockParameterMap getOutParameter(String sql)
    {
        List<MockParameterMap> list = getMatchingObjects(outParameterForStatementCompiled, sql);
        if(null != list && list.size() > 0)
        {
            return list.get(0);
        }
        return null;
    }
    
    /**
     * Returns the first out parameter <code>Map</code> that matches 
     * the specified SQL string and the specified parameters. 
     * Please note that you can modify the match parameters with 
     * {@link #setCaseSensitive}, {@link #setExactMatch} and 
     * {@link #setUseRegularExpressions} and the match parameters for the 
     * specified parameter list with {@link #setExactMatchParameter}.
     * @param sql the SQL string
     * @param parameters the parameters
     * @return the corresponding out parameter <code>Map</code>
     */
    public MockParameterMap getOutParameter(String sql, MockParameterMap parameters)
    {
        ParameterWrapper<MockParameterMap> wrapper = getMatchingParameterWrapper(sql, parameters, outParameterForStatementParametersCompiled, exactMatchParameter);
        if(null != wrapper)
        {
            return wrapper.getWrappedObject();
        }
        return null;
    }
    
    /**
     * Clears the out parameters.
     */
    public void clearOutParameter()
    {
        outParameterForStatement.clear();
        outParameterForStatementCompiled.clear();
        outParameterForStatementParameters.clear();
        outParameterForStatementParametersCompiled.clear();
    }
    
    /**
     * Returns the global out parameter <code>Map</code>.
     * @return the global out parameter <code>Map</code>
     */
    public MockParameterMap getGlobalOutParameter()
    {
        return globalOutParameter;
    }
    
    /**
     * Prepares the global out parameter <code>Map</code>.
     * @param outParameters the global out parameter <code>Map</code>
     */
    public void prepareGlobalOutParameter(MockParameterMap outParameters)
    {
        globalOutParameter = new MockParameterMap(outParameters);
    }
    
    /**
     * Prepare an out parameter <code>Map</code> for a specified 
     * SQL string.
     * Please note that you can modify the match parameters with 
     * {@link #setCaseSensitive}, {@link #setExactMatch} and 
     * {@link #setUseRegularExpressions}.
     * @param sql the SQL string
     * @param outParameters the out parameter <code>Map</code>
     */
    public void prepareOutParameter(String sql, MockParameterMap outParameters)
    {
        MockParameterMap mockParameterMap = new MockParameterMap(outParameters);
        outParameterForStatement.put(sql, mockParameterMap);
        outParameterForStatementCompiled.put(getPatternMatcherFactory().create(sql), mockParameterMap);
    }
    
    /**
     * Prepare an out parameter <code>Map</code> for a specified SQL string and
     * the specified parameters. The specified parameters array
     * must contain the parameters in the correct order starting with index 0 for
     * the first parameter. Please keep in mind that parameters in
     * <code>CallableStatement</code> objects start with 1 as the first
     * parameter. So <code>parameters[0]</code> maps to the
     * parameter with index 1.
     * Please note that you can modify the match parameters with 
     * {@link #setCaseSensitive}, {@link #setExactMatch} and 
     * {@link #setUseRegularExpressions} and the match parameters for the 
     * specified parameter list with {@link #setExactMatchParameter}.
     * @param sql the SQL string
     * @param outParameters the corresponding out parameter <code>Map</code>
     * @param parameters the parameters
     */
    public void prepareOutParameter(String sql, MockParameterMap outParameters, Object[] parameters)
    {
        prepareOutParameter(sql, outParameters, Arrays.asList(parameters));
    }
    
    /**
     * Prepare an out parameter <code>Map</code> for a specified SQL string and
     * the specified parameters. The specified parameters array
     * must contain the parameters in the correct order starting with index 0 for
     * the first parameter. Please keep in mind that parameters in
     * <code>CallableStatement</code> objects start with 1 as the first
     * parameter. So <code>parameters.get(0)</code> maps to the
     * parameter with index 1.
     * Please note that you can modify the match parameters with 
     * {@link #setCaseSensitive}, {@link #setExactMatch} and 
     * {@link #setUseRegularExpressions} and the match parameters for the 
     * specified parameter list with {@link #setExactMatchParameter}.
     * @param sql the SQL string
     * @param outParameters the corresponding out parameter <code>Map</code>
     * @param parameters the parameters
     */
    public void prepareOutParameter(String sql, MockParameterMap outParameters, List<Object> parameters)
    {
        MockParameterMap params = new MockParameterMap();
        for(int ii = 0; ii < parameters.size(); ii++)
        {
            params.put(ii + 1, parameters.get(ii));
        }
        prepareOutParameter(sql, outParameters,  params);
    }
    
    /**
     * Prepare an out parameter <code>Map</code> for a specified SQL string
     * and the specified parameters. The specified parameters <code>Map</code>
     * must contain the parameters by mapping <code>Integer</code> or
     * <code>String</code> objects to the corresponding parameter. 
     * An <code>Integer</code> object is the index of the parameter.
     * A <code>String</code> is the name of the parameter.
     * Please note that you can modify the match parameters with 
     * {@link #setCaseSensitive}, {@link #setExactMatch} and 
     * {@link #setUseRegularExpressions} and the match parameters for the 
     * specified parameter list with {@link #setExactMatchParameter}.
     * @param sql the SQL string
     * @param outParameters the corresponding out parameter <code>Map</code>
     * @param parameters the parameters
     */
    public void prepareOutParameter(String sql, MockParameterMap outParameters, MockParameterMap parameters)
    {
        List<ParameterWrapper<MockParameterMap>> list = getListFromMapForSQLStatement(sql, outParameterForStatementParameters);
        list.add(new ParameterWrapper<MockParameterMap>(new MockParameterMap(outParameters), new MockParameterMap(parameters)));
        outParameterForStatementParametersCompiled.put(getPatternMatcherFactory().create(sql), list);
    }
    
    public void removeOutParameter(String sql){
        outParameterForStatement.remove(sql);
        outParameterForStatementCompiled.remove(getPatternMatcherFactory().create(sql));
    }

    public void removeOutParameter(String sql, MockParameterMap parameters){
        removeMatchingParameterWrapper(sql, parameters, outParameterForStatementParameters, exactMatchParameter);
    }
    
}
