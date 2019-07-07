package models;

import com.avaje.ebean.PagedList;
import models.product.Product;
import models.search.Search;
import models.search.SearchCommand;

/**
 * UserInterface class uses the Facade design pattern to provide various functionalities
 * such as search, purchase and delivery through a common interface
 */
public class UserInterface {

    public static PagedList<Product> doSearch(int page, int pageSize, String sortBy, String order, String filter) {
        SearchCommand searchCommand = new SearchCommand(new Search(page, pageSize, sortBy, order, filter));
        return searchCommand.execute();
    }

    public void doPurchase() {

    }

    public void doDelivery() {

    }
}
