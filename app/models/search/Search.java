package models.search;

import com.avaje.ebean.PagedList;
import models.product.Product;

public class Search {

    private int page;
    private int pageSize;
    private String sortBy;
    private String order;
    private String filter;

    public Search(int page, int pageSize, String sortBy, String order, String filter) {
        this.page = page;
        this.pageSize = pageSize;
        this.sortBy = sortBy;
        this.order = order;
        this.filter = filter;
    }

    public PagedList<Product> doSearch() {
        return Product.page(page, pageSize, sortBy, order, filter);
    }
}
