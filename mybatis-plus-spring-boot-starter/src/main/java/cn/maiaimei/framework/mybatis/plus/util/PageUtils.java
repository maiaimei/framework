package cn.maiaimei.framework.mybatis.plus.util;

import cn.maiaimei.framework.beans.PagingResult;
import cn.maiaimei.framework.util.BeanUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

public class PageUtils {
    public static <T> PagingResult generate(Page page, Class<T> targetClass) {
        List<T> records = BeanUtils.copyList(page.getRecords(), targetClass);
        PagingResult<T> pagingResult = new PagingResult<>();
        pagingResult.setRecords(records);
        pagingResult.setTotal(page.getTotal());
        pagingResult.setCurrent(page.getCurrent());
        pagingResult.setSize(page.getSize());
        return pagingResult;
    }
}