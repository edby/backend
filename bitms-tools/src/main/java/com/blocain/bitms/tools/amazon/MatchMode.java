package com.blocain.bitms.tools.amazon;

/**
 * MatchMode Introduce
 * <p>File：MatchMode.java</p>
 * <p>Title: MatchMode</p>
 * <p>Description: MatchMode</p>
 * <p>Copyright: Copyright (c) 2017/8/3</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
/**
 * Post Policy Conditions匹配方式。
 */
public enum MatchMode {
    Unknown,
    Exact,  // 精确匹配
    StartWith,  // Starts With
    Range   // 指定文件大小
}