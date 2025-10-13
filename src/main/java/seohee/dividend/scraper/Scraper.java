package seohee.dividend.scraper;

import seohee.dividend.model.Company;
import seohee.dividend.model.ScrapedResult;

public interface Scraper {
    Company scrapCompanyByTicker(String ticker);
    ScrapedResult scrap(Company company);
}
