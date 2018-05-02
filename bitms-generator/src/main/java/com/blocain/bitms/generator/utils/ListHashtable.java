package com.blocain.bitms.generator.utils;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * 扩展的一个Hash对象
 * <p>File：ListHashtable.java</p>
 * <p>Title: ListHashtable</p>
 * <p>Description:ListHashtable</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * 
 * @author Playguy
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public class ListHashtable extends Hashtable
{
    private static final long serialVersionUID = 8122682427787981757L;
    
    protected List            orderedKeys      = new ArrayList();
    
    @Override
    public synchronized void clear()
    {
        super.clear();
        orderedKeys = new ArrayList();
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public synchronized Object put(Object aKey, Object aValue)
    {
        if (orderedKeys.contains(aKey))
        {
            int pos = orderedKeys.indexOf(aKey);
            orderedKeys.remove(pos);
            orderedKeys.add(pos, aKey);
        }
        else
        {
            if (aKey instanceof Integer)
            {
                Integer key = (Integer) aKey;
                int pos = getFirstKeyGreater(key.intValue());
                if (pos >= 0)
                {
                    orderedKeys.add(pos, aKey);
                }
                else
                {
                    orderedKeys.add(aKey);
                }
            }
            else
            {
                orderedKeys.add(aKey);
            }
        }
        return super.put(aKey, aValue);
    }
    
    /**
     * @param aKey
     * @returns calculate position at which the first key is greater otherwise
     *          return -1 if no key can be found which is greater
     */
    private int getFirstKeyGreater(int aKey)
    {
        int pos = 0;
        int numKeys = getOrderedKeys().size();
        for (int i = 0; i < numKeys; i++)
        {
            Integer key = (Integer) getOrderedKey(i);
            int keyval = key.intValue();
            if (keyval < aKey)
            {
                ++pos;
            }
            else
            {
                break;
            }
        }
        if (pos >= numKeys)
        {
            pos = -1;
        }
        return pos;
    }
    
    @Override
    public synchronized Object remove(Object aKey)
    {
        if (orderedKeys.contains(aKey))
        {
            int pos = orderedKeys.indexOf(aKey);
            orderedKeys.remove(pos);
        }
        return super.remove(aKey);
    }
    
    /**
     * This method reorders the ListHashtable only if the keys used are integer
     * keys.
     */
    @SuppressWarnings("unchecked")
    public void reorderIntegerKeys()
    {
        List keys = getOrderedKeys();
        int numKeys = keys.size();
        if (numKeys <= 0) { return; }
        if (!(getOrderedKey(0) instanceof Integer)) { return; }
        List newKeys = new ArrayList();
        List newValues = new ArrayList();
        for (int i = 0; i < numKeys; i++)
        {
            Integer key = (Integer) getOrderedKey(i);
            Object val = getOrderedValue(i);
            int numNew = newKeys.size();
            int pos = 0;
            for (int j = 0; j < numNew; j++)
            {
                Integer newKey = (Integer) newKeys.get(j);
                if (newKey.intValue() < key.intValue())
                {
                    ++pos;
                }
                else
                {
                    break;
                }
            }
            if (pos >= numKeys)
            {
                newKeys.add(key);
                newValues.add(val);
            }
            else
            {
                newKeys.add(pos, key);
                newValues.add(pos, val);
            }
        }
        this.clear();
        for (int l = 0; l < numKeys; l++)
        {
            put(newKeys.get(l), newValues.get(l));
        }
    }
    
    @Override
    public String toString()
    {
        StringBuffer x = new StringBuffer();
        x.append("Ordered Keys: ");
        int numKeys = orderedKeys.size();
        x.append("[");
        for (int i = 0; i < numKeys; i++)
        {
            x.append(orderedKeys.get(i) + " ");
        }
        x.append("]\n");
        x.append("Ordered Values: ");
        x.append("[");
        for (int j = 0; j < numKeys; j++)
        {
            x.append(getOrderedValue(j) + " ");
        }
        x.append("]\n");
        return x.toString();
    }
    
    public void merge(ListHashtable newTable)
    {
        int num = newTable.size();
        for (int i = 0; i < num; i++)
        {
            Object aKey = newTable.getOrderedKey(i);
            Object aVal = newTable.getOrderedValue(i);
            this.put(aKey, aVal);
        }
    }
    
    public List getOrderedKeys()
    {
        return orderedKeys;
    }
    
    public Object getOrderedKey(int i)
    {
        return getOrderedKeys().get(i);
    }
    
    public Object getKeyForValue(Object aValue)
    {
        int num = getOrderedValues().size();
        for (int i = 0; i < num; i++)
        {
            Object tmpVal = getOrderedValue(i);
            if (tmpVal.equals(aValue)) { return getOrderedKey(i); }
        }
        return null;
    }
    
    @SuppressWarnings("unchecked")
    public List getOrderedValues()
    {
        List values = new ArrayList();
        int numKeys = orderedKeys.size();
        for (int i = 0; i < numKeys; i++)
        {
            values.add(get(getOrderedKey(i)));
        }
        return values;
    }
    
    public Object getOrderedValue(int i)
    {
        return get(getOrderedKey(i));
    }
}
