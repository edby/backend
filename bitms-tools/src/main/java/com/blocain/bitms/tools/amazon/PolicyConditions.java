package com.blocain.bitms.tools.amazon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * PolicyConditions Introduce
 * <p>File：PolicyConditions.java</p>
 * <p>Title: PolicyConditions</p>
 * <p>Description: PolicyConditions</p>
 * <p>Copyright: Copyright (c) 2017/8/3</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class PolicyConditions
{
    public final static String                  COND_CONTENT_LENGTH_RANGE    = "content-length-range";
    
    public final static String                  COND_CACHE_CONTROL           = "Cache-Control";
    
    public final static String                  COND_CONTENT_TYPE            = "Content-Type";
    
    public final static String                  COND_CONTENT_DISPOSITION     = "Content-Disposition";
    
    public final static String                  COND_CONTENT_ENCODING        = "Content-Encoding";
    
    public final static String                  COND_EXPIRES                 = "Expires";
    
    public final static String                  COND_KEY                     = "key";
    
    public final static String                  COND_SUCCESS_ACTION_REDIRECT = "success_action_redirect";
    
    public final static String                  COND_SUCCESS_ACTION_STATUS   = "success_action_status";
    
    public final static String                  COND_X_OSS_META_PREFIX       = "x-oss-meta-";
    
    private static Map<String, List<MatchMode>> _supportedMatchRules         = new HashMap<String, List<MatchMode>>();
    
    private List<ConditionItem>                 _conds                       = new ArrayList<ConditionItem>();
    static
    {
        List<MatchMode> ordinaryMatchModes = new ArrayList<MatchMode>();
        ordinaryMatchModes.add(MatchMode.Exact);
        ordinaryMatchModes.add(MatchMode.StartWith);
        List<MatchMode> specialMatchModes = new ArrayList<MatchMode>();
        specialMatchModes.add(MatchMode.Range);
        _supportedMatchRules.put(COND_CONTENT_LENGTH_RANGE, specialMatchModes);
        _supportedMatchRules.put(COND_CACHE_CONTROL, ordinaryMatchModes);
        _supportedMatchRules.put(COND_CONTENT_TYPE, ordinaryMatchModes);
        _supportedMatchRules.put(COND_CONTENT_DISPOSITION, ordinaryMatchModes);
        _supportedMatchRules.put(COND_CONTENT_ENCODING, ordinaryMatchModes);
        _supportedMatchRules.put(COND_EXPIRES, ordinaryMatchModes);
        _supportedMatchRules.put(COND_KEY, ordinaryMatchModes);
        _supportedMatchRules.put(COND_SUCCESS_ACTION_REDIRECT, ordinaryMatchModes);
        _supportedMatchRules.put(COND_SUCCESS_ACTION_STATUS, ordinaryMatchModes);
        _supportedMatchRules.put(COND_X_OSS_META_PREFIX, ordinaryMatchModes);
    }
    
    /**
     * 采用默认匹配方式（精确匹配）添加Conditions项。
     * @param name Condition名称。
     * @param value Condition数值。
     */
    public void addConditionItem(String name, String value)
    {
        checkMatchModes(MatchMode.Exact, name);
        _conds.add(new ConditionItem(name, value));
    }
    
    /**
     * 采用指定匹配模式添加Conditions项。
     * @param matchMode Conditions匹配方式。
     * @param name Condition名称。
     * @param value Condition数值。
     */
    public void addConditionItem(MatchMode matchMode, String name, String value)
    {
        checkMatchModes(matchMode, name);
        _conds.add(new ConditionItem(matchMode, name, value));
    }
    
    /**
     * 采用范围匹配模式添加Conditions项。
     * @param name Condition名称。
     * @param min 范围最小值。
     * @param max 范围最小值。
     */
    public void addConditionItem(String name, long min, long max)
    {
        if (min > max) throw new IllegalArgumentException(String.format("Invalid range [%d, %d].", min, max));
        _conds.add(new ConditionItem(name, min, max));
    }
    
    private void checkMatchModes(MatchMode matchMode, String condName)
    {
        if (_supportedMatchRules.containsKey(condName))
        {
            List<MatchMode> mms = _supportedMatchRules.get(condName);
            if (!mms.contains(matchMode)) throw new IllegalArgumentException(String.format("Unsupported match mode for condition item %s", condName));
        }
    }
    
    public String jsonize()
    {
        StringBuilder jsonizedConds = new StringBuilder();
        jsonizedConds.append("\"conditions\":[");
        for (ConditionItem cond : _conds)
            jsonizedConds.append(cond.jsonize());
        if (_conds.size() > 0) jsonizedConds.deleteCharAt(jsonizedConds.length() - 1);
        jsonizedConds.append("]");
        return jsonizedConds.toString();
    }
}
