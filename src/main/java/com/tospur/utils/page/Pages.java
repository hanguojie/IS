package com.tospur.utils.page;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.miemiedev.mybatis.paginator.domain.Order;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.github.miemiedev.mybatis.paginator.jackson2.PageListJsonMapper;
import com.google.common.base.Strings;

public class Pages {

	/**
	 * 组装分页和排序参数
	 * 
	 * @param querier
	 * @return
	 */
	public static PageBounds build(PageQuerier querier) {
		return build(querier, true);
	}
	
	public static PageBounds build(PageQuerier querier, boolean isLimitPageIndexAndPageSize) {
		int pageIndex = querier.getPage();
		int pageSize = querier.getRows();
		String sortName = querier.getSidx();
		String sortOrder = querier.getSord();		
		return build(pageIndex, pageSize, sortName, sortOrder, isLimitPageIndexAndPageSize);
	}
	
	/**
	 * 组装分页和排序参数
	 * 
	 * @param pageIndex
	 *            第几页
	 * @param pageSize
	 *            每页的结果数量
	 * @param sortName
	 *            排序字段
	 * @param sortOrder
	 *            排序顺序 ASC|DESC
	 * @param doCheck
	 *            是否检查 pageIndex与PageSize
	 * @return
	 */
	public static PageBounds build(Integer pageIndex, Integer pageSize, String sortName, String sortOrder,
			boolean isLimitPageIndexAndPageSize) {
		if (isLimitPageIndexAndPageSize) {
			if (pageIndex <= 0) {
				pageIndex = 1;
			}
			if (pageSize <= 0 || pageSize > 3000) {
				pageSize = 3000;
			}
		}

		PageBounds pageBounds = null;
		if (Strings.isNullOrEmpty(sortName)) {
			pageBounds = new PageBounds(pageIndex, pageSize);
		} else {
			pageBounds = new PageBounds(pageIndex, pageSize, Order.create(sortName, sortOrder));
		}
		return pageBounds;
	}

	/**
	 * 组装分页和排序参数
	 * 
	 * @param pageIndex
	 *            第几页
	 * @param pageSize
	 *            每页的结果数量
	 * @param orderSegment
	 *            排序顺序, 例如：type asc, create_time desc
	 * @return
	 */
	/*public static PageBounds build(Integer pageIndex, Integer pageSize, String orderSegment) {
		if ( pageIndex <= 0) {
			pageIndex = 1;
		}
		if (pageSize<= 0 || pageSize > 3000) {
			pageSize = 3000;
		}

		PageBounds pageBounds = null;
		if (Strings.isNullOrEmpty(orderSegment)) {
			pageBounds = new PageBounds(pageIndex, pageSize);
		} else {
			pageBounds = new PageBounds(pageIndex, pageSize, Order.formString(orderSegment));
		}
		return pageBounds;
	}*/

	public static <T> PageList<T> toPageList(List<T> list) {
		if (null == list) {
			list = new ArrayList<T>();
		}
		return (PageList<T>) list;
	}

	public static <T> String toJson(PageList<T> pageList) throws JsonProcessingException {
		PageListJsonMapper pageListJsonMapper = new PageListJsonMapper();
		pageListJsonMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		String jsonStr = pageListJsonMapper.writeValueAsString(pageList);
		return jsonStr;
		//return JSON.toJSONStringWithDateFormat(pageList, Dates.FORMAT_DEFAULT);
	}
	
	public static <T> String toJson2(PageList<T> pageList) throws JsonProcessingException {
		PageListJsonMapper pageListJsonMapper = new PageListJsonMapper();
		pageListJsonMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
		String jsonStr = pageListJsonMapper.writeValueAsString(pageList);
		return jsonStr;
		//return JSON.toJSONStringWithDateFormat(pageList, Dates.FORMAT_DEFAULT);
	}
}
