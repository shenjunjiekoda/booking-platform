package cn.shenjunjie.booking.dto.request;

/**
 * @author JunjieShen
 */
public class BaseRequest {

	protected Integer pageCurrent = 1;
	protected Integer pageSize = 10;

	public Integer getPageCurrent() {
		return pageCurrent;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageCurrent(Integer pageCurrent) {
		this.pageCurrent = pageCurrent;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

}
