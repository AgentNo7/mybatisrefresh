package cn.com.seryou.mybatisrefresh;

/**
 * Description:
 *
 * @author yhf
 * @since 2019/8/26
 */
public interface MybatisRefresher {

    /**
     * 热部署mapper
     */
    public void refresh();
}
