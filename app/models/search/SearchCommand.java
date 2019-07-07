package models.search;

import com.avaje.ebean.PagedList;
import models.product.Product;
import models.UserInterfaceCommand;

public class SearchCommand implements UserInterfaceCommand {

    private Search search;

    public SearchCommand(Search search) {
        this.search = search;
    }

    @Override
    public PagedList<Product> execute() {
        return search.doSearch();
    }
}
