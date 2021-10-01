package com.myproject.aem.core.services;

import javax.jcr.RepositoryException;
import java.util.List;

public interface SearchArticleService {

    public List<String> getJSONData(String path, String limit) throws RepositoryException;

}
