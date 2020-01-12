package cn.shenjunjie.booking.dto.request;

/**
 * @author JunjieShen
 */
public class BaseRequest {

	protected Integer page = 1;
	protected Integer limit = 20;

	public Integer getPage() {
		return page;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

}
