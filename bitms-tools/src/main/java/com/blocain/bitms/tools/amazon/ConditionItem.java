package com.blocain.bitms.tools.amazon;

/**
 * ConditionItem Introduce
 * <p>File：ConditionItem.java</p>
 * <p>Title: ConditionItem</p>
 * <p>Description: ConditionItem</p>
 * <p>Copyright: Copyright (c) 2017/8/3</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class ConditionItem
{
    /**
     * Conditions元组类型，目前支持二元组（{ ... }）、三元组（[ ... ]）。
     */
    enum TupleType
    {
        Two, Three
    }
    
    private String                  name;
    
    private MatchMode               matchMode;
    
    private String                  value;
    
    private ConditionItem.TupleType tupleType;
    
    private long                    minimum;
    
    private long                    maximum;
    
    public ConditionItem(String name, String value)
    {
        this.matchMode = MatchMode.Exact;
        this.name = name;
        this.value = value;
        this.tupleType = ConditionItem.TupleType.Two;
    }
    
    public ConditionItem(String name, long min, long max)
    {
        this.matchMode = MatchMode.Range;
        this.name = name;
        this.minimum = min;
        this.maximum = max;
        this.tupleType = ConditionItem.TupleType.Three;
    }
    
    public ConditionItem(MatchMode matchMode, String name, String value)
    {
        this.matchMode = matchMode;
        this.name = name;
        this.value = value;
        this.tupleType = ConditionItem.TupleType.Three;
    }
    
    public String jsonize()
    {
        String jsonizedCond = null;
        switch (tupleType)
        {
            case Two:
                jsonizedCond = String.format("{\"%s\":\"%s\"},", name, value);
                break;
            case Three:
                switch (matchMode)
                {
                    case Exact:
                        jsonizedCond = String.format("[\"eq\",\"$%s\",\"%s\"],", name, value);
                        break;
                    case StartWith:
                        jsonizedCond = String.format("[\"starts-with\",\"$%s\",\"%s\"],", name, value);
                        break;
                    case Range:
                        jsonizedCond = String.format("[\"content-length-range\",%d,%d],", minimum, maximum);
                        break;
                    default:
                        throw new IllegalArgumentException(String.format("Unsupported match mode %s", matchMode.toString()));
                }
                break;
        }
        return jsonizedCond;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public MatchMode getMatchMode()
    {
        return matchMode;
    }
    
    public void setMatchMode(MatchMode matchMode)
    {
        this.matchMode = matchMode;
    }
    
    public String getValue()
    {
        return value;
    }
    
    public void setValue(String value)
    {
        this.value = value;
    }
    
    public ConditionItem.TupleType getTupleType()
    {
        return tupleType;
    }
    
    public void setTupleType(ConditionItem.TupleType tupleType)
    {
        this.tupleType = tupleType;
    }
    
    public long getMinimum()
    {
        return minimum;
    }
    
    public void setMinimum(long minimum)
    {
        this.minimum = minimum;
    }
    
    public long getMaximum()
    {
        return maximum;
    }
    
    public void setMaximum(long maximum)
    {
        this.maximum = maximum;
    }
}

