package org.foa.entity;

/**
 * @author miaomuzhi
 * @since 2018/8/11
 */
public enum ValueState {
    /**
     * 对看涨期权，价格C>行权价X为实值期权；C=X为平值；C小于X为虚值
     * 对看跌期权，价格P小于行权价X为实值；等于为平值；P大于X为虚值
     */
    VIRTUAL, FLAT, REAL
}
