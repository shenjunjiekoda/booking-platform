package cn.shenjunjie.booking.dto.response;

import cn.shenjunjie.booking.dto.request.BaseRequest;
import lombok.Data;

import java.util.List;

/**
 * @author: Junjie.Shen
 * @date: 2019/1/12
 * @description:
 */
@Data
public class PageBean<T> {

	private Integer pageNo;
	private Integer pageSize;
	private Long total;
	private List<T> items;

	public PageBean(BaseRequest request, List<T> items, long total) {
		this.pageNo = request.getPageCurrent();
		this.pageSize = request.getPageSize();
		this.total = total;
		this.items = items;
	}

}
