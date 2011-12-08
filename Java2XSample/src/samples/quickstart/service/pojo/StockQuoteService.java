package samples.quickstart.service.pojo;

import java.util.HashMap;

/**
 * This source is Axis sample. See
 * http://axis.apache.org/axis2/java/core/docs/quickstartguide.html
 * */
public class StockQuoteService {
    private HashMap<String, Double> map = new HashMap<String, Double>();

    public double getPrice(String symbol) {
	Double price = (Double) map.get(symbol);
	if (price != null) {
	    return price.doubleValue();
	}
	return 42.00;
    }

    public void update(String symbol, double price) {
	map.put(symbol, new Double(price));
    }
}