package cn.maiaimei.framework.mybatisplus.util;

import cn.maiaimei.framework.beans.PaginationResult;
import cn.maiaimei.framework.util.BeanUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

public class PaginationUtils {
    public static <S, T> PaginationResult<T> build(Page<S> page, Class<T> targetClass) {
        List<T> records = BeanUtils.copyList(page.getRecords(), targetClass);
        PaginationResult<T> paginationResult = new PaginationResult<>();
        paginationResult.setRecords(records);
        paginationResult.setTotal(page.getTotal());
        paginationResult.setCurrent(page.getCurrent());
        paginationResult.setSize(page.getSize());
        return paginationResult;
    }
}
