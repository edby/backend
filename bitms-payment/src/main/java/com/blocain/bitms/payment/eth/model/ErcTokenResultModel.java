package com.blocain.bitms.payment.eth.model;

/**
 * Created by admin on 2018/2/26.
 */
public class ErcTokenResultModel
{
    private String blockHash;
    
    private String blockNumber;
    
    private String from;
    
    private String gas;
    
    private String gasPrice;
    
    private String hash;
    
    private String input;
    
    private String nonce;
    
    private String to;
    
    private String transactionIndex;
    
    private String value;
    
    private String v;
    
    private String r;
    
    private String s;
    
    public String getBlockHash()
    {
        return blockHash;
    }
    
    public void setBlockHash(String blockHash)
    {
        this.blockHash = blockHash;
    }
    
    public String getBlockNumber()
    {
        return blockNumber;
    }
    
    public void setBlockNumber(String blockNumber)
    {
        this.blockNumber = blockNumber;
    }
    
    public String getFrom()
    {
        return from;
    }
    
    public void setFrom(String from)
    {
        this.from = from;
    }
    
    public String getGas()
    {
        return gas;
    }
    
    public void setGas(String gas)
    {
        this.gas = gas;
    }
    
    public String getGasPrice()
    {
        return gasPrice;
    }
    
    public void setGasPrice(String gasPrice)
    {
        this.gasPrice = gasPrice;
    }
    
    public String getHash()
    {
        return hash;
    }
    
    public void setHash(String hash)
    {
        this.hash = hash;
    }
    
    public String getInput()
    {
        return input;
    }
    
    public void setInput(String input)
    {
        this.input = input;
    }
    
    public String getNonce()
    {
        return nonce;
    }
    
    public void setNonce(String nonce)
    {
        this.nonce = nonce;
    }
    
    public String getTo()
    {
        return to;
    }
    
    public void setTo(String to)
    {
        this.to = to;
    }
    
    public String getTransactionIndex()
    {
        return transactionIndex;
    }
    
    public void setTransactionIndex(String transactionIndex)
    {
        this.transactionIndex = transactionIndex;
    }
    
    public String getValue()
    {
        return value;
    }
    
    public void setValue(String value)
    {
        this.value = value;
    }
    
    public String getV()
    {
        return v;
    }
    
    public void setV(String v)
    {
        this.v = v;
    }
    
    public String getR()
    {
        return r;
    }
    
    public void setR(String r)
    {
        this.r = r;
    }
    
    public String getS()
    {
        return s;
    }
    
    public void setS(String s)
    {
        this.s = s;
    }
    
    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("ErcTokenResultModel{");
        sb.append("blockHash='").append(blockHash).append('\'');
        sb.append(", blockNumber='").append(blockNumber).append('\'');
        sb.append(", from='").append(from).append('\'');
        sb.append(", gas='").append(gas).append('\'');
        sb.append(", gasPrice='").append(gasPrice).append('\'');
        sb.append(", hash='").append(hash).append('\'');
        sb.append(", input='").append(input).append('\'');
        sb.append(", nonce='").append(nonce).append('\'');
        sb.append(", to='").append(to).append('\'');
        sb.append(", transactionIndex='").append(transactionIndex).append('\'');
        sb.append(", value='").append(value).append('\'');
        sb.append(", v='").append(v).append('\'');
        sb.append(", r='").append(r).append('\'');
        sb.append(", s='").append(s).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
