package cn.maiaimei.framework.mybatisplus.util;

import cn.maiaimei.framework.beans.PagingResult;
import cn.maiaimei.framework.util.BeanUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

public class PageUtils {
    public static <S, T> PagingResult<T> generate(Page<S> page, Class<T> targetClass) {
        List<T> records = BeanUtils.copyList(page.getRecords(), targetClass);
        PagingResult<T> pagingResult = new PagingResult<>();
        pagingResult.setRecords(records);
        pagingResult.setTotal(page.getTotal());
        pagingResult.setCurrent(page.getCurrent());
        pagingResult.setSize(page.getSize());
        return pagingResult;
    }
}
