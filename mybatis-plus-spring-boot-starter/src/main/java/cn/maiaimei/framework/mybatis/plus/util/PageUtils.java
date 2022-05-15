package cn.maiaimei.framework.mybatis.plus.util;

import cn.maiaimei.framework.beans.PageResult;
import cn.maiaimei.framework.util.BeanUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

public class PageUtils {
    public static <T> PageResult getPageResult(Page page, Class<T> targetClass) {
        List<T> records = BeanUtils.copyList(page.getRecords(), targetClass);
        PageResult<T> pageResult = new PageResult<>();
        pageResult.setRecords(records);
        pageResult.setTotal(page.getTotal());
        pageResult.setCurrent(page.getCurrent());
        pageResult.setSize(page.getSize());
        return pageResult;
    }
}